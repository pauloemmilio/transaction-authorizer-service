package com.transaction.authorizer.service

import com.transaction.authorizer.entity.TransactionCategory
import com.transaction.authorizer.exception.ResourceNotFoundException
import com.transaction.authorizer.repository.TransactionCategoryRepository
import org.springframework.stereotype.Service

@Service
class TransactionCategoryService(
    private val transactionCategoryRepository: TransactionCategoryRepository
) {

    fun findByCode(code: String): TransactionCategory {
        return transactionCategoryRepository.findById(code)
            .orElseThrow { ResourceNotFoundException("Transaction category (MCC) with code $code not found") }
    }
}