# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Development Server
```bash
npm run serve
# or
yarn serve
```
Starts development server with hot-reload. Uses proxy configuration for API calls.

### Build
```bash
npm run build
# or
yarn build
```
Builds the application for production to the `dist` directory.

### Linting
```bash
npm run lint
# or
yarn lint
```
Runs ESLint to check and fix code style issues.

### Documentation
```bash
npm run docs:dev    # Start VuePress documentation server
npm run docs:build  # Build documentation
```

## Architecture Overview

This is a Vue 2 admin dashboard application built with the Ant Design Vue component library. It's based on the Vue Antd Admin template but customized for data service management.

### Key Technologies
- **Vue 2.6.11** - Main framework
- **Ant Design Vue 1.7.2** - UI component library
- **Vue Router 3.x** - Client-side routing with async route loading
- **Vuex 3.x** - State management
- **Vue CLI 4.x** - Build tooling
- **Less** - CSS preprocessor with theme customization
- **Viser-Vue** - Data visualization charts
- **Axios** - HTTP client with interceptors

### Project Structure

#### Core Directories
- `src/project/` - Main business logic and views
- `src/layouts/` - Layout components (AdminLayout, PageLayout, etc.)
- `src/components/` - Reusable UI components
- `src/router/` - Routing configuration with async loading
- `src/store/` - Vuex store modules
- `src/utils/` - Utility functions and helpers

#### Business Modules (src/project/views/)
- `dataManage/` - Data instance and source management
- `dataModelManage/` - Data model configuration
- `dbManage/` - Database management
- `labelSystem/` - Label model and type management  
- `management/` - Multi-level organization management
- `powerMgt/` - User permissions and role management
- `intelligentConfig/` - Smart configuration features
- `query-project/` - Project querying and topology visualization

### Router Configuration
- Routes are defined in `src/project/router/config.js`
- Supports async loading with `formatRoutes` utility
- Layout components handle different page structures
- Login bypass configuration in router index

### API Configuration
- Base API URL configured via environment variables
- Proxy setup in `vue.config.js` for development
- Two main API endpoints: `/baichuan/api` and `/maccdata/api`
- Request interceptors in `src/utils/axios-interceptors.js`

### Theme System
- Dynamic theme switching using `webpack-theme-color-replacer`
- Less variables in `src/theme/` directory
- Color utilities in `src/utils/themeUtil.js`

### Development Setup
- Environment variables in `.env` files
- Webpack aliases: `@` (src), `@p` (src/project), `@f` (src/frame)
- ESLint configuration with Vue plugin
- Babel with conditional console removal in production

## Key Configuration Files

- `vue.config.js` - Webpack configuration, proxy settings, theme setup
- `src/config/index.js` - Application configuration merging
- `src/project/config/appConfig.js` - Business app settings
- `.env` - Environment variables for API base URL and app name

## Mock Data
Mock API responses are configured in `src/mock/` for development and testing.

## Build Configuration
- Production builds to `dist/` directory
- Assets served from `static/` subdirectory
- Public path set to `/maccdata`
- Source maps disabled in production
- Optional gzip compression and CDN externals