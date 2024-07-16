package com.group.mock.place.infrastructure

import com.group.mock.place.domain.Place
import org.springframework.data.jpa.repository.JpaRepository

interface PlaceRepository : JpaRepository<Place, Long>, PlaceRepositoryCustom {
}