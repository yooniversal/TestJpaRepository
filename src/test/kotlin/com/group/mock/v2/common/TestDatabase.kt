package com.group.mock.v2.common

import com.group.mock.beverage.domain.Beverage
import com.group.mock.food.domain.Food
import com.group.mock.place.domain.Place
import java.util.concurrent.atomic.AtomicLong

class TestDatabase<T, ID>(
    val idName: String,
) {
    val index = AtomicLong(0L)
    val indexSet = mutableSetOf<ID>()
    val entityList = mutableListOf<T>()
}

val BEVERAGE_TEST_DB = TestDatabase<Beverage, Long>("id")
val FOOD_TEST_DB = TestDatabase<Food, Long>("id")
val PLACE_TEST_DB = TestDatabase<Place, Long>("id")