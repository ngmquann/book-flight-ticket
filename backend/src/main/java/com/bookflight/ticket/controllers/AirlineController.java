package com.bookflight.ticket.controllers;

import com.bookflight.ticket.dto.request.AirlineRequest;
import com.bookflight.ticket.services.AirlineService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin/airline")
public class AirlineController {
    @Autowired
    private AirlineService airlineService;

    @PostMapping("/create")
    public ResponseEntity<?> createAirline(@ModelAttribute @Valid AirlineRequest airlineRequest, BindingResult result) throws IOException {
        try {
            if (result.hasErrors()) {
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .collect(Collectors.toList());
                return ResponseEntity.badRequest().body(errorMessages);
            }
            airlineService.createAirline(airlineRequest);
            return ResponseEntity.ok("Airline created successfully");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAirline(@PathVariable Long id){
        try {
            if(airlineService.deleteAirline(id)){
                return ResponseEntity.ok("Airline deleted successfully");
            }
            return ResponseEntity.badRequest().body("Airline not found");
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
