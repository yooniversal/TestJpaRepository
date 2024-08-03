package com.group.mock.place

import com.group.mock.place.domain.Place
import com.group.mock.place.infrastructure.PlaceRepository
import com.group.mock.infrastructure.jpa.TestJpaRepository

object TestPlaceRepository : TestJpaRepository<Place, Long>("id"), PlaceRepository {
}