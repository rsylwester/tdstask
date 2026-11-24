package uk.co.testdrivensolutions.tdstask.parking.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import uk.co.testdrivensolutions.tdstask.parking.persistance.ParkingStorage;
import uk.co.testdrivensolutions.tdstask.parking.services.ChargeCalculator;
import uk.co.testdrivensolutions.tdstask.parking.services.ParkingService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ParkingController.class)
@Import({ParkingService.class, ParkingStorage.class, ChargeCalculator.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class ParkingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void emptyCarParkHasAllSpacesAvailable() throws Exception {
        mockMvc.perform(get("/parking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.availableSpaces").value(20))
                .andExpect(jsonPath("$.occupiedSpaces").value(0));
    }

    @Test
    void parkingAVehicleOccupiesOneSpace() throws Exception {
        mockMvc.perform(post("/parking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"vehicleReg": "XYZ1234", "vehicleType": 1}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleReg").value("XYZ1234"))
                .andExpect(jsonPath("$.timeIn").exists());

        mockMvc.perform(get("/parking"))
                .andExpect(jsonPath("$.availableSpaces").value(19))
                .andExpect(jsonPath("$.occupiedSpaces").value(1));
    }

    @Test
    void parkingWhenFullReturnsConflict() throws Exception {
        for (int i = 1; i <= 20; i++) {
            park("CAR" + i).andExpect(status().isOk());
        }

        park("ONE_TOO_MANY").andExpect(status().isConflict());

        mockMvc.perform(get("/parking"))
                .andExpect(jsonPath("$.availableSpaces").value(0))
                .andExpect(jsonPath("$.occupiedSpaces").value(20));
    }


    @Test
    void billingFreesTheSpaceAndReturnsTheBill() throws Exception {
        park("XYZ1234").andExpect(status().isOk());

        bill("XYZ1234")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.vehicleReg").value("XYZ1234"))
                .andExpect(jsonPath("$.vehicleCharge").value(0.0))
                .andExpect(jsonPath("$.timeIn").exists())
                .andExpect(jsonPath("$.timeOut").exists());

        mockMvc.perform(get("/parking"))
                .andExpect(jsonPath("$.availableSpaces").value(20))
                .andExpect(jsonPath("$.occupiedSpaces").value(0));
    }

    @Test
    void largeVehicleTypeThreeIsAccepted() throws Exception {
        park("BIG1", 3).andExpect(status().isOk());
    }

    @Test
    void vehicleTypeZeroIsRejected() throws Exception {
        park("ZERO1", 0).andExpect(status().isBadRequest());
    }

    @Test
    void billingUnknownVehicleReturnsNotFound() throws Exception {
        bill("NOT_PARKED").andExpect(status().isNotFound());
    }

    @Test
    void parkingSameVehicleTwiceReturnsConflict() throws Exception {
        park("XYZ1234").andExpect(status().isOk());
        park("XYZ1234").andExpect(status().isConflict());
    }

    private ResultActions park(String vehicleReg) throws Exception {
        return mockMvc.perform(post("/parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"vehicleReg": "%s", "vehicleType": 1}
                        """.formatted(vehicleReg)));
    }

    private ResultActions park(String vehicleReg, int vehicleType) throws Exception {
        return mockMvc.perform(post("/parking")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"vehicleReg": "%s", "vehicleType": %d}
                        """.formatted(vehicleReg, vehicleType)));
    }

    private ResultActions bill(String vehicleReg) throws Exception {
        return mockMvc.perform(post("/parking/bill")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"vehicleReg": "%s"}
                        """.formatted(vehicleReg)));
    }

}
