package uk.co.testdrivensolutions.tdstask.parking.constants;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum VehicleType {
    SMALL(1), MEDIUM(2), LARGE(3);

    private final int number;

    VehicleType(int number) {
        this.number = number;
    }

    @JsonCreator
    public static VehicleType fromNumber(int number) {
        return Arrays.stream(values())
                .filter(type -> type.number == number)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown vehicle type: " + number));
    }
}
