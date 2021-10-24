package ru.golovchen.spring.sheet;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class HouseRow {
    public final int row;

    public String link;
    public Double price;
    public Double houseArea;
    public Double landArea;
    public String landStatus;
    public Double commuteTime; //minutes
    public Integer floors;
    public String wallMaterial;
    public String floorMaterial;
    public String foundation;
    public Integer year;
    public String finishing;

    public String gas;
    public String water;
    public String sewerage;
    public String internet;

    public String garage;

    public HouseRow(int row) {
        this.row = row;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }
}
