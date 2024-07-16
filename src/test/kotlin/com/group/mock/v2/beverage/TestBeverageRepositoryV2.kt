package com.group.mock.v2.beverage

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.infrastructure.BeverageRepository
import com.group.mock.v2.common.BEVERAGE_TEST_DB
import com.group.mock.v2.common.jpa.TestJpaRepositoryV2
import com.group.mock.v2.food.TestFoodRepositoryV2
import com.group.mock.v2.place.TestPlaceRepositoryV2
import java.util.*

class TestBeverageRepositoryV2 : TestJpaRepositoryV2<Beverage, Long>(BEVERAGE_TEST_DB), BeverageRepository {
    private val foodRepository = TestFoodRepositoryV2()
    private val placeRepository = TestPlaceRepositoryV2()

    override fun <S : Beverage> save(entity: S): S {
        val food = foodRepository.save(entity.food)
        val place = placeRepository.save(entity.place)
        return super.save(entity.copy(food = food, place = place)) as S
    }

    override fun findById(id: Long): Optional<Beverage> {
        val optionalBeverage = super.findById(id)
        return if (optionalBeverage.isPresent) {
            val beverage = optionalBeverage.get()
            Optional.of(
                beverage.copy(
                    food = foodRepository.getReferenceById(beverage.food.id),
                    place = placeRepository.getReferenceById(beverage.place.id),
                )
            )
        } else {
            Optional.empty()
        }
    }

    override fun findAll(): List<Beverage> {
        return super.findAll().map { beverage ->
            beverage.copy(
                food = foodRepository.getReferenceById(beverage.food.id),
                place = placeRepository.getReferenceById(beverage.place.id),
            )
        }
    }

    override fun getReferenceById(id: Long): Beverage {
        return super.getReferenceById(id).let { beverage ->
            beverage.copy(
                food = foodRepository.getReferenceById(beverage.food.id),
                place = placeRepository.getReferenceById(beverage.place.id),
            )
        }
    }

    override fun getBeverageByPlaceId(placeId: Long): List<Beverage> {
        return entityList.filter { it.place.id == placeId }
    }

    override fun getBeverageByFoodId(foodId: Long): List<Beverage> {
        return entityList.filter { it.food.id == foodId }
    }
}