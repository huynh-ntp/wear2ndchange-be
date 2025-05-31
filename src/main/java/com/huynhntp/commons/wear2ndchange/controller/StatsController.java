package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.enums.ProductAndOrderStatusEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderStatsDto;
import com.huynhntp.commons.wear2ndchange.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/stats")
@AllArgsConstructor
public class StatsController {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;

    @GetMapping("/total-amount-orders")
    public OrderStatsDto getOrderStats() {
        Double total = orderRepository.findTotalAmountSince();
        return new OrderStatsDto(total != null ? total : 0.0);
    }

    @GetMapping("/total-order")
    public Map<String, Long> countAllOrders() {
        return Map.of("totalOrder", orderRepository.count());
    }

    @GetMapping("/total-orders-delivering")
    public Map<String, Integer> getOrderStatsTotal() {
        Map<String, Integer> result = new HashMap<>();
        Integer totalItems = orderRepository.countByStatus(ProductAndOrderStatusEnum.DELIVERING.toString());
        result.put("total-order", totalItems);
        return result;
    }

    @GetMapping("/order-periodic")
    public Map<String, Double> getWeeklyStats(@RequestParam(defaultValue = "7") int days) {
        LocalDateTime fromTime = LocalDateTime.now().minusDays(days);
        Map<String, Double> result = new LinkedHashMap<>();

        if (days <= 7) {
            Map<Integer, String> dayNames = Map.of(
                    0, "Sunday", 1, "Monday", 2, "Tuesday", 3, "Wednesday",
                    4, "Thursday", 5, "Friday", 6, "Saturday"
            );

            // Initialize all days to 0
            dayNames.values().forEach(day -> result.put(day, 0.0));

            List<Object[]> stats = orderRepository.findDailyStats(fromTime);
            for (Object[] row : stats) {
                Integer day = row[0] != null ? ((Number) row[0]).intValue() : null;
                Double amount = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

                String dayName = dayNames.get(day);
                if (dayName != null) {
                    result.put(dayName, amount);
                } else {
                    System.err.println("Unexpected day value from DB: " + day); // Optional debug log
                }
            }

        } else {
            Map<Integer, String> monthNames = Map.ofEntries(
                    Map.entry(1, "January"), Map.entry(2, "February"),
                    Map.entry(3, "March"), Map.entry(4, "April"),
                    Map.entry(5, "May"), Map.entry(6, "June"),
                    Map.entry(7, "July"), Map.entry(8, "August"),
                    Map.entry(9, "September"), Map.entry(10, "October"),
                    Map.entry(11, "November"), Map.entry(12, "December")
            );

            // Initialize all months to 0
            monthNames.values().forEach(month -> result.put(month, 0.0));

            List<Object[]> stats = orderRepository.findMonthlyStats(fromTime);
            for (Object[] row : stats) {
                Integer month = row[0] != null ? ((Number) row[0]).intValue() : null;
                Double amount = row[1] != null ? ((Number) row[1]).doubleValue() : 0.0;

                String monthName = monthNames.get(month);
                if (monthName != null) {
                    result.put(monthName, amount);
                } else {
                    System.err.println("Unexpected month value from DB: " + month); // Optional debug log
                }
            }
        }

        return result;
    }

    @GetMapping("/total-user")
    public Map<String, Long> countAllUsers() {
        return Map.of("totalUser", accountRepository.countByRole("USER"));
    }

}
