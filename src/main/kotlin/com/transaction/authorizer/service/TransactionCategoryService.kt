package com.transaction.authorizer.service

import com.transaction.authorizer.repository.TransactionCategoryRepository
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val transactionCategoryRepository: TransactionCategoryRepository
) {

    fun findTransactionCategoryNameByCode(code: String): String {
        val transactionCategory = transactionCategoryRepository.findByCode(code)
        return transactionCategory?.name ?: transactionCategoryRepository.findByIsDefault().name
    }
}