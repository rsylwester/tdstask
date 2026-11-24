package uk.co.testdrivensolutions.tdstask.parking.dto.requests;

import lombok.Data;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;

@Data
public class VehicleSpaceReservationRequestDto {

    String vehicleReg;
    VehicleType vehicleType;

}
