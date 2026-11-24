package uk.co.testdrivensolutions.tdstask.parking.services.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String vehicleReg) {
        super("Vehicle " + vehicleReg + " is not parked here");
    }
}
