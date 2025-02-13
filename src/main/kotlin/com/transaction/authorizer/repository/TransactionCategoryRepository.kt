package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.TransactionCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TransactionCategoryRepository : JpaRepository<TransactionCategory, String>