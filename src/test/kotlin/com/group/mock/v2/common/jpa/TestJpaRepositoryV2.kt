package com.group.mock.v2.common.jpa

import com.group.mock.v2.common.TestDatabase
import org.springframework.data.domain.*
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.query.FluentQuery
import org.springframework.util.Assert
import java.util.*
import java.util.concurrent.atomic.AtomicLong
import java.util.function.Function
import kotlin.reflect.full.memberProperties

abstract class TestJpaRepositoryV2<T, ID>(
    private val testDatabase: TestDatabase<T, ID>,
) : JpaRepository<T, ID> where T : Any {

    private val index: AtomicLong = testDatabase.index
    private val indexSet = testDatabase.indexSet
    private val idName = testDatabase.idName
    protected val entityList: MutableList<T> = testDatabase.entityList

    override fun <S : T> save(entity: S): S {
        Assert.notNull(entity, ENTITY_MUST_NOT_BE_NULL)
        upsert(entity)
        return entity
    }

    override fun <S : T> saveAll(entities: Iterable<S>): List<S> {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL)
        entities.forEach { entity -> save(entity) }
        return entities.toList()
    }

    override fun findById(id: ID): Optional<T> {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL)
        return Optional.ofNullable(entityList.find { it.getId<T, ID>() == id })
    }

    override fun existsById(id: ID): Boolean {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL)
        return entityList.any { it.getId<T, ID>() == id }
    }

    override fun <S : T> findAll(example: Example<S>): List<S> {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun <S : T> findAll(example: Example<S>, sort: Sort): List<S> {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun findAll(): List<T> {
        return entityList
    }

    override fun findAll(sort: Sort): List<T> {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun findAll(pageable: Pageable): Page<T> {
        return entityList.size.let { size ->
            val start = pageable.offset.toInt()
            val end = (start + pageable.pageSize).coerceAtMost(size)
            PageImpl(entityList.subList(start, end), pageable, size.toLong())
        }
    }

    override fun count(): Long {
        return entityList.size.toLong()
    }

    override fun deleteAll() {
        entityList.clear()
    }

    override fun flush() {
    }

    override fun deleteAllInBatch() {
        entityList.clear()
    }

    override fun getReferenceById(id: ID): T {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL)
        return entityList.find { it.getId<T, ID>() == id }
            ?: throw NoSuchElementException("No entity found for id: $id")
    }

    @Deprecated("Use getReferenceById instead", ReplaceWith("getReferenceById(id)"))
    override fun getById(id: ID): T {
        return getReferenceById(id)
    }

    @Deprecated("Use getReferenceById instead", ReplaceWith("getReferenceById(id)"))
    override fun getOne(id: ID): T {
        return getReferenceById(id)
    }

    override fun deleteAllByIdInBatch(ids: Iterable<ID>) {
        Assert.notNull(ids, ID_LIST_MUST_NOT_BE_NULL)
        val idSet = ids.toSet()
        entityList.removeIf { entity -> idSet.contains(entity.getId<T, ID>()) }
    }

    override fun deleteAllInBatch(entities: Iterable<T>) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL)
        deleteAllByIdInBatch(entities.mapNotNull { it.getId<T, ID>() })
    }

    override fun <S : T> saveAllAndFlush(entities: Iterable<S>): List<S> {
        return saveAll(entities)
    }

    override fun <S : T> saveAndFlush(entity: S): S {
        return save(entity)
    }

    override fun <S : T, R : Any?> findBy(
        example: Example<S>,
        queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>
    ): R {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun <S : T> exists(example: Example<S>): Boolean {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun <S : T> findOne(example: Example<S>): Optional<S> {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun deleteAll(entities: Iterable<T>) {
        Assert.notNull(entities, ENTITIES_MUST_NOT_BE_NULL)
        deleteAllByIdInBatch(entities.mapNotNull { it.getId<T, ID>() })
    }

    override fun deleteAllById(ids: Iterable<ID>) {
        Assert.notNull(ids, ID_LIST_MUST_NOT_BE_NULL)
        deleteAllByIdInBatch(ids)
    }

    override fun delete(entity: T) {
        entityList.removeIf { savedEntity -> savedEntity.getId<T, ID>() == entity.getId<T, ID>() }
    }

    override fun deleteById(id: ID) {
        Assert.notNull(id, ID_MUST_NOT_BE_NULL)
        entityList.removeIf { it.getId<T, ID>() == id }
    }

    override fun <S : T> count(example: Example<S>): Long {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    override fun findAllById(ids: Iterable<ID>): List<T> {
        Assert.notNull(ids, ID_LIST_MUST_NOT_BE_NULL)
        val idSet = ids.toSet()
        return entityList.filter { idSet.contains(it.getId<T, ID>()) }
    }

    override fun <S : T> findAll(example: Example<S>, pageable: Pageable): Page<S> {
        throw UnsupportedOperationException(NOT_SUPPORTED)
    }

    protected fun <T : Any, ID> T.getId(): ID? {
        return this::class.memberProperties
            .firstOrNull { it.name == idName }?.getter?.call(this) as? ID
    }

    protected inline fun <T : Any, reified TYPE> T.getField(fieldName: String): TYPE? {
        return this::class.memberProperties
            .firstOrNull { it.name == fieldName }?.getter?.call(this) as? TYPE
    }

    private fun <S : T> upsert(entity: S) {
        val requestId = entity.getId<T, ID>()
        if (isNew(requestId)) {
            updateId(entity)
        } else {
            entityList.removeIf { it.getId<T, ID>() == requestId }
        }
        entityList.add(entity)
    }

    private fun isNew(id: Any?): Boolean {
        if (id == null) return true
        return when (id) {
            is Long -> id == 0L
            is Int -> id == 0
            is String -> id.isEmpty()
            else -> throw IllegalArgumentException("Unsupported id type: ${id::class.simpleName}")
        }
    }

    private fun <S : T> updateId(entity: S) {
        entity::class.java.getDeclaredField(idName).apply {
            isAccessible = true

            generateId(type).let { newIndex ->
                set(entity, newIndex)
                if (indexSet.contains(newIndex)) {
                    entityList.removeIf { it.getId<T, ID>() == newIndex }
                } else {
                    indexSet.add(newIndex)
                }
            }
        }
    }

    private fun generateId(type: Class<*>): ID {
        return when (type) {
            Long::class.java -> return index.incrementAndGet() as ID
            Int::class.java -> index.incrementAndGet().toInt() as ID
            String::class.java -> return UUID.randomUUID().toString() as ID
            else -> throw IllegalArgumentException("Unsupported id type")
        }
    }

    companion object {
        private const val ID_MUST_NOT_BE_NULL = "The given id must not be null"
        private const val ID_LIST_MUST_NOT_BE_NULL = "The given Ids must not be null"
        private const val ENTITY_MUST_NOT_BE_NULL = "Entity must not be null"
        private const val ENTITIES_MUST_NOT_BE_NULL = "Entities must not be null"
        private const val NOT_SUPPORTED = "Not supported"
    }
}