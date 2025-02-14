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

    fun authorizeTransaction(accountId: String, amount: BigDecimal, mcc: String, merchant: String): ResponseCodeEnum {
        return try {
            val transactionCategory = transactionCategoryService.findTransactionCategoryNameByCode(mcc)
            val balance = balanceService.findByAccountIdAndTransactionCategory(accountId, transactionCategory)

            processTransaction(amount, balance)
        } catch (e: Exception) {
            ResponseCodeEnum.ERROR
        }
    }

    private fun processTransaction(amount: BigDecimal, balance: Balance): ResponseCodeEnum {
        if (amount > balance.availableAmount) return ResponseCodeEnum.REJECTED

        balanceService.update(balance.copy(availableAmount = balance.availableAmount - amount))
        return ResponseCodeEnum.APPROVED
    }
}