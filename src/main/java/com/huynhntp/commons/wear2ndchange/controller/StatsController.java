package com.huynhntp.commons.wear2ndchange.controller;

import com.huynhntp.commons.wear2ndchange.enums.ProductAndOrderStatusEnum;
import com.huynhntp.commons.wear2ndchange.model.dto.OrderStatsDto;
import com.huynhntp.commons.wear2ndchange.model.dto.StatDto;
import com.huynhntp.commons.wear2ndchange.repository.AccountRepository;
import com.huynhntp.commons.wear2ndchange.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public List<StatDto> getWeeklyStats(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) Integer year
            ) {
        List<StatDto> result = new ArrayList<>();

        if (date != null) {
            LocalDateTime fromTime = LocalDate.parse(date).atTime(LocalTime.MIN).minusDays(6);
            result.add(new StatDto(fromTime.toLocalDate().toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(1).toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(2).toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(3).toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(4).toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(5).toString(), 0.0));
            result.add(new StatDto(fromTime.toLocalDate().plusDays(6).toString(), 0.0));

            // Initialize all days to 0
//            dayNames.values().forEach(day -> result.put(day, 0.0));

            List<Object[]> stats = orderRepository.findDailyStats(fromTime);
            for (StatDto statDto : result) {
                Optional<Object[]> obt = stats.stream()
                        .filter(objects -> objects[0].toString().equals(statDto.getKey()))
                        .findFirst();
                obt.ifPresent(objects -> statDto.setValue((Double) objects[1]));
            }

            return result;

        } else {
            LocalDateTime fromTime = LocalDate.of(year,1,1).atTime(LocalTime.MIN);

            result.add(new StatDto("1", 0.0));
            result.add(new StatDto("2", 0.0));
            result.add(new StatDto("3", 0.0));
            result.add(new StatDto("4", 0.0));
            result.add(new StatDto("5", 0.0));
            result.add(new StatDto("6", 0.0));
            result.add(new StatDto("7", 0.0));
            result.add(new StatDto("8", 0.0));
            result.add(new StatDto("9", 0.0));
            result.add(new StatDto("10", 0.0));
            result.add(new StatDto("11", 0.0));
            result.add(new StatDto("12", 0.0));

//
            List<Object[]> stats = orderRepository.findMonthlyStats(fromTime);

            for (StatDto statDto : result) {
                Optional<Object[]> obt = stats.stream()
                        .filter(objects -> objects[0].toString().equals(statDto.getKey()))
                        .findFirst();
                obt.ifPresent(objects -> statDto.setValue((Double) objects[1]));
            }

            return result;
        }

    }

    @GetMapping("/total-user")
    public Map<String, Long> countAllUsers() {
        return Map.of("totalUser", accountRepository.countByRole("USER"));
    }

}
