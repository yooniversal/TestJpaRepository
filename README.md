<div align=center>

# TestJpaRepository Documentation

  <img width="300" alt="TestJpaRepository" src="https://github.com/user-attachments/assets/fad4bd89-16ab-4f5d-b677-0ada89bc3f8b">
</div>

## Overview
**TestJpaRepository** is an abstract class that implements the JpaRepository interface (from [spring-data-jpa](https://github.com/spring-projects/spring-data-jpa)), 
designed to simulate a JpaRepository in a testing environment. 
This class provides functionality similar to a JpaRepository, allowing you to verify data access logic without a test database. 
It operates entirely in-memory, making it fast and independent of any external database.

## Getting Started
1. Create a repository class for testing that inherits from `TestJpaRepository`.
2. Pass the ID field name to the constructor.
3. In test class, define a repository field with the repository interface type and initialize it with what you made at `1`.

```kt
// Create a repository class for testing
// Pass the ID field name "id" to the constructor
// Example says Beverage Entity use PK field name as "id"
class TestBeverageRepository : TestJpaRepository<Beverage, Long>("id"), BeverageRepository {
}

class TestExample {
    // Define the repository field with the interface type as used in production
    private laitinit var beverageRepository: BeverageRepository

    @BeforeEach
    fun setUp() {
        // Initialize it with the repository for testing
        beverageRepository = TestBeverageRepositoryV1()
    }
}
```

## Versions
You can refer to the structures, implementations and test codes of **V1** and **V2** presented below.
| V1                                      | V2                                      |
| :------: | :------: |
| <img width="627" alt="240718_test_jpa_repository_v1" src="https://github.com/user-attachments/assets/66997aa9-f5e5-4902-9c3c-8596723e2bd1">             | <img width="589" alt="0715_initilaized_db" src="https://github.com/user-attachments/assets/4ca66ca6-3b1b-440c-b857-f4effc100440">             |
| [TestJpaRepositoryV1.kt](https://github.com/yooniversal/TestJpaRepository/blob/main/src/test/kotlin/com/group/mock/v1/common/jpa/TestJpaRepositoryV1.kt) | [TestJpaRepositoryV2.kt](https://github.com/yooniversal/TestJpaRepository/blob/main/src/test/kotlin/com/group/mock/v2/common/jpa/TestJpaRepositoryV2.kt)<br>[TestDatabase.kt](https://github.com/yooniversal/TestJpaRepository/blob/main/src/test/kotlin/com/group/mock/v2/common/TestDatabase.kt) |
| [RepositoryTestV1.kt](https://github.com/yooniversal/TestJpaRepository/blob/main/src/test/kotlin/com/group/mock/v1/RepositoryTestV1.kt) | [RepositoryTestV2.kt](https://github.com/yooniversal/TestJpaRepository/blob/main/src/test/kotlin/com/group/mock/v2/RepositoryTestV2.kt) |

## Advantages
- **No Database Dependency**: Enables testing without the need for a database.
- **No Mocking Library Required**: Removes the need to use mocking libraries.
- **Fast Execution**: Tests run faster as there is no database I/O.
- **Consistency with Production**: If you use JpaRepository in production, you can leverage the same structure in tests.

## Not Suitable For
- **Large Data Sets**: Not ideal for tests involving large amounts of data (should consider OOM).
- **Multi-threaded Environment**: Inappropriate for tests requiring multi-threaded access. It works on single-threaded only.

## Unsupported Features
The following methods throw `UnsupportedOperationException` as they are not supported in `TestJpaRepository`:
- `findAll(example: Example<S>)`
- `findAll(example: Example<S>, sort: Sort)`
- `findAll(example: Example<S>, pageable: Pageable)`
- `findAll(sort: Sort)`
- `findBy(example: Example<S>, queryFunction: Function<FluentQuery.FetchableFluentQuery<S>, R>)`
- `exists(example: Example<S>)`
- `findOne(example: Example<S>)`
- `count(example: Example<S>)`

Additionally:
- `flush()` method does nothing.

## License
[MIT License](https://github.com/yooniversal/TestJpaRepository?tab=MIT-1-ov-file)
