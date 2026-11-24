package uk.co.testdrivensolutions.tdstask.parking.dto.responses;

import java.math.BigDecimal;
import java.time.Instant;

public record ParkingBillResponseDto(
        String vehicleReg,
        BigDecimal vehicleCharge,
        Instant timeIn,
        Instant timeOut) {
}
