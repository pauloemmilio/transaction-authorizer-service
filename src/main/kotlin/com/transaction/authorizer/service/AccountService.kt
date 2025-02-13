package com.transaction.authorizer.service

import com.transaction.authorizer.entity.Account
import com.transaction.authorizer.exception.ResourceNotFoundException
import com.transaction.authorizer.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountService(
    private val accountRepository: AccountRepository
) {

    fun findById(account: String): Account {
        return accountRepository.findById(account)
            .orElseThrow { ResourceNotFoundException("Account with id $account not found") }
    }
}