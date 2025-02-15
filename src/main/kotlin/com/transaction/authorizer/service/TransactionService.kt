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
        return when {
            amount <= balance.availableAmount -> {
                updateBalance(balance, amount)
                ResponseCodeEnum.APPROVED
            }
            balance.transactionCategory == CASH_TRANSACTION_CATEGORY -> ResponseCodeEnum.REJECTED
            else -> processAdditionalBalance(amount, balance)
        }
    }

    private fun processAdditionalBalance(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        val cashBalance = balanceService.findByAccountIdAndTransactionCategory(
            balance.account.accountId, CASH_TRANSACTION_CATEGORY
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
        private const val CASH_TRANSACTION_CATEGORY = "CASH"
    }
}