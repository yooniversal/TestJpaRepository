package com.group.mock

import com.group.mock.beverage.domain.Beverage
import com.group.mock.beverage.infrastructure.BeverageRepository
import com.group.mock.food.domain.Food
import com.group.mock.food.infrastructure.FoodRepository
import com.group.mock.place.domain.Place
import com.group.mock.beverage.TestBeverageRepository
import com.group.mock.food.TestFoodRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

class RepositoryTest {

    private val beverageRepository = TestBeverageRepository
    private val foodRepository = TestFoodRepository

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
    @DisplayName("여러 Beverage를 한번에 저장 및 조회할 수 있다")
    fun `save and read Beverages`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)

        // when
        val savedBeverages = beverageRepository.saveAll(listOf(milk, coffee))

        // then
        beverageRepository.findAll().let { beverages ->
            assertThat(beverages[0]).isEqualTo(savedBeverages[0])
            assertThat(beverages[1]).isEqualTo(savedBeverages[1])
        }
    }

    @Test
    @DisplayName("여러 Beverage를 id로 조회할 수 있다")
    fun `save and read Beverages by ids`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)

        // when
        val savedBeverages = beverageRepository.saveAll(listOf(milk, coffee))

        // then
        beverageRepository.findAllById(savedBeverages.map { it.id }).let { beverages ->
            assertThat(beverages[0]).isEqualTo(savedBeverages[0])
            assertThat(beverages[1]).isEqualTo(savedBeverages[1])
        }
    }

    @Test
    @DisplayName("여러 Beverage를 한번에 저장 및 조회할 수 있다")
    fun `verify the Beverage by id`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val beverage = Beverage(name = "milk", food = food, place = place)
        beverageRepository.save(beverage)

        // when & then
        assertThat(beverageRepository.existsById(1L)).isTrue
    }

    @Test
    @DisplayName("저장된 Beverage의 수를 알 수 있다")
    fun `read count of Beverages`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)
        val tea = Beverage(name = "tea", food = food, place = place)
        beverageRepository.saveAll(listOf(milk, coffee, tea))

        // when & then
        assertThat(beverageRepository.count()).isEqualTo(3)
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
    @DisplayName("저장된 Beverage를 모두 삭제할 수 있다")
    fun `delete all of Beverages`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)
        val tea = Beverage(name = "tea", food = food, place = place)
        beverageRepository.saveAll(listOf(milk, coffee, tea))

        // when
        beverageRepository.deleteAll()

        // then
        assertThat(beverageRepository.count()).isZero
    }

    @Test
    @DisplayName("여러 Beverage를 id로 삭제할 수 있다")
    fun `delete multiple of Beverages by ids`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)
        val tea = Beverage(name = "tea", food = food, place = place)
        val savedIds = beverageRepository.saveAll(listOf(milk, coffee, tea)).map { it.id }

        // when
        beverageRepository.deleteAllById(savedIds)

        // then
        assertThat(beverageRepository.count()).isZero
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
    @DisplayName("여러 Beverage를 entity로 삭제할 수 있다")
    fun `delete multiple of Beverages by entities`() {
        // given
        val food = Food(name = "bread")
        val place = Place(name = "bakery")
        val milk = Beverage(name = "milk", food = food, place = place)
        val coffee = Beverage(name = "coffee", food = food, place = place)
        val tea = Beverage(name = "tea", food = food, place = place)
        val savedBeverages = beverageRepository.saveAll(listOf(milk, coffee, tea))

        // when
        beverageRepository.deleteAllInBatch(savedBeverages)

        // then
        assertThat(beverageRepository.count()).isZero
    }

    @Test
    @DisplayName("위치가 다른 FoodRepository에 저장하면 조회할 수 없다")
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