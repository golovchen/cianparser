package ru.golovchen.spring.examples;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class Offer {
    public Long id;
    public String description;
    public String fullUrl;
    public Building building;
    public Geo geo;
    public Double totalArea;

    public static class Land {
        public String area;
        public String areaUnitType;
        public String status;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
