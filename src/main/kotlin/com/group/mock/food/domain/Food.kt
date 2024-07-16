package com.group.mock.food.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Food(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val name: String,
)