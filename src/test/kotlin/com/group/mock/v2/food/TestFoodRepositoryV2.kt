package com.group.mock.v2.food

import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.v2.common.FOOD_TEST_DB
import com.group.mock.v2.common.jpa.TestJpaRepositoryV2

class TestFoodRepositoryV2 : TestJpaRepositoryV2<Food, Long>(FOOD_TEST_DB), FoodRepository {
}