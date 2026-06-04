English | [简体中文](README.zh-CN.md)

# Spring Boot two-route spike

Run `./mvnw -q verify` and `./mvnw spring-boot:run`. `GET /events/concert-1/seats` returns two in-memory seats; `POST /orders` with `{"eventId":"concert-1","seatId":"A1"}` returns 201, then 409 for the same seat. The in-memory state resets at restart.

| Spring piece | Hand-written counterpart (raw HTTP server and blocking-load experiment) |
| --- | --- |
| `spring-boot-starter-webmvc` and embedded server | `RawHttpServer` accept loop, `ServerExecutor`, socket lifecycle |
| `spring.threads.virtual.enabled=true` | the `ServerExecutor.VIRTUAL` thread choice in the blocking-load experiment |
| `@SpringBootApplication` | `TicketingMain` startup and wiring |
| `@RestController`, `@GetMapping`, `@PostMapping`, `@PathVariable` | Request-line parsing and `TicketingApi.handle` route dispatch |
| `@RequestBody` and MVC message conversion | `Content-Length` framing and Jackson JSON mapping |
| `@ResponseStatus` and `ResponseStatusException` | Explicit `HttpReply` status codes |
| `@SpringBootTest` and `TestRestTemplate` | Live HTTP integration tests |

The team still must understand the servlet thread model, request and connection timeouts, and the default error response format before deployment. Spring Boot supplies defaults for all three; they are operational choices, not guarantees for this workload.

## License

This repository is licensed under the [MIT License](LICENSE).

## About this repository

This repository contains a focused Stubline experiment. The README documents its commands and observed results.

The code and documents in this repository were written with the help of AI tools; every recorded output comes from actually running them.

## Findings

The experiment results and their effect on the backend decision are recorded in [FINDINGS.md](FINDINGS.md).
