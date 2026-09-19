# ecommance

### How to Run for the first time

### Step 1: Initialize PostgreSQL Database via Docker

1. Open a terminal in the project root directory (`ecommance/`).

2. Start the PostgreSQL container in detached mode:

```bash
docker compose up -d
```

### Step 2: Start Backend Microservices

#### Service Execution Order:

1. **User Service** (`user-service`) - Port `8081`

```bash
cd user-service
mvn spring-boot:run
```

2. **Product Service** (`product-service`) - Port `8082`

```bash
cd product-service
mvn spring-boot:run
```

3. **Cart Service** (`cart-service`) - Port `8084`

```bash
cd cart-service
mvn spring-boot:run
```

4. **Order Service** (`order-service`) - Port `8083`

```bash
cd order-service
mvn spring-boot:run
```

5. **Payment Service** (`payment-service`) - Port `8085`

```bash
cd payment-service
mvn spring-boot:run
```

6. **API Gateway Service** (`gateway-service`) - Port `8090`

```bash
cd gateway-service
mvn spring-boot:run
```

### Step 3: Launch the Frontend

1. Open a terminal and navigate to the `frontend` folder:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Start the development server:

```bash
npm run dev
```

4. Access the web app in your browser at: **`http://localhost:5173`**

## Port Details

| Component / Service | Port |
|---------------------|------|
| Frontend            | `5173` |
| API Gateway         | `8090` |
| User Service        | `8081` |
| Product Service     | `8082` |
| Order Service       | `8083` |
| Cart Service        | `8084` |
| Payment Service     | `8085` |
| PostgreSQL Database | `5433` |