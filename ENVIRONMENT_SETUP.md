# Environment Configuration Guide

This document explains how to configure and use different environment profiles for the Mblogger application.

## Available Profiles

### 1. Default Profile (No profile specified)
- Uses `application.properties` as the base configuration
- Suitable for production-like environments
- Requires all environment variables to be set

### 2. Local Profile (`local`)
- Uses `application-local.properties` for local development
- Database: `mblogger_local` on localhost
- Enhanced logging and debugging
- Dummy OIDC credentials for development

### 3. Development Profile (`dev`)
- Uses `application-dev.properties` for development servers
- Database: `mblogger_dev` on dev server
- Environment variable-based configuration
- Actuator endpoints enabled

### 4. Test Profile (`test`)
- Uses `application-test.properties` for testing
- In-memory H2 database
- Test-specific OIDC credentials
- H2 console enabled

## How to Use Profiles

### Running with Local Profile
```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=local

# Using Java
java -jar -Dspring.profiles.active=local mlogger.jar

# Using environment variable
export SPRING_PROFILES_ACTIVE=local
mvn spring-boot:run
```

### Running with Development Profile
```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Using Java
java -jar -Dspring.profiles.active=dev mlogger.jar

# Using environment variable
export SPRING_PROFILES_ACTIVE=dev
mvn spring-boot:run
```

### Running Tests
```bash
# Tests automatically use the 'test' profile
mvn test

# Or explicitly specify
mvn test -Dspring.profiles.active=test
```

## Environment Variables

### For Development Profile
```bash
export DB_USERNAME=your_dev_username
export DB_PASSWORD=your_dev_password
export JWT_SECRET=your_dev_jwt_secret
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

### For Production
```bash
export SPRING_PROFILES_ACTIVE=prod
export DB_USERNAME=your_prod_username
export DB_PASSWORD=your_prod_password
export JWT_SECRET=your_prod_jwt_secret
export GOOGLE_CLIENT_ID=your_google_client_id
export GOOGLE_CLIENT_SECRET=your_google_client_secret
export GITHUB_CLIENT_ID=your_github_client_id
export GITHUB_CLIENT_SECRET=your_github_client_secret
```

## IDE Configuration

### IntelliJ IDEA
1. Go to Run/Debug Configurations
2. Add VM options: `-Dspring.profiles.active=local`
3. Or set Environment variables: `SPRING_PROFILES_ACTIVE=local`

### Eclipse
1. Right-click on your application
2. Run As → Run Configurations
3. Add VM arguments: `-Dspring.profiles.active=local`

### VS Code
1. Create `.vscode/launch.json`
2. Add VM args: `"-Dspring.profiles.active=local"`

## Database Setup

### Local Development
```sql
CREATE DATABASE mblogger_local;
USE mblogger_local;
```

### Development Server
```sql
CREATE DATABASE mblogger_dev;
USE mblogger_dev;
```

## OIDC Provider Setup

### Google OAuth2
1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project or select existing
3. Enable Google+ API
4. Go to Credentials → Create Credentials → OAuth 2.0 Client ID
5. Set authorized redirect URIs:
   - Local: `http://localhost:8080/api/login/oauth2/code/google`
   - Dev: `http://your-dev-server:8080/api/login/oauth2/code/google`

### GitHub OAuth2
1. Go to [GitHub Settings](https://github.com/settings/developers)
2. Click "New OAuth App"
3. Set Authorization callback URL:
   - Local: `http://localhost:8080/api/login/oauth2/code/github`
   - Dev: `http://your-dev-server:8080/api/login/oauth2/code/github`

## Security Considerations

### Local Profile
- Uses dummy OIDC credentials
- Not suitable for production
- Enhanced logging for debugging

### Development Profile
- Uses environment variables for sensitive data
- Moderate logging level
- Actuator endpoints enabled for monitoring

### Production Profile
- All sensitive data must be provided via environment variables
- Minimal logging
- Strict security settings
- No development tools enabled

## Troubleshooting

### Profile Not Loading
- Check if the profile is correctly specified
- Verify the properties file exists
- Check for syntax errors in properties files

### Database Connection Issues
- Verify database server is running
- Check database credentials
- Ensure database exists

### OIDC Authentication Issues
- Verify OIDC provider credentials
- Check redirect URIs configuration
- Ensure proper scopes are configured
