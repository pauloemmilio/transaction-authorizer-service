package com.transaction.authorizer.service

import com.transaction.authorizer.entity.TransactionCategory
import com.transaction.authorizer.repository.TransactionCategoryRepository
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val transactionCategoryRepository: TransactionCategoryRepository
) {

    fun findTransactionCategoryNameByCode(code: String): String? {
        return transactionCategoryRepository.findByCode(code)?.name
    }

    fun findTransactionCategoryByName(name: String): List<TransactionCategory> {
        return transactionCategoryRepository.findByName(name)
    }

    fun validatesTransactionCategory(transactionCategoryName: String) {
        val transactionCategory = findTransactionCategoryByName(transactionCategoryName)
        if (transactionCategory.isEmpty()) {
            throw IllegalArgumentException("Transaction category not found")
        }
    }
}