package com.group.mock.beverage.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
class Beverage(
    @Id
    @GeneratedValue
    val id: Long = 0,
    val name: String,
    val foodId: Long,
    val placeId: Long,
)