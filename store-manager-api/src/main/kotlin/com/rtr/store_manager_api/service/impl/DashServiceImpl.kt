package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.dto.dashdto.*
import com.rtr.store_manager_api.repository.DashRepository
import com.rtr.store_manager_api.service.DashService
import org.springframework.stereotype.Service
import java.sql.Timestamp
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Service
class DashServiceImpl(
    private val dashRepository: DashRepository,
) : DashService {

    // === KPIs ===

    override fun getTotalCardsInStock(): TotalCardsInStockDTO {
        val total = dashRepository.getTotalCardsInStock()
        return TotalCardsInStockDTO(totalCardsInStock = total)
    }

    override fun getTotalBoosterBoxes(): TotalBoosterBoxesDTO {
        val total = dashRepository.getTotalBoosterBoxes()
        return TotalBoosterBoxesDTO(totalBoosterBoxes = total)
    }

    override fun getTopPokemonByStock(): TopPokemonByStockDTO {
        val result = dashRepository.getTopPokemonByStock()

        return if (result == null) {
            TopPokemonByStockDTO(
                pokemonName = "Desconhecido",
                totalQuantity = 0L
            )
        } else {
            TopPokemonByStockDTO(
                pokemonName = result.pokemonName,
                totalQuantity = result.totalQuantity
            )
        }
    }



    override fun getTopCollectionByItems(): TopCollectionByItemsDTO {
        val result = dashRepository.getTopCollectionByItems()
        val name = result["collection_name"]?.toString() ?: "Desconhecida"
        val total = (result["total_items"] as? Number)?.toLong() ?: 0L
        return TopCollectionByItemsDTO(collectionName = name, totalItems = total)
    }

    // === GRÁFICOS ===

    override fun getMonthlyAcquisitions(): List<MonthlyAcquisitionDTO> {
        val rows = dashRepository.getMonthlyAcquisitions()

        return rows.map { r ->
            MonthlyAcquisitionDTO(
                month = r.getMonth(),
                movementId = r.getMovementId(),
                description = r.getDescription(),
                quantity = r.getQuantity(),
                unitPurchasePrice = r.getUnitPurchasePrice().toDouble(),
                totalCost = r.getTotalCost().toDouble(),
                createdAt = r.getCreatedAt()
            )
        }
    }


    override fun getSalesOverview(): List<SalesOverviewDTO> {
        return dashRepository.findSalesOverview().map { row ->
            SalesOverviewDTO(
                productName = row["product_name"].toString(),
                salePrice = (row["sale_price"] as? Number)?.toDouble() ?: 0.0,
                totalSold = (row["total_sold"] as? Number)?.toLong() ?: 0L,
                totalRevenue = (row["total_revenue"] as? Number)?.toDouble() ?: 0.0,
                monthYear = row["month_year"]?.toString() ?: "N/A"
            )
        }
    }

    override fun getStockAgingOverview(): List<StockAgingOverviewDTO> {
        return dashRepository.findStockAgingOverview().map { row ->
            StockAgingOverviewDTO(
                productId = row["product_id"].toString(),
                productName = row["product_name"].toString(),
                productDescription = row["product_description"]?.toString(),
                currentPrice = (row["current_price"] as? Number)?.toDouble() ?: 0.0,
                conditionName = row["condition_name"].toString(),
                currentQuantity = (row["current_quantity"] as? Number)?.toInt() ?: 0,
                productCreatedAt = (row["product_created_at"] as? Timestamp)?.toLocalDateTime() ?: LocalDateTime.now(),
                lastMovementDate = (row["last_movement_date"] as? Timestamp)?.toLocalDateTime() ?: LocalDateTime.now(),
                daysInStock = (row["days_in_stock"] as? Number)?.toLong() ?: 0L,
                totalMovements = (row["total_movements"] as? Number)?.toLong() ?: 0L,
                totalIn = (row["total_in"] as? Number)?.toLong() ?: 0L,
                totalOut = (row["total_out"] as? Number)?.toLong() ?: 0L,
                balance = (row["balance"] as? Number)?.toLong() ?: 0L
            )
        }
    }

    override fun getValuedCards(): List<ValuedCardDTO> {
        return dashRepository.findValuedCards().map { row ->
            ValuedCardDTO(
                productId = row["product_id"].toString(),
                productName = row["product_name"].toString(),
                productDescription = row["product_description"]?.toString(),
                avgSalePrice = (row["avg_sale_price"] as? Number)?.toDouble() ?: 0.0,
                maxSalePrice = (row["max_sale_price"] as? Number)?.toDouble() ?: 0.0,
                minSalePrice = (row["min_sale_price"] as? Number)?.toDouble() ?: 0.0,
                currentSalePrice = (row["current_sale_price"] as? Number)?.toDouble() ?: 0.0,
                differenceFromAvg = (row["difference_from_avg"] as? Number)?.toDouble() ?: 0.0,
                percentageChange = (row["percentage_change"] as? Number)?.toDouble() ?: 0.0,
                currentStock = (row["current_stock"] as? Number)?.toInt() ?: 0,
                lastSale = (row["last_sale"] as? Timestamp)?.toLocalDateTime() ?: LocalDateTime.now()
            )
        }
    }
    override fun getHistoricalDistribution(date: LocalDateTime): List<InventoryDistributionDTO> =
        dashRepository.getHistoricalInventoryDistribution(date).map {
            InventoryDistributionDTO(
                category = it["category"] as String,
                totalQuantity = (it["total_quantity"] as Number).toLong()
            )
        }

    override fun getCardSales(start: LocalDate, end: LocalDate): List<TopSellingCardDTO> {
        val startDateTime = start.atStartOfDay()
        val endDateTime = end.atTime(23, 59, 59)
        return dashRepository.findAllCardSales(startDateTime, endDateTime).map {
            TopSellingCardDTO(it.getProductName(), it.getTotalSold())
        }
    }

    override fun getProfitByCategory(start: LocalDate, end: LocalDate): List<ProfitByCategoryProjection> {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dashRepository.getProfitByCategory(
            start.atStartOfDay().format(formatter),
            end.atTime(23, 59, 59).format(formatter)
        )
    }

    override fun getSpendVsEarn(start: LocalDate, end: LocalDate): List<SpendEarnByMonthProjection> {
        val f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dashRepository.getSpendVsEarnByMonth(
            start.atStartOfDay().format(f),
            end.atTime(23, 59, 59).format(f)
        )
    }

    override fun getStockValuation(start: LocalDate, end: LocalDate): List<StockValuationProjection> {
        val f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        return dashRepository.getStockValuationByMonth(
            start.atStartOfDay().format(f),
            end.atTime(23, 59, 59).format(f)
        )
    }
}
