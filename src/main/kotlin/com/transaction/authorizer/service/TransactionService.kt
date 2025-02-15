package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.enums.ResponseCodeEnum
import com.transaction.authorizer.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionCategoryService: TransactionCategoryService,
    private val balanceService: BalanceService,
    val merchantService: MerchantService
) {

    fun processTransaction(accountId: String, amount: BigDecimal, mcc: String, merchant: String): ResponseCodeEnum {
        return try {
            val transactionCategoryName = getTransactionCategoryName(merchant, mcc)
            val balance = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategoryName)
            processBalance(amount, balance)
        } catch (e: Exception) {
            ResponseCodeEnum.ERROR
        }
    }

    private fun getTransactionCategoryName(merchant: String, mcc: String): String {
        val merchants = merchantService.findByMerchant(merchant)

        if (merchants.isEmpty()) {
            throw ResourceNotFoundException("Merchant $merchant not found")
        }

        if (merchants.size == 1) {
            return merchants.first().transactionCategory
        }

        val transactionCategoryMcc = transactionCategoryService.findTransactionCategoryNameByCode(mcc)
        return merchants.firstOrNull { it.transactionCategory == transactionCategoryMcc }?.transactionCategory
            ?: merchants.firstOrNull()?.transactionCategory
            ?: DEFAULT_TRANSACTION_CATEGORY
    }

    private fun processBalance(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        return when {
            amount <= balance.availableAmount -> {
                updateBalance(balance, amount)
                ResponseCodeEnum.APPROVED
            }
            balance.transactionCategory == DEFAULT_TRANSACTION_CATEGORY -> ResponseCodeEnum.REJECTED
            else -> processAdditionalBalance(amount, balance)
        }
    }

    private fun processAdditionalBalance(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        val cashBalance = balanceService.findByAccountIdAndTransactionCategory(
            balance.account.accountId, DEFAULT_TRANSACTION_CATEGORY
        )
        val totalAvailableAmount = balance.availableAmount + cashBalance.availableAmount

        return if (amount <= totalAvailableAmount) {
            val remainingAmount = amount - balance.availableAmount
            updateBalance(balance, balance.availableAmount)
            updateBalance(cashBalance, remainingAmount)
            ResponseCodeEnum.APPROVED
        } else {
            ResponseCodeEnum.REJECTED
        }
    }

    private fun updateBalance(balance: Balance, deduction: BigDecimal) = balanceService.update(
        balance.copy(availableAmount = balance.availableAmount - deduction)
    )

    companion object {
        private const val DEFAULT_TRANSACTION_CATEGORY = "CASH"
    }
}