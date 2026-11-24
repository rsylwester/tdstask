package uk.co.testdrivensolutions.tdstask.parking.dto.requests;

import jakarta.validation.constraints.NotBlank;

public record BillRequestDto(@NotBlank String vehicleReg) {
}
