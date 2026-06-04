English | [简体中文](FINDINGS.zh-CN.md)

# Findings

## What we observed

The two-route Spring Boot app returns two in-memory seats for `GET /events/concert-1/seats`; posting the same seat twice to `/orders` returns 201 then 409. Its controller and tests show how the framework handles routing, request-body binding and response status. The in-memory state resets on restart. This spike contains no order-event producer or consumer.

## Backend decision

Spring Boot removes hand-written HTTP framing, routing and JSON binding from these routes. The backend still needs an explicit, versioned event schema for other services; this experiment does not test event compatibility or make that decision for the team. See [backend ADR 0007 — Order event encoding](https://github.com/codexary/stubline-backend/blob/main/docs/adr/0007-order-event-encoding.md).
