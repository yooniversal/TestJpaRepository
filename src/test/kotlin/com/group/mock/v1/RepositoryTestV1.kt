package com.group.mock.v1

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.infrastructure.BeverageRepository
import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.place.domain.Place
import com.group.mock.v1.beverage.TestBeverageRepositoryV1
import com.group.mock.v1.food.TestFoodRepositoryV1
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class RepositoryTestV1 {
    private lateinit var beverageRepository: BeverageRepository
    private lateinit var foodRepository: FoodRepository

    @BeforeEach
    fun setUp() {
        beverageRepository = TestBeverageRepositoryV1()
        foodRepository = TestFoodRepositoryV1()
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
        beverageRepository.findById(savedBeverage.id).also { optionalBeverage ->
            assertThat(optionalBeverage.isPresent).isFalse
        }
    }

    @Test
    @DisplayName("FoodRepository가 다르면 저장되지 않는다")
    fun `cannot read Food entity saved at different repository`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)

        // when & then
        assertThatThrownBy { foodRepository.getReferenceById(savedBeverage.food.id) }
                .isInstanceOf(NoSuchElementException::class.java)
    }

    @Test
    @DisplayName("Food가 저장된 Repository가 다르면 정합성이 깨진다")
    fun `Data integrity is broken if the Food is stored in a different repository`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        val savedBeverage = beverageRepository.save(beverage)
        val savedFood = foodRepository.save(food)

        // when
        val updatedFood = savedFood.copy(name = "cake")
        beverageRepository.save(savedBeverage.copy(food = updatedFood))

        // then
        foodRepository.getReferenceById(savedFood.id).also { food ->
            assertThat(food.name).isNotEqualTo(updatedFood.name)
        }
    }
}