package uk.co.testdrivensolutions.tdstask.parking.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.testdrivensolutions.tdstask.parking.dto.requests.BillRequestDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.requests.VehicleSpaceReservationRequestDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.ParkingBillResponseDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.ParkingDto;
import uk.co.testdrivensolutions.tdstask.parking.dto.responses.VehicleSpaceOrderConfirmationResponseDto;
import uk.co.testdrivensolutions.tdstask.parking.services.ParkingService;


@RestController
@RequestMapping("/parking")
@RequiredArgsConstructor
public class ParkingController {

    private final ParkingService parkingService;

    @PostMapping
    public ResponseEntity<VehicleSpaceOrderConfirmationResponseDto> occupyParkSpace(@Valid @RequestBody VehicleSpaceReservationRequestDto request) {
        return parkingService.reserveParkingSpace(request)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.CONFLICT).build());
    }

    @GetMapping
    public ResponseEntity<ParkingDto> availableParkSpaces() {
        return ResponseEntity.ok(parkingService.getParking());
    }

    @PostMapping("/bill")
    public ResponseEntity<ParkingBillResponseDto> billForParking(@Valid @RequestBody BillRequestDto billRequestDto) {
        return ResponseEntity.ok(parkingService.freeVehicleSpace(billRequestDto.vehicleReg()));
    }
}
