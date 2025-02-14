package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.entity.TransactionCategory
import com.transaction.authorizer.enums.ResponseCodeEnum
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertEquals

class TransactionServiceTest {

    private val balanceService: BalanceService = mockk<BalanceService>()
    private val transactionCategoryService: TransactionCategoryService = mockk<TransactionCategoryService>()

    private val transactionService = TransactionService(
        transactionCategoryService, balanceService
    )

    private val faker = Faker()

    @Test
    fun `should authorize valid transaction with success`() {
        val accountId = faker.random.nextInt(1, 10).toString()
        val amount = BigDecimal.valueOf(faker.random.nextLong(10))
        val mcc = faker.random.randomValue(listOf("5411", "5412", "5811", "5812"))
        val categoryName = faker.random.randomValue(listOf("FOOD", "MEAL"))
        val merchant = faker.company.name()

        val account = Account(
            accountId, LocalDateTime.now(), LocalDateTime.now()
        )

        val transactionCategory = TransactionCategory(
            mcc, categoryName, LocalDateTime.now(), LocalDateTime.now()
        )

        val balance = Balance(
            1, account, transactionCategory.name, BigDecimal.valueOf(100), LocalDateTime.now(), LocalDateTime.now()
        )

        every {
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
        } returns transactionCategory.name

        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory.name)
        } returns balance

        every { balanceService.update(any()) } returns balance

        val response = transactionService.processTransaction(
            accountId, amount, mcc, merchant
        )

        assertEquals(ResponseCodeEnum.APPROVED.code, response.code)
        assertEquals(ResponseCodeEnum.APPROVED.message, response.message)

        verify(exactly = 1) {
            transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory.name)
            balanceService.update(any())
        }
    }

}