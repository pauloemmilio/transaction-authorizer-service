package com.transaction.authorizer.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(
    name = "balance",
    uniqueConstraints = [UniqueConstraint(columnNames = ["account_id", "transaction_category_code"])]
)
data class Balance(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    val account: Account,

    @ManyToOne
    @JoinColumn(name = "transaction_category_code", nullable = false)
    val transactionCategory: TransactionCategory,

    @Column(name = "available_amount", nullable = false)
    var availableAmount: BigDecimal,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
)
