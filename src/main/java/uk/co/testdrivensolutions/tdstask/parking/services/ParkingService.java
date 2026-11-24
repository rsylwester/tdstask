package uk.co.testdrivensolutions.tdstask.parking.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uk.co.testdrivensolutions.tdstask.parking.dto.requests.VehicleSpaceReservationRequestDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.ParkingBillResponseDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.ParkingDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.VehicleSpaceOrderConfirmationResponseDto;
import uk.co.testdrivensolutions.tdstask.parking.persistance.ParkingStorage;
import uk.co.testdrivensolutions.tdstask.parking.persistance.entities.ParkingSpaceEntity;
import uk.co.testdrivensolutions.tdstask.parking.services.exceptions.VehicleNotFoundException;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingService {

    private final ParkingStorage parkingStorage;
    private final ChargeCalculator chargeCalculator;


    public ParkingDto getParking() {
        return new ParkingDto(parkingStorage.availableSpaces(), parkingStorage.occupiedSpaces());
    }


    public ParkingBillResponseDto freeVehicleSpace(String vehicleReg) {

        Optional<ParkingSpaceEntity> parkingSpaceEntity = parkingStorage.findByReg(vehicleReg);

        if (parkingSpaceEntity.isPresent()) {
            ParkingBillResponseDto bill = calculateBill(parkingSpaceEntity.get());
            parkingStorage.freeSpace(parkingSpaceEntity.get());
            return bill;
        } else {
            throw new VehicleNotFoundException(vehicleReg);
        }
    }

    private ParkingBillResponseDto calculateBill(ParkingSpaceEntity space) {
        Instant timeIn = space.getTimeIn();
        Instant timeOut = Instant.now();
        BigDecimal cost = chargeCalculator.calculate(space.getVehicleType(), Duration.between(timeIn, timeOut));

        return new ParkingBillResponseDto(space.getVehicleReg(), cost, timeIn, timeOut);
    }

    public Optional<VehicleSpaceOrderConfirmationResponseDto> reserveParkingSpace(VehicleSpaceReservationRequestDto vehicleSpaceReservationRequestDto) {
        Optional<Instant> dateOpt = parkingStorage.reservePlace(vehicleSpaceReservationRequestDto.getVehicleReg(), vehicleSpaceReservationRequestDto.getVehicleType());

        return dateOpt.map(date -> new VehicleSpaceOrderConfirmationResponseDto(vehicleSpaceReservationRequestDto.getVehicleReg(), date));
    }
}
