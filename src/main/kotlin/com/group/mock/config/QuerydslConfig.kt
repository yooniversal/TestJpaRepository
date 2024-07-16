package com.group.mock.config

import com.querydsl.jpa.impl.JPAQueryFactory
import jakarta.persistence.EntityManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class QuerydslConfig(
    private val entityManager: EntityManager
) {

    @Bean
    fun querydsl(): JPAQueryFactory {
        return JPAQueryFactory(entityManager)
    }
}