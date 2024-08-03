package com.group.mock.v1.beverage

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.infrastructure.BeverageRepository
import com.group.mock.v1.infrastructure.jpa.TestJpaRepositoryV1

class TestBeverageRepositoryV1 : TestJpaRepositoryV1<Beverage, Long>("id"), BeverageRepository {
    override fun getBeverageByPlaceId(placeId: Long): List<Beverage> {
        return entityList.filter { it.place.id == placeId }
    }

    override fun getBeverageByFoodId(foodId: Long): List<Beverage> {
        return entityList.filter { it.food.id == foodId }
    }
}