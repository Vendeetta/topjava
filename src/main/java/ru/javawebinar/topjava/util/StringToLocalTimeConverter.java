package ru.javawebinar.topjava.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class StringToLocalTimeConverter implements Converter<String, LocalTime> {

    private String pattern = "HH:mm";

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public LocalTime convert(String stringTime) {
        return StringUtils.hasLength(stringTime) ?
                LocalTime.parse(stringTime, DateTimeFormatter.ofPattern(pattern)) : null;
    }
}
