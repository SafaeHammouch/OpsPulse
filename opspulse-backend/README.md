# OpsPulse backend

This is the first OpsPulse application. It stores services to monitor, calls their health URLs on request, and saves every result in PostgreSQL.

## What it provides

- `POST /api/services` — register a health URL.
- `GET /api/services` — list registered services.
- `POST /api/services/{id}/check` — perform and save one health check.
- `GET /api/services/{id}/health-results` — view saved checks, newest first.

The backend runs on port `8080`. The demo Payment Service runs separately on port `8081`.

## Prerequisites

- JDK 21
- Maven 3.9+
- PostgreSQL 16+ running locally

Create a local database and role with the defaults used by `application.yml`:

```sql
CREATE USER opspulse WITH PASSWORD 'opspulse';
CREATE DATABASE opspulse OWNER opspulse;
```

Alternatively, set `OPSPULSE_DB_URL`, `OPSPULSE_DB_USERNAME`, and `OPSPULSE_DB_PASSWORD` before starting the application.

## Run

```bash
mvn spring-boot:run
```

Check that this backend itself is running:

```bash
curl http://localhost:8080/actuator/health
```

## First complete flow

Start `demo-payment-service` on port `8081`, then register it with OpsPulse:

```bash
curl -X POST http://localhost:8080/api/services \
  -H 'Content-Type: application/json' \
  -d '{
    "name": "Payment Service",
    "healthUrl": "http://localhost:8081/actuator/health",
    "criticality": "CRITICAL",
    "checkIntervalSeconds": 30
  }'
```

Ask OpsPulse to check it:

```bash
curl -X POST http://localhost:8080/api/services/1/check
```

Set the demo service down and check it again:

```bash
curl -X POST http://localhost:8081/demo/down
curl -X POST http://localhost:8080/api/services/1/check
curl http://localhost:8080/api/services/1/health-results
```

The second result should have `"status":"DOWN"`. If the Payment Service is stopped entirely, OpsPulse records `"status":"UNREACHABLE"` rather than crashing.

## Not included yet

Automatic scheduling and incident creation intentionally come after the manual workflow is verified.
