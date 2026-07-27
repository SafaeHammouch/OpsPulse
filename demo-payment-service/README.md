# Demo Payment Service

This is the first service in the OpsPulse project. It imitates a payment API
and exposes a health endpoint that can be switched between demonstration modes.

## Prerequisites

- JDK 21
- Maven 3.9 or newer

Check them with:

```bash
java -version
mvn -version
```

## Run it

From this directory:

```bash
mvn spring-boot:run
```

The first Maven run downloads dependencies. When startup completes, the
service listens at `http://localhost:8081`.

## Try the health endpoint

```bash
curl http://localhost:8081/actuator/health
```

Expected initial response (details may also include other built-in indicators):

```json
{"status":"UP","components":{"demoPayment":{"status":"UP","details":{"mode":"HEALTHY"}}}}
```

## Change the mode

```bash
curl -X POST http://localhost:8081/demo/down
curl http://localhost:8081/actuator/health

curl -X POST http://localhost:8081/demo/healthy
curl http://localhost:8081/actuator/health

curl -X POST http://localhost:8081/demo/slow
curl http://localhost:8081/actuator/health
```

`DOWN` makes the overall Actuator health status `DOWN` (normally HTTP 503).
`SLOW` remains `UP`, but waits about two seconds before returning. That gives
OpsPulse a realistic response-time condition to measure.

## What each part teaches

- `DemoPaymentServiceApplication`: starts Spring Boot and its embedded web server.
- `DemoModeController`: maps the three `POST /demo/*` URLs to Java methods.
- `DemoHealthState`: a Spring-managed singleton storing the selected mode.
- `DemoPaymentHealthIndicator`: integrates our state with Actuator health.
- `application.yml`: external configuration; it selects port 8081 and exposes
  the health endpoint over HTTP.
