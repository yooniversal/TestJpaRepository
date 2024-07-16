package com.group.mock.v2.place

import com.group.mock.place.domain.Place
import com.group.mock.place.infrastructure.PlaceRepository
import com.group.mock.v2.common.PLACE_TEST_DB
import com.group.mock.v2.common.jpa.TestJpaRepositoryV2

class TestPlaceRepositoryV2 : TestJpaRepositoryV2<Place, Long>(PLACE_TEST_DB), PlaceRepository {
}