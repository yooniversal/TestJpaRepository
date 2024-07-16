package com.group.mock.food.infrastructure

import com.group.mock.food.domain.Food
import org.springframework.data.jpa.repository.JpaRepository

interface FoodRepository : JpaRepository<Food, Long>, FoodRepositoryCustom {
}