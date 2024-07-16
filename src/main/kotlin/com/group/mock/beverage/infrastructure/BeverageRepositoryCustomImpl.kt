package com.group.mock.beverage.infrastructure

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.domain.QBeverage.beverage
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

class BeverageRepositoryCustomImpl : BeverageRepositoryCustom, QuerydslRepositorySupport(Beverage::class.java) {
    override fun getBeverageByPlaceId(placeId: Long): List<Beverage> {
        return from(beverage)
            .where(beverage.place.id.eq(placeId))
            .fetch()
    }

    override fun getBeverageByFoodId(foodId: Long): List<Beverage> {
        return from(beverage)
            .where(beverage.food.id.eq(foodId))
            .fetch()
    }
}