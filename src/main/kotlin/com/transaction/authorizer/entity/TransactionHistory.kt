package com.transaction.authorizer.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_history")
data class TransactionHistory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(name = "account_id", nullable = false)
    val accountId: String,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "transaction_category", nullable = false)
    val transactionCategory: String,

    @Column(name = "merchant", nullable = false)
    val merchant: String,

    @Column(name = "response_code", nullable = false)
    val responseCode: String,

    @Column(name = "response_message", nullable = false)
    val responseMessage: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now()
)
