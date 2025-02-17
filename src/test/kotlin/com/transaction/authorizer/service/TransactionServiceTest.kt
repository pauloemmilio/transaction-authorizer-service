package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.entity.Merchant
import com.transaction.authorizer.entity.TransactionHistory
import com.transaction.authorizer.enums.ResponseCodeEnum
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TransactionServiceTest {

    private val transactionCategoryService: TransactionCategoryService = mockk<TransactionCategoryService>()
    private val balanceService: BalanceService = mockk<BalanceService>()
    private val merchantService: MerchantService = mockk<MerchantService>()
    private val transactionHistoryService: TransactionHistoryService = mockk<TransactionHistoryService>()

    private val transactionService = TransactionService(
        transactionCategoryService, balanceService, merchantService, transactionHistoryService
    )

    private val faker = Faker()

    @Test
    fun `should return approved when transaction is valid`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val availableAmount = 100L
        val amount = BigDecimal.valueOf(faker.random.nextLong(availableAmount))
        val mcc = "5411"
        val categoryName = "FOOD"
        val merchant = faker.company.name()

        val account = Account(
            accountId = accountId,
        )

        val balance = Balance(
            account = account,
            transactionCategory = categoryName,
            availableAmount = BigDecimal.valueOf(availableAmount)
        )

        every { merchantService.findByMerchant(merchant) } returns emptyList()
        every { transactionCategoryService.findTransactionCategoryNameByCode(mcc) } returns categoryName
        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
        } returns balance
        every { balanceService.update(any()) } returns balance
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant)

        assertEquals(ResponseCodeEnum.APPROVED, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(merchant)
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
            balanceService.update(any())
            transactionHistoryService.save(any())
        }
    }

    @Test
    fun `should return approved when uses additional balance`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val availableAmount = 100L
        val amount = BigDecimal.valueOf(150)
        val mcc = "5411"
        val categoryName = "FOOD"
        val merchant = faker.company.name()

        val account = Account(
            accountId = accountId,
        )

        val balance = Balance(
            account = account,
            transactionCategory = categoryName,
            availableAmount = BigDecimal.valueOf(availableAmount)
        )

        every { merchantService.findByMerchant(merchant) } returns emptyList()
        every { transactionCategoryService.findTransactionCategoryNameByCode(mcc) } returns categoryName
        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
        } returns balance
        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, "CASH")
        } returns Balance(
            account = account,
            transactionCategory = "CASH",
            availableAmount = BigDecimal.valueOf(50)
        )

        every { balanceService.update(any()) } returns balance
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant)

        assertEquals(ResponseCodeEnum.APPROVED, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(merchant)
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
            transactionHistoryService.save(any())
        }

        verify(exactly = 2) {
            balanceService.update(any())
        }
    }

    @Test
    fun `should return approved when merchant is different from mcc`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val availableAmount = 100L
        val amount = BigDecimal.valueOf(faker.random.nextLong(availableAmount))
        val mcc = "5411"
        val categoryName = "MEAL"
        val merchant = Merchant(
            merchant = faker.company.name(),
            transactionCategory = categoryName
        )

        val account = Account(
            accountId = accountId,
        )

        val balance = Balance(
            account = account,
            transactionCategory = categoryName,
            availableAmount = BigDecimal.valueOf(availableAmount)
        )

        every { merchantService.findByMerchant(merchant.merchant) } returns listOf(
            merchant
        )

        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
        } returns balance
        every { balanceService.update(any()) } returns balance
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant.merchant)

        assertEquals(ResponseCodeEnum.APPROVED, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(merchant.merchant)
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
            balanceService.update(any())
            transactionHistoryService.save(any())
        }
    }

    @Test
    fun `should return approved when category is not found`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val availableAmount = 100L
        val amount = BigDecimal.valueOf(faker.random.nextLong(availableAmount))
        val mcc = "1"
        val categoryName = "CASH"
        val merchant = faker.company.name()

        val account = Account(
            accountId = accountId,
        )

        val balance = Balance(
            account = account,
            transactionCategory = categoryName,
            availableAmount = BigDecimal.valueOf(availableAmount)
        )

        every { merchantService.findByMerchant(merchant) } returns emptyList()
        every { transactionCategoryService.findTransactionCategoryNameByCode(mcc) } returns null
        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
        } returns balance
        every { balanceService.update(any()) } returns balance
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant)

        assertEquals(ResponseCodeEnum.APPROVED, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(merchant)
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
            balanceService.update(any())
            transactionHistoryService.save(any())
        }
    }

    @Test
    fun `should return transaction rejected when balance is insufficient`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val amount = BigDecimal.valueOf(150)
        val mcc = "1"
        val categoryName = "CASH"
        val merchant = faker.company.name()

        val account = Account(
            accountId = accountId,
        )

        val balance = Balance(
            account = account,
            transactionCategory = categoryName,
            availableAmount = BigDecimal.valueOf(100)
        )

        every { merchantService.findByMerchant(merchant) } returns emptyList()
        every { transactionCategoryService.findTransactionCategoryNameByCode(mcc) } returns null
        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
        } returns balance
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant)

        assertEquals(ResponseCodeEnum.REJECTED, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(merchant)
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, categoryName)
            transactionHistoryService.save(any())
        }
    }

    @Test
    fun `should return transaction rejected when has an error`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val amount = BigDecimal.valueOf(150)
        val mcc = "1"
        val merchant = faker.company.name()

        every { merchantService.findByMerchant(any()) } returns emptyList()
        every { transactionCategoryService.findTransactionCategoryNameByCode(any()) } returns null
        every {
            balanceService.findByAccountIdAndTransactionCategory(any(), any())
        } returns null
        every { transactionHistoryService.save(any()) } returns Unit

        val response = transactionService.process(accountId, amount, mcc, merchant)

        assertEquals(ResponseCodeEnum.ERROR, response)

        verify(exactly = 1) {
            merchantService.findByMerchant(any())
            transactionCategoryService.findTransactionCategoryNameByCode(any())
            balanceService.findByAccountIdAndTransactionCategory(any(), any())
        }
    }

    @Test
    fun `should get transaction history with success`() {
        val accountId = faker.random.nextInt().toString()

        every { transactionHistoryService.findByAccountId(accountId) } returns listOf(
            TransactionHistory(
                accountId = accountId,
                amount = BigDecimal.TEN,
                transactionCategory = "FOOD",
                merchant = faker.company.name(),
                responseCode = ResponseCodeEnum.APPROVED.code,
                responseMessage = ResponseCodeEnum.APPROVED.message
            )
        )

        val response = transactionHistoryService.findByAccountId(accountId)

        assertNotNull(response)
        assertEquals(1, response.size)

        verify(exactly = 1) {
            transactionHistoryService.findByAccountId(accountId)
        }
    }

}