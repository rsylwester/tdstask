package uk.co.testdrivensolutions.tdstask.parking.persistance.entities;

import lombok.Data;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;

import java.time.Instant;
import java.util.Optional;

@Data
public class ParkingSpaceEntity {
    int number;
    Instant timeIn;
    String vehicleReg;
    VehicleType vehicleType;

    public ParkingSpaceEntity(int number) {
        this.number = number;
    }

    public void setBusy(String vehicleReg, VehicleType vehicleType, Instant timeIn) {
        this.vehicleReg = vehicleReg;
        this.vehicleType = vehicleType;
        this.timeIn = timeIn;
    }

    public void setFree() {
        this.vehicleReg = null;
        this.vehicleType = null;
        this.timeIn = null;
    }

    public boolean isBusy() {
        return Optional.ofNullable(timeIn).isPresent();
    }

    public boolean isFree() {
        return Optional.ofNullable(timeIn).isEmpty();
    }
}
