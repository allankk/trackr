<div align="center">
    <img src="frontend/src/assets/trackr-logo-transp.png" alt="Trackr Logo" />
</div>

<br />

<div align="center" style="margin-top: 30px">
App available at <a href="https://trackr.allank.ee">trackr.allank.ee</a>
</div>

<br />

<div align="center" style="margin-top: 20px">
    Trackr is a fitness and activity tracker application where users can log their workouts, track progress, and visualize data.
</div>

## Technologies

- **Frontend**: Vue.js
- **Backend**: Spring Boot
- **Database**: PostgreSQL

## Dependencies

- NPM v10.8
- Node v22.3.0
- Apache Maven 3.6.3
- JDK 17.0.2

## Deployment

### 1. Deploy locally with Docker Compose

```
docker-compose -f docker-compose.app.yml up --build
```

Access the app at [localhost:8098](http://localhost:8098)

### 2. Deploy manually (Local)

In the root directory
```
// Start the database
docker-compose up -d
// Compile
mvn clean install
```

In the `/backend` directory:
```
mvn spring-boot:run
```

In the `/frontend` directory:
```
cp .env.example .env
npm run serve
```


Access the app at [localhost:8080](http://localhost:8080)

### 3. Deploy with Dockerfile

You need to assign values to the following environment variables in your deployment environment (or set them manually in the Dockerfile):

```
SPRING_DATASOURCE_URL
SPRING_DATASOURCE_USERNAME
SPRING_DATASOURCE_PASSWORD
VUE_APP_BACKEND_API_URL (set to /api/ route)
```


