package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Balance
import com.transaction.authorizer.enums.ResponseCodeEnum
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class TransactionService(
    private val transactionCategoryService: TransactionCategoryService,
    private val balanceService: BalanceService
) {

    fun processTransaction(accountId: String, amount: BigDecimal, mcc: String, merchant: String): ResponseCodeEnum {
        return try {
            val transactionCategory = transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            val balance = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory)
            processBalance(amount, balance)
        } catch (e: Exception) {
            ResponseCodeEnum.ERROR
        }
    }

    private fun processBalance(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        if (amount <= balance.availableAmount) {
            balanceService.update(balance.copy(availableAmount = balance.availableAmount - amount))
            return ResponseCodeEnum.APPROVED
        } else {
            if(balance.transactionCategory == CASH_TRANSACTION_CATEGORY) {
                return ResponseCodeEnum.REJECTED
            }

            val cashBalance = balanceService.findByAccountIdAndTransactionCategory(balance.account.accountId, CASH_TRANSACTION_CATEGORY)
            val totalAmount = balance.availableAmount + cashBalance.availableAmount

            if (amount <= totalAmount) {
                val remainingBalance = amount - balance.availableAmount
                balanceService.update(balance.copy(availableAmount = BigDecimal.ZERO))
                balanceService.update(cashBalance.copy(availableAmount = cashBalance.availableAmount - remainingBalance))
                return ResponseCodeEnum.APPROVED
            }
            else {
                return ResponseCodeEnum.REJECTED
            }
        }
    }

    companion object {
        private const val CASH_TRANSACTION_CATEGORY = "CASH"
    }
}