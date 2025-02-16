package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.entity.TransactionHistory
import com.transaction.authorizer.enums.ResponseCodeEnum
import com.transaction.authorizer.exception.ResourceNotFoundException
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionCategoryService: TransactionCategoryService,
    private val balanceService: BalanceService,
    private val merchantService: MerchantService,
    private val transactionHistoryService: TransactionHistoryService
) {

    fun process(accountId: String, amount: BigDecimal, mcc: String, merchant: String): ResponseCodeEnum {
        return try {
            val transactionCategoryName = getTransactionCategoryName(merchant, mcc)
            val balance = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategoryName)
                ?: throw ResourceNotFoundException("Balance not found for account $accountId and transaction category $transactionCategoryName")
            val response = processTransaction(amount, balance)

            createTransactionHistory(accountId, amount, transactionCategoryName, merchant, response)

            return response
        } catch (e: Exception) {
            ResponseCodeEnum.ERROR
        }
    }

    private fun getTransactionCategoryName(merchant: String, mcc: String): String {
        val merchants = merchantService.findByMerchant(merchant)

        if (merchants.isNotEmpty()) {
            return merchants.first().transactionCategory
        }

        val transactionCategoryMcc = transactionCategoryService.findTransactionCategoryNameByCode(mcc)
        if (transactionCategoryMcc != null) {
            return transactionCategoryMcc
        }

        return DEFAULT_TRANSACTION_CATEGORY
    }

    private fun processTransaction(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
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

        if (cashBalance == null) {
            return ResponseCodeEnum.REJECTED
        }

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

    private fun createTransactionHistory(accountId: String, amount: BigDecimal, transactionCategoryName: String, merchant: String, response: ResponseCodeEnum) {
        transactionHistoryService.save(
            TransactionHistory(
                accountId = accountId,
                amount = amount,
                transactionCategory = transactionCategoryName,
                merchant = merchant,
                responseCode = response.code,
                responseMessage = response.message
            )
        )
    }

    fun getHistory(accountId: String): List<TransactionHistory> {
        return transactionHistoryService.findByAccountId(accountId)
    }

    companion object {
        private const val DEFAULT_TRANSACTION_CATEGORY = "CASH"
    }
}