# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Java 21 study repository focused on implementing business domain exercises using Event Sourcing, CQRS, and Domain-Driven Design patterns. Currently implements Exercise E (subscriptions-billing) with sophisticated event-driven architecture.

**Group ID**: `org.lucasxf`
**Artifact ID**: `estudos`
**Java Version**: 21
**Spring Boot**: 3.5.6

## Build Commands

```bash
# Clean and compile the project
mvn clean compile

# Run tests
mvn test

# Build the project
mvn clean package

# Run the Spring Boot application
mvn spring-boot:run
# Or: java -jar target/estudos-1.0-SNAPSHOT.jar

# Run a single test class
mvn test -Dtest=AccountAggregateTest

# Run a specific test method
mvn test -Dtest=AccountAggregateTest#givenInactiveAccount_WhenCreateAccount_ThenAccountCreated
```

## Architecture Overview

This codebase implements **Event Sourcing with CQRS** following Domain-Driven Design principles.

### Core Architectural Patterns

1. **Event Sourcing**: State is reconstructed by replaying immutable events stored in an event stream
2. **CQRS**: Commands represent intent, Events represent facts
3. **DDD Aggregates**: AccountAggregate, SubscriptionAggregate, UsernameAggregate
4. **Saga Pattern**: AccountSignupSaga orchestrates multi-aggregate workflows
5. **Repository Pattern**: Event-sourcing variant using AbstractEventSourcingRepository
6. **Optimistic Locking**: Version-based concurrency control in EventStore

### Layer Structure

```
application/        - Use cases, REST API, command handlers, sagas
  ├── api/         - REST controllers and DTOs
  ├── commands/    - Command handlers
  └── process/     - Saga orchestrators

domain/            - Pure business logic, no infrastructure dependencies
  ├── account/     - Account aggregate, commands, events
  ├── subscription/ - Subscription aggregate, commands, events
  └── username/    - Username aggregate, commands, events

infrastructure/    - Technical concerns
  ├── eventstore/  - In-memory event persistence
  ├── repository/  - Event-sourcing repositories
  ├── messaging/   - Event bus (pub/sub)
  ├── serialization/ - Event JSON mapping
  └── context/     - Execution context (correlation/causation IDs)
```

### Key Components

**EventStore** (`infrastructure/eventstore/EventStore.java`)
- In-memory event persistence using ConcurrentHashMap
- Thread-safe with synchronized event stream access
- Optimistic locking via version checking in `appendRaw()`
- Key for testing and learning event sourcing concepts

**Aggregates** (Domain layer)
- Each aggregate has: `decide()`, `apply()`, and `replay()` methods
- `decide()`: Validates command, returns events (doesn't mutate state)
- `apply()`: Applies single event to state
- `replay()`: Reconstructs state by applying all historical events
- State is always derived from events, never stored directly

**Command Handlers** (`application/commands/handlers/impl/`)
- Load aggregate from repository (event stream)
- Execute aggregate's `decide()` method
- Persist resulting events
- Publish events via EventBus

**EventBus** (`infrastructure/messaging/impl/InMemoryEventBus.java`)
- In-memory pub/sub for domain events
- Type-safe event handler registration
- Automatic MDC context propagation for tracing

**Execution Context** (`infrastructure/context/`)
- `ExecutionContext`: Record holding correlationId and causationId
- `ContextScope`: Try-with-resources wrapper for MDC management
- Enables distributed tracing across aggregate boundaries

### Event Sourcing Flow

1. Command arrives (e.g., CreateAccount)
2. Handler loads aggregate from event stream
3. Aggregate reconstructs state via `replay()`
4. Aggregate executes `decide(command)` → returns events
5. Handler persists events to EventStore
6. Handler publishes events via EventBus
7. Event handlers react (e.g., Saga listens and triggers next command)

### Java 21 Features Used

- **Sealed Interfaces**: All command and event hierarchies are sealed for type safety
- **Records**: Immutable DTOs, events, value objects
- **Pattern Matching**: Switch expressions with pattern matching in `replay()` methods
- **Virtual Threads**: Ready for Spring Boot 3.x virtual thread support

## Development Guidelines

### Adding a New Command

1. Define command as sealed interface record in `domain/<aggregate>/command/`
2. Add event record in `domain/<aggregate>/event/`
3. Register event type in `EventTypeRegistry.java`
4. Implement `decide()` method in aggregate
5. Implement `apply()` method for state changes
6. Add pattern in `replay()` method
7. Create handler in `application/commands/handlers/impl/`
8. Wire handler to controller or saga

### Testing Aggregates

Follow Given-When-Then pattern:
```java
@Test
void testName() {
    // given - create aggregate with initial events
    var aggregate = AccountAggregate.from(id, List.of(), 0);
    var command = new CreateAccount(...);

    // when - execute decision
    var event = aggregate.decide(command);

    // then - verify event and state
    assertEquals(expected, event.field());
    assertEquals(expectedState, aggregate.getStatus());
}
```

### Concurrency Handling

The EventStore uses optimistic locking:
- Each event has a version number
- `appendRaw()` verifies expected version matches actual
- Throws `ConcurrencyException` on mismatch
- Handlers should catch and retry with fresh aggregate state

### Logging and Tracing

All operations should use ExecutionContext:
```java
try (var scope = ContextScope.open(correlationId, causationId)) {
    logger.info("Processing command"); // MDC includes IDs
    // ... work here
}
```

Logback pattern includes: `[correlationId=%X{correlationId} causationId=%X{causationId}]`

## Project Structure Context

This repository is designed for multiple exercises (A-F) as separate Maven modules. Currently only Exercise E is implemented. Each exercise should:
- Be a separate Maven module (future multi-module structure)
- Have its own README.md with decisions and trade-offs
- Include JUnit 5 tests covering success and failure paths
- Optionally expose Spring Boot REST APIs

### Current Implementation: Exercise E

Subscription billing system with:
- Trial periods and conversions
- Plan upgrades/downgrades (BASIC → STANDARD → PREMIUM)
- Pro-rata billing calculations
- Account and username management
- Multi-aggregate saga for signup flow

## Common Pitfalls

1. **Don't mutate aggregate state in decide()** - Return events only
2. **Always replay events to reconstruct state** - Don't store aggregate state directly
3. **Use correlationId consistently** - Pass through entire command chain
4. **Register new event types** - Add to EventTypeRegistry before use
5. **Test with event streams** - Create aggregates with historical events to test state transitions
6. **Handle ConcurrencyException** - Retry command with fresh aggregate
7. **Keep domain layer pure** - No Spring annotations or infrastructure concerns in domain/

## Dependencies

- **Spring Boot 3.5.6**: Web framework and dependency injection
- **Lombok 1.18.38**: Reduces boilerplate (getters, constructors, builders)
- **JUnit 5.13.4**: Testing framework
- **Jackson**: JSON serialization (transitively from Spring Boot)
- **SLF4J/Logback**: Logging

## Repository Status

**Branch**: develop
**Main branch**: main (use for PRs)

The codebase is in active development:
- AccountSignupSaga partially implemented
- Event sourcing infrastructure complete
- In-memory implementations ready for learning
- Production persistence (PostgreSQL, Kafka) not yet implemented
