package com.management.employee.system.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Locale;
import java.util.Map;

@Getter
@Builder
public class ModelAndView {

    private String view;
    private Map<String, Object> model;
    private final Locale locale = new Locale("pt", "BR");
}
