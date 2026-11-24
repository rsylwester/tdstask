package uk.co.testdrivensolutions.tdstask.parking.persistance;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;
import uk.co.testdrivensolutions.tdstask.parking.persistance.entities.ParkingSpaceEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Repository
public class ParkingStorage {

    private final ArrayList<ParkingSpaceEntity> parkingSpaces;

    public ParkingStorage(@Value("${parking.size}") int parkingSize) {
        this.parkingSpaces = IntStream.rangeClosed(1, parkingSize).mapToObj(ParkingSpaceEntity::new).collect(Collectors.toCollection(ArrayList::new));
    }

    private Optional<ParkingSpaceEntity> findFirstFreePlace() {
        return parkingSpaces.stream().filter(ParkingSpaceEntity::isFree).findFirst();
    }

    public Optional<Instant> reservePlace(String vehicleReg, VehicleType vehicleType) {

        if (findByReg(vehicleReg).isPresent()) {
            return Optional.empty();
        }

        return findFirstFreePlace().map(place -> {
            Instant timeIn = Instant.now();
            place.setBusy(vehicleReg, vehicleType, timeIn);
            return timeIn;
        });
    }

    public void freeSpace(ParkingSpaceEntity space) {
        space.setFree();
    }

    public Optional<ParkingSpaceEntity> findByReg(String vehicleReg) {
        return parkingSpaces.stream()
                .filter(space -> vehicleReg.equals(space.getVehicleReg()))
                .findFirst();
    }

    public int availableSpaces() {
        return (int) parkingSpaces.stream().filter(ParkingSpaceEntity::isFree).count();
    }

    public int occupiedSpaces() {
        return (int) parkingSpaces.stream().filter(ParkingSpaceEntity::isBusy).count();
    }

}
