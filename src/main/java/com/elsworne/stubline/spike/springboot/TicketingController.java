package com.elsworne.stubline.spike.springboot;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/** Implements the seat and order routes from the raw HTTP experiment with Spring MVC. */
@RestController
@RequestMapping
public class TicketingController {
  private final Set<String> reserved = ConcurrentHashMap.newKeySet();

  public record Seat(String id, String eventId, boolean available) {}

  public record OrderRequest(String eventId, String seatId) {}

  public record OrderResponse(String id, String eventId, String seatId, Instant createdAt) {}

  @GetMapping("/events/{id}/seats")
  public List<Seat> seats(@PathVariable String id) {
    if (!id.equals("concert-1")) return List.of();
    return List.of(
        new Seat("A1", id, !reserved.contains("A1")), new Seat("A2", id, !reserved.contains("A2")));
  }

  @PostMapping("/orders")
  @org.springframework.web.bind.annotation.ResponseStatus(HttpStatus.CREATED)
  public OrderResponse order(@RequestBody OrderRequest request) {
    if (request == null
        || request.eventId() == null
        || request.seatId() == null
        || request.eventId().isBlank()
        || request.seatId().isBlank())
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "eventId and seatId are required");
    if (!request.eventId().equals("concert-1")
        || !Set.of("A1", "A2").contains(request.seatId())
        || !reserved.add(request.seatId()))
      throw new ResponseStatusException(HttpStatus.CONFLICT, "seat unavailable");
    return new OrderResponse(
        UUID.randomUUID().toString(), request.eventId(), request.seatId(), Instant.now());
  }
}
