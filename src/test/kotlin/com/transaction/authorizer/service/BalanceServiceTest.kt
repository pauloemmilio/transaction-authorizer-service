package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.repository.BalanceRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class BalanceServiceTest {

    private val balanceRepository: BalanceRepository = mockk<BalanceRepository>()
    private val transactionCategoryService: TransactionCategoryService = mockk<TransactionCategoryService>()

    private val balanceService = BalanceService(balanceRepository, transactionCategoryService)

    @Test
    fun `should update balance with success`() {
        val balance = Balance(
            account = Account(accountId = "123"),
            transactionCategory = "CASH",
            availableAmount = BigDecimal(100)
        )

        every { balanceRepository.save(balance) } returns balance

        val response = balanceService.update(balance)

        assert(response == balance)

        verify(exactly = 1) { balanceRepository.save(balance) }
    }

    @Test
    fun `should find balance by account and transaction category with success`() {
        val accountId = "123"
        val transactionCategory = "CASH"

        val balance = Balance(
            account = Account(accountId = accountId),
            transactionCategory = transactionCategory,
            availableAmount = BigDecimal(100)
        )

        every { balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory) } returns balance

        val response = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory)

        assert(response == balance)

        verify(exactly = 1) { balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory) }
    }

    @Test
    fun `should create balance with success`() {
        val account = Account(accountId = "123")
        val transactionCategory = "CASH"
        val amount = BigDecimal(100)

        val balance = Balance(
            account = account,
            transactionCategory = transactionCategory,
            availableAmount = amount
        )

        every { balanceRepository.save(any()) } returns balance

        balanceService.createBalance(account, transactionCategory, amount)

        verify(exactly = 1) { balanceRepository.save(any()) }
    }

    @Test
    fun `should add balance with success when balance already exists`() {
        val accountId = "123"
        val transactionCategory = "CASH"
        val amount = BigDecimal(100)

        val balance = Balance(
            account = Account(accountId = accountId),
            transactionCategory = transactionCategory,
            availableAmount = BigDecimal(100)
        )

        every { transactionCategoryService.validatesTransactionCategory(transactionCategory) } returns Unit
        every { balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory) } returns balance
        every { balanceRepository.save(balance) } returns balance

        balanceService.addBalance(accountId, amount, transactionCategory)

        verify(exactly = 1) {
            transactionCategoryService.validatesTransactionCategory(transactionCategory)
            balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory)
            balanceRepository.save(balance)
        }
    }

    @Test
    fun `should add balance with success when balance does not exist`() {
        val accountId = "123"
        val transactionCategory = "CASH"
        val amount = BigDecimal(100)

        every { transactionCategoryService.validatesTransactionCategory(transactionCategory) } returns Unit
        every { balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory) } returns null
        every { balanceRepository.save(any()) } returns Balance(
            account = Account(accountId = accountId),
            transactionCategory = transactionCategory,
            availableAmount = amount
        )

        balanceService.addBalance(accountId, amount, transactionCategory)

        verify(exactly = 1) {
            transactionCategoryService.validatesTransactionCategory(transactionCategory)
            balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory)
            balanceRepository.save(any())
        }
    }

    @Test
    fun `should not add balance when transaction category is invalid`() {
        val accountId = "123"
        val transactionCategory = "INVALID"
        val amount = BigDecimal(100)

        every { transactionCategoryService.validatesTransactionCategory(transactionCategory) } throws Exception()

        assertThrows<Exception> {
            balanceService.addBalance(accountId, amount, transactionCategory)
        }

        verify(exactly = 1) { transactionCategoryService.validatesTransactionCategory(transactionCategory) }
        verify(exactly = 0) {
            balanceRepository.findByAccount_AccountIdAndTransactionCategory(accountId, transactionCategory)
            balanceRepository.save(any())
        }
    }
}