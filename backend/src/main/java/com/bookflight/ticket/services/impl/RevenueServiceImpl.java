package com.bookflight.ticket.services.impl;

import com.bookflight.ticket.dto.DashboardSummary;
import com.bookflight.ticket.dto.response.MonthlyRevenueResponse;
import com.bookflight.ticket.dto.response.RevenueResponse;
import com.bookflight.ticket.dto.response.YearlyRevenueResponse;
import com.bookflight.ticket.models.RevenueEntity;
import com.bookflight.ticket.repositories.*;
import com.bookflight.ticket.services.RevenueService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class RevenueServiceImpl implements RevenueService {
    @Autowired
    private RevenueRepository revenueRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AirlineRepository airlineRepository;

    @Autowired
    private AirportRepository airportRepository;

    @Autowired
    private PlaneRepository planeRepository;

    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<RevenueResponse> getRevenueByDate() {
        LocalDate date = LocalDate.now();
        List<RevenueEntity> revenueList = revenueRepository.findAllByDate(date);
        List<RevenueResponse> revenueResponseList = new ArrayList<>();
        for (RevenueEntity revenueEntity : revenueList) {
            RevenueResponse revenueResponse = modelMapper.map(revenueEntity, RevenueResponse.class);
            revenueResponseList.add(revenueResponse);
        }
        return revenueResponseList;
    }

    @Override
    public YearlyRevenueResponse getRevenueByYear(Integer year) {
        List<MonthlyRevenueResponse> monthlyRevenues = revenueRepository.getMonthlyRevenueByYear(year);

        BigDecimal totalYearlyRevenue = monthlyRevenues.stream()
                .map(MonthlyRevenueResponse::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        YearlyRevenueResponse response = YearlyRevenueResponse.builder()
                .year(year)
                .totalRevenue(totalYearlyRevenue)
                .monthlyRevenues(monthlyRevenues)
                .build();
        return response;
    }

    @Override
    public DashboardSummary getDashboardSummary() {
        long totalUsers = userRepository.count();
        long totalAirlines = airlineRepository.count();
        long totalAirports = airportRepository.count();
        long totalPlanes = planeRepository.count();
        long totalFlights = flightRepository.count();
        DashboardSummary dashboardSummary = new DashboardSummary();
        dashboardSummary.setTotalUsers(totalUsers);
        dashboardSummary.setTotalAirlines(totalAirlines);
        dashboardSummary.setTotalAirports(totalAirports);
        dashboardSummary.setTotalPlanes(totalPlanes);
        dashboardSummary.setTotalFlights(totalFlights);
        return dashboardSummary;
    }
}
