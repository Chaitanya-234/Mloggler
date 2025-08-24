# Mblogger - Social Media Platform Backend

A comprehensive Spring Boot backend application for a social media platform with support for various content types, user interactions, and OpenID Connect authentication.

## Features

- **User Authentication & Authorization**: JWT-based authentication with OpenID Connect support
- **Content Management**: Support for text posts, blogs, reels, videos, images, and documents
- **User Interactions**: Like, comment, share, and follow/unfollow functionality
- **Real-time Notifications**: For user activities and interactions
- **Personalized Feeds**: Algorithm-based content recommendations
- **Search & Discovery**: Advanced search by content, users, hashtags, and tags
- **File Management**: Local and cloud storage support for media files
- **API Documentation**: Comprehensive Swagger/OpenAPI documentation

## Technology Stack

- **Framework**: Spring Boot 3.5.5
- **Database**: MySQL 8.0 with Hibernate/JPA
- **Security**: Spring Security with JWT and OAuth2/OIDC
- **Documentation**: OpenAPI 3 (Swagger UI)
- **Build Tool**: Maven
- **Java Version**: 17
- **Testing**: JUnit 5 + Mockito

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- MySQL 8.0+
- Git

## Quick Start

### 1. Clone the Repository
```bash
git clone <repository-url>
cd Mlogger
```

### 2. Environment Configuration
The application supports multiple environment profiles. See [Environment Setup Guide](ENVIRONMENT_SETUP.md) for detailed configuration.

#### For Local Development
```bash
# Run with local profile
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

#### For Development Server
```bash
# Set environment variables
export DB_USERNAME=your_dev_username
export DB_PASSWORD=your_dev_password
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret

# Run with dev profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 3. Database Setup
```sql
-- For local development
CREATE DATABASE mblogger_local;
USE mblogger_local;

-- For development server
CREATE DATABASE mblogger_dev;
USE mblogger_dev;
```

### 4. OIDC Provider Setup
Configure your OAuth2/OIDC providers (Google, GitHub, etc.) and update the credentials in the appropriate profile configuration.

### 5. Access the Application
- **API Base URL**: http://localhost:8080/api
- **Swagger UI**: http://localhost:8080/api/swagger-ui.html
- **API Docs**: http://localhost:8080/api/api-docs

## API Endpoints

### Authentication
- `POST /api/auth/register` - User registration
- `POST /api/auth/login` - User login
- `POST /api/auth/logout` - User logout
- `GET /api/auth/oidc/success` - OIDC authentication success

### Users
- `GET /api/users/{id}` - Get user by ID
- `GET /api/users/username/{username}` - Get user by username
- `GET /api/users/search` - Search users
- `POST /api/users/follow/{id}` - Follow user
- `POST /api/users/unfollow/{id}` - Unfollow user

### Posts
- `POST /api/posts` - Create new post
- `GET /api/posts/{id}` - Get post by ID
- `GET /api/posts/user/{userId}` - Get user posts
- `GET /api/posts/feed` - Get personalized feed
- `GET /api/posts/trending` - Get trending posts
- `GET /api/posts/search` - Search posts

### Comments
- `POST /api/comments` - Create comment
- `GET /api/comments/post/{postId}` - Get post comments

### Likes
- `POST /api/posts/{postId}/like` - Like post
- `DELETE /api/posts/{postId}/like` - Unlike post
- `GET /api/posts/{postId}/likes/count` - Get likes count
- `GET /api/posts/{postId}/liked` - Check if liked

### Notifications
- `GET /api/notifications` - Get user notifications
- `GET /api/notifications/unread/count` - Get unread count
- `PUT /api/notifications/{id}/read` - Mark as read
- `PUT /api/notifications/read-all` - Mark all as read

## Database Schema

The application uses the following main entities:
- **User**: User accounts and profiles
- **Post**: Content posts with various types
- **MediaFile**: File metadata and storage information
- **Blog**: Rich text content for blog posts
- **Comment**: User comments on posts
- **Like**: User likes on posts
- **Notification**: User notifications
- **Follow**: User following relationships

## Testing

```bash
# Run all tests
mvn test

# Run tests with specific profile
mvn test -Dspring.profiles.active=test

# Run specific test class
mvn test -Dtest=UserServiceTest
```

## Development Guidelines

- Follow Spring Boot best practices
- Use DTOs for API communication
- Implement proper validation and error handling
- Write comprehensive unit tests
- Follow RESTful API design principles
- Use appropriate HTTP status codes
- Implement proper logging and monitoring

## Deployment

### Local Development
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```

### Development Server
```bash
mvn clean package -Dspring.profiles.active=dev
java -jar -Dspring.profiles.active=dev target/Mlogger-0.0.1-SNAPSHOT.jar
```

### Production
```bash
mvn clean package -Dspring.profiles.active=prod
java -jar -Dspring.profiles.active=prod target/Mlogger-0.0.1-SNAPSHOT.jar
```

## Environment Variables

Key environment variables for production deployment:
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `JWT_SECRET`: JWT signing secret
- `GOOGLE_CLIENT_ID`: Google OAuth2 client ID
- `GOOGLE_CLIENT_SECRET`: Google OAuth2 client secret
- `GITHUB_CLIENT_ID`: GitHub OAuth2 client ID
- `GITHUB_CLIENT_SECRET`: GitHub OAuth2 client secret

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests for new functionality
5. Ensure all tests pass
6. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support and questions, please open an issue in the repository or contact the development team.
