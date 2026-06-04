package com.elsworne.stubline.spike.springboot;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.TestRestTemplate;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestRestTemplate
class TicketingControllerTest {
  @Autowired TestRestTemplate http;

  @Test
  void seatsAndReservation() {
    var before = http.getForEntity("/events/concert-1/seats", TicketingController.Seat[].class);
    assertEquals(2, before.getBody().length);
    assertTrue(before.getBody()[0].available());
    var request = new TicketingController.OrderRequest("concert-1", "A1");
    assertEquals(
        HttpStatus.CREATED, http.postForEntity("/orders", request, String.class).getStatusCode());
    assertEquals(
        HttpStatus.CONFLICT, http.postForEntity("/orders", request, String.class).getStatusCode());
    assertFalse(
        http.getForEntity("/events/concert-1/seats", TicketingController.Seat[].class)
            .getBody()[0]
            .available());
  }

  @Test
  void invalidOrderIsBadRequest() {
    assertEquals(
        HttpStatus.BAD_REQUEST,
        http.postForEntity("/orders", new TicketingController.OrderRequest("", "A2"), String.class)
            .getStatusCode());
  }
}
