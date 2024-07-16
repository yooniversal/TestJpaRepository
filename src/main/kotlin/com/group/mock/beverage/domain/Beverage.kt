package com.group.mock.beverage.domain

import com.group.mock.food.domain.Food
import com.group.mock.place.domain.Place
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne

@Entity
data class Beverage(
    @Id
    @GeneratedValue
    val id: Long = 0,

    val name: String,

    @ManyToOne
    val food: Food,

    @ManyToOne
    val place: Place,
)