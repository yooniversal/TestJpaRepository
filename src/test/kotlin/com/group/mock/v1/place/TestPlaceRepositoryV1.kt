package com.group.mock.v1.place

import com.group.mock.place.domain.Place
import com.group.mock.place.infrastructure.PlaceRepository
import com.group.mock.v1.common.jpa.TestJpaRepositoryV1

class TestPlaceRepositoryV1 : TestJpaRepositoryV1<Place, Long>("id"), PlaceRepository {
}