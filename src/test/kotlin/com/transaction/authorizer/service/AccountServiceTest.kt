package com.transaction.authorizer.service

import com.transaction.authorizer.dto.request.AccountBalanceRequestDTO
import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.repository.AccountRepository
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class AccountServiceTest {

    private val accountRepository: AccountRepository = mockk<AccountRepository>()
    private val balanceService: BalanceService = mockk<BalanceService>()

    private val accountService = AccountService(accountRepository, balanceService)

    private val faker = Faker()

    @Test
    fun `should create account with success`() {
        val accountId = faker.random.nextInt().toString()

        val amount = BigDecimal(100)
        val balance = listOf(
            AccountBalanceRequestDTO(
                transactionCategory = "CASH",
                amount = amount
            ),
            AccountBalanceRequestDTO(
                transactionCategory = "FOOD",
                amount = amount
            )
        )

        every { accountRepository.save(any()) } returns Account(accountId)
        every { balanceService.createBalance(any(), any(), amount) } returns Unit

        assertDoesNotThrow {
            accountService.createAccount(accountId, balance)
        }

        verify(exactly = 1) {
            accountRepository.save(any())
        }

        verify(exactly = 2) {
            balanceService.createBalance(any(), any(), amount)
        }
    }
}