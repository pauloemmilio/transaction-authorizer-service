package com.transaction.authorizer.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "transaction_category")
data class TransactionCategory(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = true, unique = true)
    val code: String,

    @Column(nullable = false)
    val name: String,

    @Column(name = "is_default", nullable = false)
    val isDefault: Boolean,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime,

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime
)
