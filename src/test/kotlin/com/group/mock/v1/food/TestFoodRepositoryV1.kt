package com.group.mock.v1.food

import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.v1.common.jpa.TestJpaRepositoryV1

class TestFoodRepositoryV1 : TestJpaRepositoryV1<Food, Long>("id"), FoodRepository {
}