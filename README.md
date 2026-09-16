# SmartInsight Hub 🚀

SmartInsight Hub is an AI-powered data orchestration platform that bridges the gap between raw database records and actionable business insights. Using **Spring AI** and **Google Gemini**, it provides a conversational interface for real-time data analysis, moving beyond static dashboards to dynamic, natural language-driven exploration.

---

## 🌟 Features

- **Natural Language Queries**: Ask questions like *"What are my best-selling electronics?"* instead of manually filtering data.
- **AI Orchestration**: Seamlessly feeds database context to LLMs using Spring AI.
- **Advanced BI Insights**: Leverages Text-to-SQL capabilities to provide deep insights from Product and Sales history.
- **Direct SQL Execution**: Power user tool for executing raw SQL queries against the underlying database.
- **Interactive API Docs**: Fully documented REST endpoints via Swagger UI.

---

## 🛠️ Tech Stack

- **Backend**: Java 17, Spring Boot, Spring AI, Spring Data JPA
- **AI**: Google Gemini (Generative AI)
- **Database**: H2 (In-memory, easily swappable for PostgreSQL)
- **Documentation**: SpringDoc OpenAPI (Swagger UI)

---

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher.
- **Maven 3.6+**.
- A **Google Gemini API Key**.

### Configuration

1. Obtain your API key from the [Google AI Studio](https://aistudio.google.com/).
2. Open `backend/src/main/resources/application.yml`.
3. Update the `api-key` field:
   ```yaml
   spring:
     ai:
       google:
         genai:
           api-key: YOUR_GEMINI_API_KEY_HERE
   ```

### Running the Application

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```
2. Run the application using Maven:
   ```bash
   mvn spring-boot:run
   ```
3. The server will start on `http://localhost:8080`.

---

## 📖 API Documentation

Once the application is running, you can access the interactive Swagger UI at:
👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

### Key Endpoints:
- `POST /api/ai/query`: Simple natural language query on products.
- `POST /api/ai/advanced-query`: Advanced BI insights using Text-to-SQL.
- `POST /api/ai/execute-sql`: Direct SQL execution.
- `GET /api/products`: Retrieve all products in the system.

---

## 📂 Project Structure

```text
backend/
├── src/main/java/com/example/smartinsight/
│   ├── controller/    # REST Endpoints (AI & Product)
│   ├── service/       # AI Logic & Database Interaction
│   ├── repository/    # JPA Data Access
│   └── model/         # Data Entities (Product, SalesRecord)
├── src/main/resources/
│   └── application.yml # Configuration & AI Settings
└── pom.xml            # Dependencies (Spring AI, etc.)
```

---

## 🎯 Hackathon Strategy

- **Ease of Use**: Focus on natural language to make data accessible to everyone.
- **Scalability**: Architecture designed to swap H2 for production-grade databases and add complex RAG patterns.
- **Demonstrate Value**: Show how AI detects trends or anomalies that manual analysis might miss.

---

Developed for **AI Orchestrator Project R&D**.
