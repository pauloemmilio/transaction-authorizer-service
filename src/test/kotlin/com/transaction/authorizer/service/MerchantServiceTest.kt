package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Merchant
import com.transaction.authorizer.repository.MerchantRepository
import io.github.serpro69.kfaker.Faker
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MerchantServiceTest {

    private val merchantRepository: MerchantRepository = mockk<MerchantRepository>()

    private val merchantService = MerchantService(merchantRepository)

    private val faker = Faker()

    @Test
    fun `should find merchant by merchant name with success`() {
        val merchant = faker.company.name()
        val transactionCategory = "CASH"

        val merchantEntity = Merchant(
            merchant = merchant,
            transactionCategory = transactionCategory
        )

        every { merchantRepository.findByMerchant(merchant) } returns listOf(merchantEntity)

        val response = merchantService.findByMerchant(merchant)

        assertEquals(1, response.size)
        assert(response.first() == merchantEntity)

        verify(exactly = 1) { merchantRepository.findByMerchant(merchant) }
    }
}