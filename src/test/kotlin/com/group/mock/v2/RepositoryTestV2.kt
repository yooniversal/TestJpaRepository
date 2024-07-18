package com.group.mock.v2

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.infrastructure.BeverageRepository
import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.place.domain.Place
import com.group.mock.v2.beverage.TestBeverageRepositoryV2
import com.group.mock.v2.food.TestFoodRepositoryV2
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class RepositoryTestV2 {
    private lateinit var beverageRepository: BeverageRepository
    private lateinit var foodRepository: FoodRepository

    @BeforeEach
    fun setUp() {
        beverageRepository = TestBeverageRepositoryV2()
        foodRepository = TestFoodRepositoryV2()
    }

    @Test
    @DisplayName("Beverage를 저장 및 조회할 수 있다")
    fun `save and read a Beverage`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)

        // when
        val savedBeverage = beverageRepository.save(beverage)

        // then
        beverageRepository.findByIdOrNull(savedBeverage.id).also { beverage ->
            assertThat(beverage).isNotNull
            assertThat(beverage!!.name).isEqualTo("milk")
            assertThat(beverage.food.name).isEqualTo("bread")
            assertThat(beverage.place.name).isEqualTo("bakery")
        }
    }

    @Test
    @DisplayName("Beverage를 수정할 수 있다")
    fun `modify a Beverage`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)

        // when
        val updatedBeverage = beverageRepository.save(savedBeverage.copy(name = "coffee"))

        // then
        beverageRepository.findByIdOrNull(updatedBeverage.id).also { beverage ->
            assertThat(beverage).isNotNull
            assertThat(beverage!!.name).isEqualTo("coffee")
            assertThat(beverage.food.name).isEqualTo("bread")
            assertThat(beverage.place.name).isEqualTo("bakery")
        }
    }

    @Test
    @DisplayName("Beverage를 id로 삭제할 수 있다")
    fun `delete Beverage by id`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)

        // when
        beverageRepository.deleteById(savedBeverage.id)

        // then
        beverageRepository.findByIdOrNull(savedBeverage.id).also { beverage ->
            assertThat(beverage).isNull()
        }
    }

    @Test
    @DisplayName("Beverage를 entity로 삭제할 수 있다")
    fun `delete Beverage by entity`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)

        // when
        beverageRepository.delete(savedBeverage)

        // then
        beverageRepository.findByIdOrNull(savedBeverage.id).also { beverage ->
            assertThat(beverage).isNull()
        }
    }

    @Test
    @DisplayName("Food를 저장한 Repository가 달라도 정합성이 깨지지 않는다")
    fun `Data integrity remains intact even if the Food is stored in different repositories`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)

        // when
        val updatedFood = savedBeverage.food.copy(name = "cake")
        beverageRepository.save(savedBeverage.copy(food = updatedFood))

        // then
        foodRepository.getReferenceById(updatedFood.id).also { food ->
            assertThat(food.name).isEqualTo("cake")
        }
    }
}