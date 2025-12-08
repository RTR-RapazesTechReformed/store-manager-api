package com.rtr.store_manager_api.service.impl

import com.rtr.store_manager_api.dto.dashdto.*
import com.rtr.store_manager_api.repository.DashRepository
import com.rtr.store_manager_api.service.DashService
import org.springframework.stereotype.Service
import java.math.BigDecimal
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



    override fun getTopCollectionKpi(): TopCollectionKpiDTO {
        val rows = dashRepository.getTopCollectionKpi()

        if (rows.isEmpty()) {
            return TopCollectionKpiDTO(
                colecao = "Desconhecida",
                vendidasUltimoMes = 0,
                estoqueAtual = 0
            )
        }

        val r = rows[0]

        return TopCollectionKpiDTO(
            colecao = r[0]?.toString() ?: "Desconhecida",
            vendidasUltimoMes = (r[1] as? Number)?.toLong() ?: 0L,
            estoqueAtual = (r[2] as? Number)?.toLong() ?: 0L
        )
    }





    // === GRÁFICOS ===

    override fun getMonthlyInvestments(): List<MonthlyInvestmentDTO> {
        val rows = dashRepository.findMonthlyInvestments()

        return rows.map { row ->
            MonthlyInvestmentDTO(
                month = row[0] as String,
                productId = row[1] as String,
                productName = row[2] as String,
                totalQuantity = (row[3] as Number).toInt(),
                unitPrice = row[4] as BigDecimal,
                totalInvested = row[5] as BigDecimal
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

    override fun getCartasKpi(): CartasKpiDTO {
        return CartasKpiDTO(
            total = dashRepository.totalCartas(),
            vendidasHoje = dashRepository.cartasVendidasHoje(),
            cadastradasHoje = dashRepository.cartasCadastradasHoje(),
        )
    }

    override fun getBoosterBoxesKpi(): BoosterBoxesKpiDTO {
        val total = dashRepository.totalBoosterBoxesSistema()
        val vendidasHoje = dashRepository.boosterBoxesVendidasHoje()
        val cadastradasHoje = dashRepository.boosterBoxesCadastradasHoje()

        return BoosterBoxesKpiDTO(
            total = total,
            vendidasHoje = vendidasHoje,
            cadastradasHoje = cadastradasHoje
        )
    }

    override fun getTopCardKpi(): TopCardKpiDTO {
        val result = dashRepository.getTopCardKpi()

        return TopCardKpiDTO(
            nomeCarta = result["nomeCarta"]?.toString() ?: "---",
            quantidadeAtual = (result["quantidadeAtual"] as Number?)?.toLong() ?: 0,
            vendasUltimoMes = (result["vendasUltimoMes"] as Number?)?.toLong() ?: 0
        )
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
