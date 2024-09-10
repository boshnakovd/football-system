package com.example.football_system.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class MatchService {

    private static final DateTimeFormatter FORMATTER1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
    private static final DateTimeFormatter FORMATTER2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, FORMATTER1);
        } catch (DateTimeParseException e) {
            return LocalDate.parse(dateStr, FORMATTER2);
        }
    }
}