package com.rtr.store_manager_api.repository

import com.rtr.store_manager_api.domain.entity.Inventory
import com.rtr.store_manager_api.dto.dashdto.MonthlyAcquisitionProjection
import com.rtr.store_manager_api.dto.dashdto.ProfitByCategoryProjection
import com.rtr.store_manager_api.dto.dashdto.SpendEarnByMonthProjection
import com.rtr.store_manager_api.dto.dashdto.StockValuationProjection
import com.rtr.store_manager_api.dto.dashdto.TopPokemonByStockProjection
import com.rtr.store_manager_api.dto.dashdto.TopSellingCardProjection
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.time.LocalDateTime

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
    fun getTopPokemonByStock(): TopPokemonByStockProjection?


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
        DATE_FORMAT(im.created_at, '%Y-%m')              AS month,
        im.id                                           AS movementId,
        im.description                                  AS description,
        im.quantity                                     AS quantity,
        im.unit_purchase_price                          AS unitPurchasePrice,
        (im.quantity * im.unit_purchase_price)          AS totalCost,
        im.created_at                                   AS createdAt
    FROM inventory_movement im
    WHERE im.type = 'IN'
      AND im.deleted = FALSE
    ORDER BY month DESC, im.created_at DESC
    """,
        nativeQuery = true
    )
    fun getMonthlyAcquisitions(): List<MonthlyAcquisitionProjection>



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

    @Query(
        """
    SELECT 
        category, 
        SUM(total_quantity) AS total_quantity
    FROM (
        SELECT
            CASE
                WHEN p.card_id IS NOT NULL THEN 'CARTAS_AVULSAS'
                WHEN op.type = 'BOOSTER_BOX' THEN 'BOOSTER_BOX'
                WHEN op.type = 'OTHER' THEN 'BOOSTER'
                WHEN op.type = 'ACCESSORY' THEN 'ACCESSORY'
                ELSE 'OUTROS'
            END AS category,

            (
                COALESCE((
                    SELECT SUM(im.quantity)
                    FROM inventory_movement im
                    WHERE im.product_id = p.id
                      AND im.type = 'IN'
                      AND im.created_at <= :dataRef
                      AND im.deleted = 0
                ), 0)
                -
                COALESCE((
                    SELECT SUM(im.quantity)
                    FROM inventory_movement im
                    WHERE im.product_id = p.id
                      AND im.type = 'OUT'
                      AND im.created_at <= :dataRef
                      AND im.deleted = 0
                ), 0)
                +
                COALESCE((
                    SELECT SUM(im.quantity)
                    FROM inventory_movement im
                    WHERE im.product_id = p.id
                      AND im.type = 'ADJUST'
                      AND im.created_at <= :dataRef
                      AND im.deleted = 0
                ), 0)
            ) AS total_quantity

        FROM product p
        LEFT JOIN other_product op ON op.id = p.other_product_id
        WHERE p.deleted = 0
    ) AS sub
    GROUP BY category
    ORDER BY category
    """,
        nativeQuery = true
    )
    fun getHistoricalInventoryDistribution(
        @Param("dataRef") dataRef: LocalDateTime
    ): List<Map<String, Any>>

    @Query(
        """
    SELECT 
        p.name AS productName,
        SUM(im.quantity) AS totalSold
    FROM inventory_movement im
    JOIN product p ON p.id = im.product_id
    WHERE im.type = 'OUT'
      AND im.deleted = 0
      AND im.created_at BETWEEN :startDate AND :endDate
    GROUP BY p.name
    ORDER BY totalSold DESC
    """,
        nativeQuery = true
    )
    fun findAllCardSales(
        @Param("startDate") startDate: LocalDateTime,
        @Param("endDate") endDate: LocalDateTime
    ): List<TopSellingCardProjection>


    @Query(
        value = """
        SELECT
            CASE
                WHEN p.card_id IS NOT NULL THEN 'CARTAS_AVULSAS'
                WHEN op.type = 'BOOSTER_BOX' THEN 'BOOSTER_BOX'
                WHEN op.type = 'OTHER' THEN 'BOOSTER'
                WHEN op.type = 'ACCESSORY' THEN 'ACCESSORY'
                ELSE 'OUTROS'
            END AS category,

            SUM(
                (im.unit_sale_price - im.unit_purchase_price) * im.quantity
            ) AS totalProfit

        FROM inventory_movement im
        JOIN product p ON p.id = im.product_id
        LEFT JOIN other_product op ON op.id = p.other_product_id

        WHERE im.type = 'OUT'
          AND im.deleted = 0
          AND im.created_at BETWEEN :startDate AND :endDate

        GROUP BY category
        ORDER BY totalProfit DESC
    """,
        nativeQuery = true
    )
    fun getProfitByCategory(
        startDate: String,
        endDate: String
    ): List<ProfitByCategoryProjection>


    @Query(
        value = """
        SELECT
            DATE_FORMAT(im.created_at, '%Y-%m') AS month,

            SUM(
                CASE WHEN im.type = 'IN'
                     THEN im.unit_purchase_price * im.quantity
                     ELSE 0 END
            ) AS totalSpent,

            SUM(
                CASE WHEN im.type = 'OUT'
                     THEN im.unit_sale_price * im.quantity
                     ELSE 0 END
            ) AS totalEarned

        FROM inventory_movement im
        WHERE im.deleted = 0
          AND im.created_at BETWEEN :startDate AND :endDate
        GROUP BY month
        ORDER BY month ASC
    """,
        nativeQuery = true
    )
    fun getSpendVsEarnByMonth(
        startDate: String,
        endDate: String
    ): List<SpendEarnByMonthProjection>

    @Query(
        value = """
        SELECT 
            DATE_FORMAT(mes.ref_date, '%Y-%m') AS month,
            SUM(

                (
                    (
                        SELECT 
                            COALESCE(SUM(CASE WHEN im2.type = 'IN' THEN im2.quantity END), 0) -
                            COALESCE(SUM(CASE WHEN im2.type = 'OUT' THEN im2.quantity END), 0) +
                            COALESCE(SUM(CASE WHEN im2.type = 'ADJUST' THEN im2.quantity END), 0)
                        FROM inventory_movement im2
                        WHERE im2.product_id = p.id
                          AND im2.created_at <= LAST_DAY(mes.ref_date)
                          AND im2.deleted = 0
                    )
                )
                *
                (
                    SELECT im3.unit_purchase_price
                    FROM inventory_movement im3
                    WHERE im3.product_id = p.id
                      AND im3.type = 'IN'
                      AND im3.created_at <= LAST_DAY(mes.ref_date)
                    ORDER BY im3.created_at DESC
                    LIMIT 1
                )

            ) AS totalStockValue

        FROM product p

        JOIN (
            SELECT 
                DATE_FORMAT(d1.date, '%Y-%m-01') AS ref_date
            FROM (
                SELECT 
                    (DATE(:startDate) + INTERVAL seq MONTH) AS date
                FROM (
                    SELECT 0 seq UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 
                    UNION SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 
                    UNION SELECT 9 UNION SELECT 10 UNION SELECT 11
                ) AS months
            ) d1
            WHERE d1.date BETWEEN :startDate AND :endDate
        ) AS mes

        WHERE p.deleted = 0
        GROUP BY month
        ORDER BY month ASC
    """,
        nativeQuery = true
    )
    fun getStockValuationByMonth(
        startDate: String,
        endDate: String
    ): List<StockValuationProjection>

}
