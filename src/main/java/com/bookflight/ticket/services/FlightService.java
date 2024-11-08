package com.bookflight.ticket.services;

import com.bookflight.ticket.dto.FlightDto;
import com.bookflight.ticket.dto.request.FlightRequest;
import com.bookflight.ticket.dto.response.FlightResponse;
import com.bookflight.ticket.dto.response.InfoSearchResponse;

import java.util.List;

public interface FlightService {
    void createFlight(FlightDto flightDto) throws Exception;
    FlightResponse getDetailFlight(Long id) throws Exception;
    InfoSearchResponse getInfoSearch();
    List<FlightResponse> searchFlights(FlightRequest flightRequest) throws Exception;
    List<FlightResponse> getAllFlights() throws Exception;
}
