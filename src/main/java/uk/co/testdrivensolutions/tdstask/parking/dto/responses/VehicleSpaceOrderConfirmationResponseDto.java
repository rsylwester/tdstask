package uk.co.testdrivensolutions.tdstask.parking.dto.responses;

import java.time.Instant;

public record VehicleSpaceOrderConfirmationResponseDto(String vehicleReg, Instant timeIn) {
}
