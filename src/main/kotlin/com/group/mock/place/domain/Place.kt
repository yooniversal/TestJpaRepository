package com.group.mock.place.domain

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.Id

@Entity
data class Place(
    @Id
    @GeneratedValue
    val id: Long = 0,

    val name: String,
)