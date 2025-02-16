package com.transaction.authorizer.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "account")
data class Account(
    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    val accountId: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime? = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime? = LocalDateTime.now()
)
