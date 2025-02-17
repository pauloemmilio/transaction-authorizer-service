package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.repository.BalanceRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
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
}