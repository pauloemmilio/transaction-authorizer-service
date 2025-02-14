package com.transaction.authorizer.service

import com.transaction.authorizer.repository.TransactionCategoryRepository
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val transactionCategoryRepository: TransactionCategoryRepository
) {

    fun findTransactionCategoryNameByCode(code: String): String {
        val transactionCategory = transactionCategoryRepository.findById(code)
        return if (transactionCategory.isPresent) {
            transactionCategory.get().name
        } else {
            DEFAULT_TRANSACTION_CATEGORY_NAME
        }
    }

    companion object {
        private const val DEFAULT_TRANSACTION_CATEGORY_NAME = "CASH"
    }
}