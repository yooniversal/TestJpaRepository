package com.group.mock.food

import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.infrastructure.jpa.TestJpaRepository

object TestFoodRepository : TestJpaRepository<Food, Long>("id"), FoodRepository {
}