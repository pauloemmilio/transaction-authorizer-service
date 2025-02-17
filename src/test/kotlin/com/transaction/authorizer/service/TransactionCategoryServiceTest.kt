package com.transaction.authorizer.service

import com.transaction.authorizer.entity.TransactionCategory
import com.transaction.authorizer.repository.TransactionCategoryRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertNotNull

class TransactionCategoryServiceTest {

    private val transactionCategoryRepository = mockk<TransactionCategoryRepository>()

    private val transactionCategoryService = TransactionCategoryService(transactionCategoryRepository)

    @Test
    fun `should find transaction category by code with success`() {
        val code = "1"
        val transactionCategoryName = "CASH"

        every { transactionCategoryRepository.findByCode(code) } returns TransactionCategory(
            code = code,
            name = transactionCategoryName,
            isDefault = true
        )

        val response = transactionCategoryService.findTransactionCategoryNameByCode(code)

        assertEquals(transactionCategoryName, response)

        verify(exactly = 1) { transactionCategoryRepository.findByCode(code) }
    }

    @Test
    fun `should find transaction category by name with success`() {
        val name = "CASH"
        val transactionCategory = TransactionCategory(
            code = "1",
            name = name,
            isDefault = true
        )

        every { transactionCategoryRepository.findByName(name) } returns listOf(transactionCategory)

        val response = transactionCategoryService.findTransactionCategoryByName(name)

        assertNotNull(response)
        assertEquals(1, response.size)
        assertEquals(transactionCategory, response.first())

        verify(exactly = 1) { transactionCategoryRepository.findByName(name) }
    }

    @Test
    fun `should throw exception when transaction category is invalid`() {
        val name = "CASH"

        every { transactionCategoryRepository.findByName(name) } returns listOf()

        assertThrows<Exception> { transactionCategoryService.validatesTransactionCategory(name) }

        verify(exactly = 1) { transactionCategoryRepository.findByName(name) }
    }
}