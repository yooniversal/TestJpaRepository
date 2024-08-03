package com.group.mock.configuration

import com.group.mock.beverage.TestBeverageRepository
import com.group.mock.food.TestFoodRepository
import com.group.mock.place.TestPlaceRepository
import org.junit.jupiter.api.AfterEach

abstract class TestRepositorySupport {

    @AfterEach
    fun tearDown() {
        TestBeverageRepository.initalize()
        TestFoodRepository.initalize()
        TestPlaceRepository.initalize()
    }
}