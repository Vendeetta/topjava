package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDateConverter implements Converter<String, LocalDate> {

    private String pattern = "yyyy-MM-dd";

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public LocalDate convert(String dateString) {
        return StringUtils.hasLength(dateString) ?
                LocalDate.parse(dateString, DateTimeFormatter.ofPattern(pattern)) : null;
    }
}
