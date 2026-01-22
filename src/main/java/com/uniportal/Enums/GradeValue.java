package com.uniportal.Enums;

import lombok.Getter;

@Getter
public enum GradeValue {
    TWO(2.0),
    THREE(3.0),
    THREE_HALF(3.5),
    FOUR(4.0),
    FOUR_HALF(4.5),
    FIVE(5.0);

    private final double numericValue;

    GradeValue(double numericValue) {
        this.numericValue = numericValue;
    }
}
