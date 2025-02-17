package com.transaction.authorizer.service

import com.transaction.authorizer.entity.TransactionHistory
import com.transaction.authorizer.enums.ResponseCodeEnum
import com.transaction.authorizer.repository.TransactionHistoryRepository
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.math.BigDecimal
import kotlin.test.assertNotNull

class TransactionHistoryServiceTest {

    private val transactionHistoryRepository = mockk<TransactionHistoryRepository>()

    private val transactionHistoryService = TransactionHistoryService(transactionHistoryRepository)

    private val faker = Faker()

    @Test
    fun `should save transaction history with success`() {
        val transaction = getTransactionHistory()

        every { transactionHistoryRepository.save(transaction) } returns transaction

        assertDoesNotThrow {
            transactionHistoryService.save(transaction)
        }

        verify(exactly = 1) { transactionHistoryRepository.save(transaction) }
    }

    @Test
    fun `should find transaction history by account id with success`() {
        val accountId = faker.random.nextInt().toString()
        val transaction = getTransactionHistory()

        every { transactionHistoryRepository.findByAccountId(accountId) } returns listOf(transaction)

        val response = transactionHistoryService.findByAccountId(accountId)

        assertNotNull(response)
        assert(response.size == 1)
        assert(response.first() == transaction)

        verify(exactly = 1) { transactionHistoryRepository.findByAccountId(accountId) }
    }

    private fun getTransactionHistory(): TransactionHistory {
        val responseCodeEnum = faker.random.nextEnum(ResponseCodeEnum.entries.toTypedArray())
        return TransactionHistory(
            accountId = faker.random.nextInt().toString(),
            amount = BigDecimal.TEN,
            transactionCategory = "CASH",
            merchant = faker.company.name(),
            responseCode = responseCodeEnum.code,
            responseMessage = responseCodeEnum.message
        )
    }
}