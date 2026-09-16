# Low-Level Design (LLD) - Backend

## 1. Architecture Overview
The backend follows a standard layered architecture for Spring Boot applications.

- **Controller Layer:** Handles REST requests.
- **Service Layer:** Contains business logic and AI orchestration.
    - `AIService`: Manages prompts and AI communication.
- **Repository Layer:** Manages data persistence using JPA.
- **Model Layer:** Defines data entities (`Product`, `SalesRecord`).
- **Documentation Layer:** Integrated Swagger/OpenAPI for interactive API exploration.

## 2. Component Design

### AIService
- **Responsibilities:** Communicates with the Google AI Gemini model and manages context.
- **Logic:** Provides DB schema to AI to enable Text-to-SQL and predictive analysis. Returns structured JSON for frontend compatibility.

### QueryExecutorService
- **Responsibilities:** Executes AI-generated SQL queries using `JdbcTemplate`.
- **Security:** Implements basic keyword filtering to ensure only read-only `SELECT` statements are executed.

### DataInitializer
- **Responsibilities:** Seeds the H2 database with initial data.
- **Data:** Products and randomized `SalesRecord` history for the last 30 days.

## 3. Class Diagram (Conceptual)
- `Product`: Entity with fields `id`, `name`, `category`, `price`, `stockQuantity`, `lastUpdated`.
- `SalesRecord`: Entity linking `Product` to historical sales (qty, price, date).
- `ProductRepository`, `SalesRecordRepository`: JPA interfaces.
- `AIController`: Endpoints `/api/ai/query`, `/api/ai/advanced-query`.

## 4. Sequence Flow: Advanced AI Insights
1. `AIController` receives `/api/ai/advanced-query`.
2. `AIService` constructs a prompt with the DB schema and user query.
3. AI returns a JSON response containing an interpretation, a SQL query, and analysis.
4. (Optional) The frontend or backend can then use the generated SQL for display or further analysis.
