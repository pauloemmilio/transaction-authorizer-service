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

    private val accountService: AccountService = mockk<AccountService>()
    private val balanceService: BalanceService = mockk<BalanceService>()
    private val transactionCategoryService: TransactionCategoryService = mockk<TransactionCategoryService>()

    private val transactionService = TransactionService(
        accountService, transactionCategoryService, balanceService
    )

    private val faker = Faker()

    @Test
    fun `should authorize transaction with success and respond with approved`() {

        val accountId = faker.random.nextInt(1, 10).toString()
        val amount = BigDecimal.valueOf(faker.random.nextLong(10))
        val mcc = "5811"
        val merchant = faker.company.name()

        val account = Account(
            accountId, LocalDateTime.now(), LocalDateTime.now()
        )

        val transactionCategory = TransactionCategory(
            mcc, "MEAL", LocalDateTime.now(), LocalDateTime.now()
        )

        val balance = Balance(
            1, account, transactionCategory.name, BigDecimal.valueOf(100), LocalDateTime.now(), LocalDateTime.now()
        )

        every {
            accountService.findById(accountId)
        } returns account

        every {
            transactionCategoryService.findByCode(mcc)
        } returns transactionCategory

        every {
            balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory.name)
        } returns balance

        every { balanceService.update(any()) } returns balance

        val response = transactionService.authorizeTransaction(
            accountId, amount, mcc, merchant
        )

        assertEquals(ResponseCodeEnum.APPROVED.code, response.code)
        assertEquals(ResponseCodeEnum.APPROVED.message, response.message)

        verify(exactly = 1) {
            accountService.findById(accountId)
            transactionCategoryService.findByCode(mcc)
            balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory.name)
            balanceService.update(any())
        }
    }

}