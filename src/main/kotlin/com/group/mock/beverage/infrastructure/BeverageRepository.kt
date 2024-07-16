package com.group.mock.beverage.infrastructure

import com.group.mock.beverage.domain.Beverage
import org.springframework.data.jpa.repository.JpaRepository

interface BeverageRepository : JpaRepository<Beverage, Long>, BeverageRepositoryCustom {
}