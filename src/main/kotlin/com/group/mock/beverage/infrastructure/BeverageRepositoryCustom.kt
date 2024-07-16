package com.group.mock.beverage.infrastructure

import com.group.mock.beverage.domain.Beverage

interface BeverageRepositoryCustom {
    fun getBeverageByPlaceId(placeId: Long): List<Beverage>
    fun getBeverageByFoodId(foodId: Long): List<Beverage>
}