package uk.co.testdrivensolutions.tdstask.parking.dto.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;

@Data
public class VehicleSpaceReservationRequestDto {

    @NotBlank
    String vehicleReg;
    @NotNull
    VehicleType vehicleType;

}
