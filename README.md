# OpsPulse

This repository contains a small demo service and the first OpsPulse monitoring backend.

## Projects

- `demo-payment-service` — a Spring Boot service with a controllable health endpoint.
- `opspulse-backend` — registers health URLs, performs manual checks, and stores results in PostgreSQL.

## First milestone

Start `demo-payment-service`, switch it between healthy, slow, and down states,
then use `opspulse-backend` to register and check its Actuator health URL.

See [`demo-payment-service/README.md`](demo-payment-service/README.md) for setup and test instructions.
See [`opspulse-backend/README.md`](opspulse-backend/README.md) for the backend setup and complete manual monitoring flow.
