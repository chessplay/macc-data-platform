# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Backend (Java/Gradle)

```bash
cd data-service-web-backend
./gradlew build               # Build all modules
./gradlew clean build         # Clean and build
./gradlew bootRun            # Run Spring Boot application
./gradlew test               # Run tests
```

The backend uses Gradle multi-module project structure with wrapper included.

### Frontend (Vue.js)

```bash
cd data-service-web-front
npm install                  # Install dependencies
npm run serve                # Development server with hot-reload
npm run build                # Production build
npm run lint                 # ESLint checking and fixes
```

## Architecture Overview

This is a full-stack data platform application consisting of a Java Spring Boot backend and Vue.js frontend for managing data sources, tasks, and user permissions.

### Backend Architecture

**Technology Stack:**
- **Spring Boot 2.7.12** with Java 8
- **MyBatis Plus** for database ORM
- **Multi-database support**: MySQL, Phoenix, ClickHouse, MaxCompute
- **Redis** for caching and session management
- **Elasticsearch** for search capabilities

**Module Structure:**
- `macc-data-platform/` - Main application module with business logic
- `dc-privilege/` - User authentication and permission management
- `dc-core/` - Common utilities and base classes

**Key Backend Components:**
- Controllers in `com.ruijie.cloud.macc.dataplatform.controller`
- Service layer with business logic
- Multi-datasource configuration via dynamic-datasource-spring-boot-starter
- Metadata management for data sources and table schemas
- Task template and instance management system

### Frontend Architecture

Based on Vue Antd Admin template with the following structure:

**Key Technologies:**
- **Vue 2.6.11** with Ant Design Vue 1.7.2
- **Vue Router 3.x** with async route loading
- **Vuex 3.x** for state management
- **Viser-Vue** for data visualization
- **Less** for styling with theme support

**Business Modules:**
- `dataManage/` - Data source and instance management
- `dataModelManage/` - Data model configuration
- `labelSystem/` - Label model and type management
- `powerMgt/` - User permissions and role management
- `query-project/` - Project querying with topology visualization

### Database Integration

The platform supports multiple database types:
- MySQL for application data
- Phoenix for HBase queries
- ClickHouse for analytics
- MaxCompute (Alibaba Cloud) for big data processing

### API Structure

Two main API endpoints:
- `/baichuan/api` - Core platform APIs
- `/maccdata/api` - Data management APIs

Frontend uses proxy configuration in `vue.config.js` for development API routing.

## Configuration Files

### Backend Configuration
- `application.yaml` - Main Spring Boot configuration
- `application-{env}.yaml` - Environment-specific settings (dev, test, idc)
- `build.gradle` - Gradle build configuration with multi-module setup
- `dependency.gradle` - Shared dependency definitions

### Frontend Configuration  
- `vue.config.js` - Webpack configuration and proxy settings
- `src/project/config/appConfig.js` - Application-specific settings
- Environment variables for API endpoints and build settings

## Multi-Module Gradle Project

The backend consists of several modules defined in `settings.gradle`:
- `dc-core` - Base utilities and common functionality
- `dc-privilege` - Authentication and authorization
- `macc-data-platform` - Main application module

Each module has its own `build.gradle` with specific dependencies and configuration.