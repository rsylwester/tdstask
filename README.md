# Car Park API

In-memory Spring Boot API that parks vehicles, reports free spaces and bills on exit.

## Requirements

- Java 25
- Maven (or the bundled `./mvnw`)

## Build and run

```bash
./mvnw clean package          # compile and run tests
./mvnw spring-boot:run        # start on http://localhost:8080
```

## Parking size

Set in `src/main/resources/application.yml` (default 20):

```yaml
parking:
  size: 20
```

## Sample requests

```bash
curl localhost:8080/parking
# {"availableSpaces":20,"occupiedSpaces":0}

curl -X POST localhost:8080/parking -H 'Content-Type: application/json' \
     -d '{"vehicleReg":"XYZ1234","vehicleType":1}'
# {"vehicleReg":"XYZ1234","timeIn":"2026-11-24T09:11:57.309717Z"}

curl -X POST localhost:8080/parking/bill -H 'Content-Type: application/json' \
     -d '{"vehicleReg":"XYZ1234"}'
# {"vehicleReg":"XYZ1234","vehicleCharge":0.0,"timeIn":"2026-11-24T09:11:57.309717Z","timeOut":"2026-11-24T09:11:57.356443Z"}
```

Errors: `409` car park full or vehicle already parked, `404` bill for a vehicle not parked, `400` vehicle type not 1, 2 or 3.

## Assumptions

- `vehicleReg` is valid.
- Only whole minutes are billed - a stay under one minute is free.
- `POST /parking/bill` is the vehicle exit - it charges and frees the space in one call.
- A vehicle can be parked only once at a time.

## Things to improve

- Exception handling - a global `@RestControllerAdvice` with error bodies instead of empty responses.
- Concurrency - parallel requests can take the same space or bill a vehicle twice. Fix with `synchronized` service methods,
  or for high traffic a lock per parking space plus a `ConcurrentHashMap` of reg to space, covered by a multi-threaded test.
- Time-based test - no test covers a real charge over time; an injected `Clock` would allow it.
