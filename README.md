# E-Banking Digital Platform

## Overview
The **E-Banking Digital Platform** is a full-stack web application designed to manage bank accounts and customer operations. It provides a secure and user-friendly interface for performing banking operations such as account creation, credit/debit transactions, transfers, and customer management. The backend is built with **Spring Boot** and **JPA/Hibernate**, while the frontend is developed using **Angular**. The application incorporates **JWT-based authentication** for secure access and role-based authorization (USER and ADMIN roles).

### Key Features
- **Customer Management**: Create, update, delete, and search for customers.
- **Account Management**: Support for current and savings accounts with operations like debit, credit, and transfers.
- **Operation History**: View paginated transaction history for accounts.
- **Security**: JWT-based authentication and role-based access control (USER and ADMIN).
- **Frontend**: Responsive Angular interface with forms, tables, and pagination for account and customer management.
- **Error Handling**: Custom exceptions for insufficient balance, account not found, and customer not found.

## Project Structure
The project is divided into two main parts:
1. **Backend**: Spring Boot application handling business logic, REST APIs, and database interactions.
2. **Frontend**: Angular application for the user interface, communicating with the backend via HTTP requests.

### Backend Structure
- **Packages**:
  - `ma.enset.ebanking_digital.dtos`: Data Transfer Objects for API communication.
  - `ma.enset.ebanking_digital.entities`: JPA entities for database persistence.
  - `ma.enset.ebanking_digital.enums`: Enums for account status and operation types.
  - `ma.enset.ebanking_digital.exceptions`: Custom exception classes.
  - `ma.enset.ebanking_digital.mappers`: Mappers for converting between entities and DTOs.
  - `ma.enset.ebanking_digital.repositories`: JPA repositories for database operations.
  - `ma.enset.ebanking_digital.services`: Business logic for banking operations.
  - `ma.enset.ebanking_digital.web`: REST controllers for API endpoints.
  - `ma.enset.ebanking_digital.security`: JWT-based security configuration.

### Frontend Structure
- **Components**:
  - `AccountsComponent`: Manages account search and operations (debit, credit, transfer).
  - `CustomersComponent`: Displays and manages customer data.
  - `NewCustomerComponent`: Form for adding new customers.
  - `CustomerAccountComponent`: Displays accounts for a specific customer.
  - `LoginComponent`: Handles user authentication.
  - `NavbarComponent`: Navigation bar with role-based menu options.
  - `NotAuthorizedComponent`: Displays unauthorized access messages.
- **Services**:
  - `AccountsService`: Handles HTTP requests for account-related operations.
  - `CustomerService`: Manages customer-related API calls.
  - `AuthService`: Manages JWT authentication and token storage.
- **Guards and Interceptors**:
  - `AuthenticationGuard`: Ensures routes are accessed by authenticated users.
  - `AuthorizationGuard`: Restricts access to admin-only routes.
  - `AppHttpInterceptor`: Adds JWT tokens to HTTP requests.

## Prerequisites
- **Backend**:
  - Java 17 or later
  - Maven 3.6+
  - MySQL or any relational database
- **Frontend**:
  - Node.js 16+
  - Angular CLI 16+
- **Others**:
  - A web browser (Chrome, Firefox, etc.)
  - Postman (optional, for API testing)

## Setup Instructions

### Backend Setup
1. **Clone the Repository**:
   ```bash
   git clone <repository-url>
   cd ebanking-digital
   ```

2. **Configure Database**:
   - Create a MySQL database named `ebanking_db`.
   - Update the `application.properties` file in `src/main/resources` with your database credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/ebanking_db?createDatabaseIfNotExist=true
     spring.datasource.username=root
     spring.datasource.password=your_password
     spring.jpa.hibernate.ddl-auto=update
     jwt.secret=your_jwt_secret_key
     ```

3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```
   The backend will be available at `http://localhost:8085`.

### Frontend Setup
1. **Navigate to Frontend Directory**:
   ```bash
   cd ebanking-digital-frontend
   ```

2. **Install Dependencies**:
   ```bash
   npm install
   ```

3. **Configure Backend URL**:
   - Update the `environment.ts` file in `src/environments`:
     ```typescript
     export const environment = {
       production: false,
       backendHost: "http://localhost:8085"
     };
     ```

4. **Run the Frontend**:
   ```bash
   ng serve
   ```
   The frontend will be available at `http://localhost:4200`.

## Usage
1. **Login**:
   - Access the login page at `http://localhost:4200/login`.
   - Use the following credentials:
     - **User**: `user1` / Password: `12345` (USER role)
     - **Admin**: `admin` / Password: `12345` (USER and ADMIN roles)

2. **Customer Management**:
   - Navigate to `/admin/customers` to view, search, add, edit, or delete customers (ADMIN role required for add/edit/delete).
   - Use the search bar to filter customers by name.

3. **Account Management**:
   - Go to `/admin/accounts` to search for accounts by ID and perform operations (debit, credit, transfer).
   - View paginated transaction history for each account.

4. **Customer Accounts**:
   - From the customers page, click "Account" to view a customer's accounts.

## Security
The application uses **JWT-based authentication**:
- **Login**: The `/auth/login` endpoint generates a JWT token upon successful authentication.
- **Authorization**: Routes are protected using `@PreAuthorize` annotations for role-based access.
- **Interceptor**: The `AppHttpInterceptor` adds the JWT token to all HTTP requests (except `/auth/login`).

### Example: Security Configuration
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Value("${jwt.secret}")
    private String secretKey;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(ar -> ar.requestMatchers("/auth/login/**").permitAll())
                .authorizeHttpRequests(ar -> ar.anyRequest().authenticated())
                .oauth2ResourceServer(oa -> oa.jwt(Customizer.withDefaults()))
                .build();
    }
}
```

## API Endpoints
### Customer Endpoints
- `GET /customers`: List all customers (USER role).
- `GET /customers/{id}`: Get customer by ID (USER role).
- `POST /customers`: Create a new customer (ADMIN role).
- `PUT /customers/{id}`: Update a customer (ADMIN role).
- `DELETE /customers/{id}`: Delete a customer (ADMIN role).
- `GET /customers/search?keyword={kw}`: Search customers by name (USER role).

### Account Endpoints
- `GET /accounts/{accountId}`: Get account details (USER role).
- `GET /accounts`: List all accounts (USER role).
- `GET /accounts/{accountId}/operations`: Get account operation history (USER role).
- `GET /accounts/{accountId}/pageOperations?page={p}&size={s}`: Get paginated operation history (USER role).
- `POST /accounts/debit`: Debit an account (USER role).
- `POST /accounts/credit`: Credit an account (USER role).
- `POST /accounts/transfer`: Transfer between accounts (USER role).

### Example: Account Operation DTO
```java
@Data
public class AccountOperationDTO {
    private Long id;
    private Date operationDate;
    private double amount;
    private String description;
    private OperationType type;
}
```

## Frontend Components
### Accounts Component
Handles account search and operations:
```typescript
export class AccountsComponent implements OnInit {
  accountFormGroup!: FormGroup;
  operationFormGroup!: FormGroup;
  accountObservable!: Observable<AccountDetails>;

  handleSearchAccount() {
    let accountId: string = this.accountFormGroup.value.accountId;
    this.accountObservable = this.accountService.getAccount(accountId, this.currentPage, this.pageSize).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }
}
```

### Customers Component
Manages customer listing and actions:
```typescript
export class CustomersComponent implements OnInit {
  customers!: Observable<Array<Customer>>;
  searchFormGroup: FormGroup | undefined;

  handleSearchCustomers() {
    let kw = this.searchFormGroup?.value.keyword;
    this.customers = this.customerService.serachCustomers(kw).pipe(
      catchError(err => {
        this.errorMessage = err.message;
        return throwError(err);
      })
    );
  }
}
```

## Database Schema
The application uses a **single-table inheritance strategy** for bank accounts:
- **Customer**: Stores customer details (id, name, email).
- **BankAccount**: Abstract entity with subclasses `CurrentAccount` and `SavingAccount` (stored in a single table with a `TYPE` discriminator column).
- **AccountOperation**: Stores transaction details linked to a bank account.

### Example: BankAccount Entity
```java
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 4, discriminatorType = DiscriminatorType.STRING)
@Data
@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class BankAccount {
    @Id
    private String id;
    private double balance;
    private Date createdAt;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY)
    private List<AccountOperation> accountOperations;
    @ManyToOne
    private Customer customer;
}
```

## Testing
- **Backend**: Use Postman to test API endpoints. Start with `/auth/login` to obtain a JWT token, then include it in the `Authorization` header as `Bearer <token>`.
- **Frontend**: Access the application at `http://localhost:4200` and log in with the provided credentials.
- **Unit Tests**: Add JUnit tests for backend services and repositories (not provided in the code but recommended).
- **E2E Tests**: Use Angular's testing tools like Cypress or Protractor for end-to-end testing.

## Known Issues
- **CORS**: Ensure the backend allows requests from `http://localhost:4200` (configured in `SecurityConfig`).
- **Pagination**: Ensure the `page` and `size` parameters are correctly passed in account history requests.
- **Token Expiry**: JWT tokens expire after 10 minutes; users must re-authenticate after expiry.

## Future Improvements
- Add support for password reset and user registration.
- Implement audit logging for all operations.
- Enhance frontend with more interactive visualizations (e.g., charts for account balances).
- Add support for internationalization (i18n).
- Introduce email notifications for transactions.

