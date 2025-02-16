package com.transaction.authorizer.service

import com.transaction.authorizer.dto.request.AccountBalanceRequestDTO
import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val balanceService: BalanceService
) {

    fun createAccount(accountId: String, balance: List<AccountBalanceRequestDTO>) {
        val account = Account(accountId)
        accountRepository.save(account)
        balance.forEach {
            balanceService.createBalance(account, it.transactionCategory, it.amount)
        }
    }
}