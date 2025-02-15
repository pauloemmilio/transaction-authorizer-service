package com.transaction.authorizer.repository

import com.transaction.authorizer.entity.Merchant
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface MerchantRepository: JpaRepository<Merchant, Long> {
    fun findByMerchant(merchant: String): List<Merchant>
}