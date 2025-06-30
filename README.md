# Pricing Service

A Spring Boot microservice implementing a pricing query system for e-commerce products using Hexagonal Architecture principles.

## Architecture

The application follows Hexagonal Architecture (Ports and Adapters) pattern with clear separation of concerns:

- **Domain Layer**: Contains business logic, entities, and domain services
- **Application Layer**: Orchestrates use cases and handles external communications
- **Infrastructure Layer**: Implements adapters for databases and external systems

## Tech Stack

- **Framework**: Spring Boot 3.5.3
- **Language**: Java 21
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA
- **Validation**: Jakarta Validation
- **Caching**: Caffeine Cache
- **Documentation**: OpenAPI 3 / Swagger UI
- **Testing**: JUnit 5, ArchUnit for architecture testing
- **Build Tool**: Gradle

## API Endpoints

### Get Applicable Price
```
GET /api/v1/prices?brandId={brandId}&productId={productId}&applicationDate={applicationDate}
```

**Parameters:**
- `brandId` (Long): Brand identifier
- `productId` (Long): Product identifier  
- `applicationDate` (LocalDateTime): Date in ISO format (yyyy-MM-dd'T'HH:mm:ss)

**Response:** Price information with applicable rates and validity periods.

## Getting Started

### Prerequisites
- Java 21+
- Gradle 8+

### Running the Application

```bash
# Clone the repository
git clone <repository-url>
cd Prueba_Java_Hexagonal

# Run the application
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### API Documentation
Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

### Database Console
H2 Console available at: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (empty)

## Testing

```bash
# Run all tests
./gradlew test

# Run architecture tests
./gradlew test --tests "*ArchitectureTest"

# Run integration tests
./gradlew test --tests "*RequiredTest"
```

## Project Structure

```
src/
├── main/java/com/prueba/tecnica/pricing/
│   ├── application/          # Controllers, DTOs, exceptions
│   ├── domain/              # Business logic, entities, ports
│   │   ├── model/           # Domain entities
│   │   ├── port/            # Inbound/outbound interfaces
│   │   └── service/         # Domain services
│   └── infrastructure/      # Database adapters, configurations
└── test/                    # Unit and integration tests
```

## Business Rules

The system implements priority-based pricing with the following logic:
- Returns the price with the highest priority for overlapping date ranges
- Supports multiple price lists per product and brand
- Date ranges determine price applicability periods

## Configuration

Key application properties:
- Database: H2 in-memory with auto-initialization
- JPA: Show SQL enabled for development
- Caching: Enabled with Caffeine
- Validation: Jakarta Bean Validation enabled

## Development

The application uses clean architecture principles:
- Domain logic is independent of frameworks
- Dependencies point inward toward the domain
- Infrastructure adapters implement domain ports
- Business rules are isolated and testable
