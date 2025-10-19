package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.Inventory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface DashRepository : JpaRepository<Inventory, String> {

    // 1ª KPI – Total de cartas em estoque
    @Query(
        """
        SELECT 
            COALESCE(SUM(inv.quantity), 0) AS totalCardsInStock
        FROM inventory inv
        JOIN product p ON p.id = inv.product_id
        WHERE p.card_id IS NOT NULL
        """,
        nativeQuery = true
    )
    fun getTotalCardsInStock(): Long

    // 2ª KPI – Total de booster boxes em estoque
    @Query(
        """
        SELECT 
            COALESCE(SUM(inv.quantity), 0) AS totalBoosterBoxes
        FROM inventory inv
        JOIN product p ON p.id = inv.product_id
        WHERE p.other_product_id IS NOT NULL
          AND p.deleted = FALSE
          AND EXISTS (
              SELECT 1 FROM other_product op 
              WHERE op.id = p.other_product_id 
              AND op.type = 'BOOSTER_BOX'
          )
        """,
        nativeQuery = true
    )
    fun getTotalBoosterBoxes(): Long

    // 3ª KPI – Pokémon com maior quantidade total em estoque
    @Query(
        """
        SELECT 
            c.title AS pokemonName,
            COALESCE(SUM(inv.quantity), 0) AS totalQuantity
        FROM card c
        JOIN product p ON p.card_id = c.id
        JOIN inventory inv ON inv.product_id = p.id
        GROUP BY c.title
        ORDER BY totalQuantity DESC
        LIMIT 1
        """,
        nativeQuery = true
    )
    fun getTopPokemonByStock(): Map<String, Any>

    // 4ª KPI – Coleção com maior número total de itens (cartas + produtos)
    @Query(
        """
        SELECT 
            col.name AS collectionName,
            (COUNT(DISTINCT c.id) + COUNT(DISTINCT p.id)) AS totalItems
        FROM collection col
        LEFT JOIN card c ON c.collection_id = col.id AND c.deleted = FALSE
        LEFT JOIN product p ON p.store_id IS NOT NULL AND p.deleted = FALSE
        GROUP BY col.name
        ORDER BY totalItems DESC
        LIMIT 1
        """,
        nativeQuery = true
    )
    fun getTopCollectionByItems(): Map<String, Any>

    // Gráfico de Aquisições Capex
    @Query(
        """
        SELECT 
            DATE_FORMAT(im.created_at, '%Y-%m') AS month,
            im.id AS movementId,
            im.description AS description,
            im.quantity AS quantity,
            im.unit_purchase_price AS unitPurchasePrice,
            (im.quantity * im.unit_purchase_price) AS totalCost,
            im.created_at AS createdAt
        FROM inventory_movement im
        WHERE im.type = 'IN'
          AND im.deleted = FALSE
        ORDER BY month DESC, im.created_at DESC
        """,
        nativeQuery = true
    )
    fun getMonthlyAcquisitions(): List<Map<String, Any>>

    // Gráfico de Saída de produtos
    @Query(
        value = """
        SELECT 
            p.name AS product_name,
            im.unit_sale_price AS sale_price,
            SUM(im.quantity) AS total_sold,
            ROUND(SUM(im.quantity * im.unit_sale_price), 2) AS total_revenue,
            DATE_FORMAT(im.created_at, '%Y-%m') AS month_year
        FROM inventory_movement im
        JOIN product p ON p.id = im.product_id
        WHERE im.type = 'OUT'
        GROUP BY 
            p.name, 
            im.unit_sale_price, 
            DATE_FORMAT(im.created_at, '%Y-%m')
        ORDER BY 
            DATE_FORMAT(im.created_at, '%Y-%m') ASC, 
            p.name ASC
    """,
        nativeQuery = true
    )
    fun findSalesOverview(): List<Map<String, Any>>

    // Select geral de dados sobre o tempo de estoque das cartas
    @Query(
        value = """
        SELECT 
            p.id AS product_id,
            p.name AS product_name,
            p.description AS product_description,
            p.price AS current_price,
            p.product_condition AS condition_name,
            i.quantity AS current_quantity,
            p.created_at AS product_created_at,
            COALESCE(MAX(im.created_at), p.created_at) AS last_movement_date,
            DATEDIFF(NOW(), COALESCE(MAX(im.created_at), p.created_at)) AS days_in_stock,
            COUNT(im.id) AS total_movements,
            SUM(CASE WHEN im.type = 'IN' THEN im.quantity ELSE 0 END) AS total_in,
            SUM(CASE WHEN im.type = 'OUT' THEN im.quantity ELSE 0 END) AS total_out,
            (
                SUM(CASE WHEN im.type = 'IN' THEN im.quantity ELSE 0 END) -
                SUM(CASE WHEN im.type = 'OUT' THEN im.quantity ELSE 0 END)
            ) AS balance
        FROM product p
        LEFT JOIN inventory i ON i.product_id = p.id
        LEFT JOIN inventory_movement im ON im.product_id = p.id
        WHERE p.deleted = FALSE
        GROUP BY 
            p.id, p.name, p.description, p.price, p.product_condition, 
            i.quantity, p.created_at
        ORDER BY days_in_stock DESC
    """,
        nativeQuery = true
    )
    fun findStockAgingOverview(): List<Map<String, Any>>

    // Gráfico de variação de preço de venda das cartas
    @Query(
        value = """
        SELECT
            p.id AS product_id,
            p.name AS product_name,
            p.description AS product_description,
            ROUND(AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2) AS avg_sale_price,
            MAX(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END) AS max_sale_price,
            MIN(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END) AS min_sale_price,
            p.price AS current_sale_price,
            ROUND(p.price - AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2) AS difference_from_avg,
            ROUND(((p.price - AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END)) /
                   AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END)) * 100, 2) AS percentage_change,
            COALESCE(i.quantity, 0) AS current_stock,
            MAX(im.created_at) AS last_sale
        FROM product p
        LEFT JOIN inventory i ON i.product_id = p.id
        LEFT JOIN inventory_movement im ON im.product_id = p.id
        WHERE im.type = 'OUT'
        GROUP BY 
            p.id, p.name, p.description, p.price, i.quantity
        HAVING p.price != ROUND(AVG(CASE WHEN im.type = 'OUT' THEN im.unit_sale_price END), 2)
        ORDER BY percentage_change DESC
    """,
        nativeQuery = true
    )
    fun findValuedCards(): List<Map<String, Any>>
}
