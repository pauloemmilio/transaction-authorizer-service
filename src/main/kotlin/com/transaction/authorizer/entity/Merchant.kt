package com.transaction.authorizer.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "merchant")
data class Merchant(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val merchant: String,

    @Column(name = "transaction_category_name", nullable = false)
    val transactionCategory: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now()
)