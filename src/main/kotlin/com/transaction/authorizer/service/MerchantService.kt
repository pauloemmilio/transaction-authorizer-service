package com.transaction.authorizer.service

import com.transaction.authorizer.repository.MerchantRepository
import org.springframework.stereotype.Service

@Service
class MerchantService(
    private val merchantRepository: MerchantRepository
) {

    fun findByMerchant(merchant: String) = merchantRepository.findByMerchant(merchant)
}