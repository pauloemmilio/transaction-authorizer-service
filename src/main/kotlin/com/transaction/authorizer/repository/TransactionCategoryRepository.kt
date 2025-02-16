package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.TransactionCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface TransactionCategoryRepository : JpaRepository<TransactionCategory, Long> {

    fun findByCode(code: String): TransactionCategory?

    @Query("SELECT * FROM transaction_category WHERE name = :name", nativeQuery = true)
    fun findByName(name: String): List<TransactionCategory>
}