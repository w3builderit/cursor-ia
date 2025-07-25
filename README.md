# User Manager Microservice

A comprehensive user management microservice built with **Java 21**, **Spring Boot 3.2.8**, **Keycloak**, and **PostgreSQL**. This service provides role-based access control (RBAC) for managing users, roles, permissions, menus, screens, papers/documents, and user profiles.

## 🚀 Features

### Core Features
- **User Management**: Complete CRUD operations for users
- **Role-Based Access Control (RBAC)**: Hierarchical role and permission system
- **Keycloak Integration**: OAuth2/JWT authentication and authorization
- **Multi-tenancy Support**: User profiles with different contexts
- **Audit Trail**: Complete audit logging with created/updated timestamps

### Domain Management
- **Users**: Registration, profile management, status control
- **Roles**: System and custom roles with permissions
- **Permissions**: Granular access control for resources and actions
- **Menus**: Hierarchical menu structure with access control
- **Screens**: UI component access management
- **Papers/Documents**: Document management with versioning and access control
- **User Profiles**: Multiple user profiles per user for different contexts

### Advanced Features
- **Search & Filtering**: Advanced search capabilities across all entities
- **Pagination**: Efficient data pagination with sorting
- **Soft Delete**: Logical deletion with data retention
- **Caching**: Application-level caching for better performance
- **Health Checks**: Comprehensive health monitoring
- **API Documentation**: OpenAPI 3.0 documentation with Swagger UI

## 🏗️ Architecture

### Technology Stack
- **Java 21** - Latest LTS version with modern language features
- **Spring Boot 3.2.8** - Enterprise application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Data access layer with Hibernate
- **PostgreSQL** - Relational database
- **Keycloak** - Identity and access management
- **Flyway** - Database migration
- **MapStruct** - Object mapping
- **Docker & Docker Compose** - Containerization

### Design Patterns
- **Clean Architecture** - Separation of concerns with layered architecture
- **Repository Pattern** - Data access abstraction
- **DTO Pattern** - Data transfer objects for API layer
- **Builder Pattern** - Object construction
- **Strategy Pattern** - Pluggable business logic

## 📋 Prerequisites

- **Java 21** or higher
- **Maven 3.6+**
- **Docker & Docker Compose**
- **Git**

## 🚀 Quick Start

### 1. Clone the Repository
```bash
git clone https://github.com/your-org/user-manager-microservice.git
cd user-manager-microservice
```

### 2. Start with Docker Compose
```bash
# Start all services (PostgreSQL, Keycloak, Application)
docker-compose up -d

# Check service status
docker-compose ps

# View logs
docker-compose logs -f user-manager-app
```

### 3. Access the Services

| Service | URL | Credentials |
|---------|-----|-------------|
| User Manager API | http://localhost:8081/api/v1 | JWT Token required |
| Swagger UI | http://localhost:8081/api/v1/swagger-ui.html | JWT Token required |
| Keycloak Admin | http://localhost:8080 | admin/admin |
| Adminer (DB Admin) | http://localhost:8082 | Server: postgres, User: usermanager, Password: password |

## 🔧 Local Development

### 1. Start Dependencies
```bash
# Start only PostgreSQL and Keycloak
docker-compose up -d postgres keycloak
```

### 2. Run Application Locally
```bash
# Set environment variables
export SPRING_PROFILES_ACTIVE=dev
export DB_HOST=localhost
export KEYCLOAK_ISSUER_URI=http://localhost:8080/realms/usermanager

# Run application
./mvnw spring-boot:run
```

### 3. Run Tests
```bash
# Unit tests
./mvnw test

# Integration tests
./mvnw verify
```

## 🔐 Keycloak Setup

### 1. Access Keycloak Admin Console
- URL: http://localhost:8080
- Username: `admin`
- Password: `admin`

### 2. Create Realm
1. Click "Create Realm"
2. Name: `usermanager`
3. Click "Create"

### 3. Create Client
1. Go to "Clients" → "Create client"
2. Client ID: `user-manager-client`
3. Client type: `OpenID Connect`
4. Click "Next"
5. Enable "Client authentication"
6. Enable "Authorization"
7. Valid redirect URIs: `http://localhost:8081/*`
8. Click "Save"

### 4. Create Roles
Create the following realm roles:
- `ADMIN`
- `USER_MANAGER`
- `ROLE_MANAGER`
- `USER_VIEWER`
- `USER`

### 5. Create Test User
1. Go to "Users" → "Create new user"
2. Username: `testuser`
3. Enable user
4. Set password in "Credentials" tab
5. Assign roles in "Role mapping" tab

## 📚 API Documentation

### Authentication
All endpoints (except health checks) require JWT authentication. Obtain a token from Keycloak:

```bash
curl -X POST "http://localhost:8080/realms/usermanager/protocol/openid-connect/token" \
  -H "Content-Type: application/x-www-form-urlencoded" \
  -d "grant_type=password" \
  -d "client_id=user-manager-client" \
  -d "client_secret=your-client-secret" \
  -d "username=testuser" \
  -d "password=testpassword"
```

### Key Endpoints

#### User Management
- `GET /api/v1/users` - List users (paginated)
- `POST /api/v1/users` - Create user
- `GET /api/v1/users/{id}` - Get user by ID
- `PUT /api/v1/users/{id}` - Update user
- `DELETE /api/v1/users/{id}` - Delete user
- `GET /api/v1/users/me` - Get current user profile
- `PUT /api/v1/users/me` - Update current user profile

#### User Operations
- `POST /api/v1/users/{id}/activate` - Activate user
- `POST /api/v1/users/{id}/deactivate` - Deactivate user
- `POST /api/v1/users/{id}/lock` - Lock user
- `POST /api/v1/users/{id}/unlock` - Unlock user
- `POST /api/v1/users/{id}/verify-email` - Verify email

#### Role Management
- `POST /api/v1/users/{userId}/roles/{roleId}` - Assign role
- `DELETE /api/v1/users/{userId}/roles/{roleId}` - Remove role
- `POST /api/v1/users/{userId}/roles` - Assign multiple roles

#### Search & Filtering
- `GET /api/v1/users/search?q={term}` - Search users
- `GET /api/v1/users/status/{status}` - Filter by status
- `GET /api/v1/users/department/{dept}` - Filter by department
- `GET /api/v1/users/role/{roleCode}` - Filter by role

#### Statistics & Monitoring
- `GET /api/v1/users/stats/count-by-status` - User count by status
- `GET /api/v1/users/stats/count-by-department` - User count by department
- `GET /api/v1/users/maintenance/inactive` - Get inactive users
- `GET /api/v1/users/maintenance/locked` - Get locked users

### Example API Calls

```bash
# Get all users (with JWT token)
curl -H "Authorization: Bearer $JWT_TOKEN" \
  "http://localhost:8081/api/v1/users?page=0&size=10"

# Create a new user
curl -X POST \
  -H "Authorization: Bearer $JWT_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "username": "newuser",
    "email": "newuser@example.com",
    "firstName": "New",
    "lastName": "User",
    "department": "IT"
  }' \
  "http://localhost:8081/api/v1/users"

# Search users
curl -H "Authorization: Bearer $JWT_TOKEN" \
  "http://localhost:8081/api/v1/users/search?q=john&page=0&size=10"
```

## 🗄️ Database Schema

### Core Tables
- `users` - User information
- `roles` - System and custom roles
- `permissions` - Granular permissions
- `user_roles` - User-role relationships
- `role_permissions` - Role-permission relationships

### Domain Tables
- `menus` - Hierarchical menu structure
- `screens` - UI screens and components
- `papers` - Documents/papers with versioning
- `user_profiles` - User profiles with contexts

### Audit & Metadata
All tables include:
- `id` (UUID primary key)
- `created_at` (timestamp)
- `updated_at` (timestamp)
- `version` (optimistic locking)
- `active` (soft delete flag)

## 🔒 Security Features

### Authentication
- **JWT Tokens** via Keycloak
- **OAuth2 Resource Server** configuration
- **Role-based authentication** with Spring Security

### Authorization
- **Method-level security** with `@PreAuthorize`
- **Hierarchical roles** support
- **Resource-based permissions**
- **Context-aware authorization**

### Data Protection
- **Soft deletes** for data retention
- **Optimistic locking** for concurrent updates
- **Input validation** with Bean Validation
- **SQL injection prevention** with JPA

## 📊 Monitoring & Observability

### Health Checks
- `GET /api/v1/actuator/health` - Application health
- `GET /api/v1/actuator/info` - Application info
- Database connectivity checks
- Keycloak connectivity checks

### Metrics
- **Prometheus metrics** endpoint
- **JVM metrics** (memory, threads, GC)
- **Database connection pool** metrics
- **HTTP request** metrics

### Logging
- **Structured logging** with JSON format
- **Correlation IDs** for request tracing
- **Security events** logging
- **Audit trail** for data changes

## 🚀 Deployment

### Production Environment Variables
```bash
# Database
DB_HOST=your-postgres-host
DB_PORT=5432
DB_NAME=usermanager
DB_USERNAME=usermanager
DB_PASSWORD=secure-password

# Keycloak
KEYCLOAK_ISSUER_URI=https://your-keycloak-domain/realms/usermanager
KEYCLOAK_JWK_SET_URI=https://your-keycloak-domain/realms/usermanager/protocol/openid-connect/certs

# Application
SPRING_PROFILES_ACTIVE=prod
SERVER_PORT=8081
CORS_ALLOWED_ORIGINS=https://your-frontend-domain

# Security
JWT_ACCESS_TOKEN_VALIDITY=3600
JWT_REFRESH_TOKEN_VALIDITY=86400
```

### Docker Production Build
```bash
# Build production image
docker build -t user-manager:latest .

# Run with production profile
docker run -d \
  -p 8081:8081 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e DB_HOST=your-postgres-host \
  -e KEYCLOAK_ISSUER_URI=https://your-keycloak-domain/realms/usermanager \
  user-manager:latest
```

## 🧪 Testing

### Unit Tests
```bash
./mvnw test
```

### Integration Tests
```bash
./mvnw verify
```

### API Testing with Postman
Import the Postman collection from `docs/postman/` directory.

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/new-feature`
3. Make changes and commit: `git commit -am 'Add new feature'`
4. Push to branch: `git push origin feature/new-feature`
5. Create a Pull Request

### Development Guidelines
- Follow Java coding standards
- Write unit tests for new features
- Update documentation for API changes
- Use conventional commits for commit messages

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🆘 Troubleshooting

### Common Issues

1. **Application won't start**
   - Check if PostgreSQL is running: `docker-compose ps`
   - Verify database connectivity
   - Check application logs: `docker-compose logs user-manager-app`

2. **Authentication fails**
   - Verify Keycloak is running and accessible
   - Check realm and client configuration
   - Validate JWT token format

3. **Database connection errors**
   - Verify PostgreSQL credentials
   - Check database name and host
   - Ensure database is initialized

4. **Permission denied errors**
   - Check user roles in Keycloak
   - Verify JWT token contains correct roles
   - Review Spring Security configuration

### Support
For issues and questions:
- Create an issue on GitHub
- Check existing documentation
- Review application logs

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Keycloak Documentation](https://www.keycloak.org/documentation)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Docker Documentation](https://docs.docker.com/)