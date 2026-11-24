package uk.co.testdrivensolutions.tdstask.parking.services;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;

import java.math.BigDecimal;
import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class ChargeCalculatorTest {

    private final ChargeCalculator calculator = new ChargeCalculator();

    @ParameterizedTest(name = "{0} parked {1}s -> £{2}")
    @CsvSource({
            // type,  seconds, expected
            "SMALL,   0,       0.00",   // left immediately
            "SMALL,   1,       0.00",   // not full minute yet
            "SMALL,   240,     0.40",   // 4 min, no surcharge yet
            "SMALL,   241,     0.40",   // 4 min 1
            "SMALL,   300,     1.50",   // 5 min -> first £1
            "MEDIUM,  600,     4.00",   // 10 min -> 2.00 + £2
            "LARGE,   720,     6.80",   // 12 min -> 4.80 + £2
    })
    void chargesPerMinutePlusOnePoundPerFullFiveMinutes(VehicleType type, long seconds, BigDecimal expected) {
        BigDecimal charge = calculator.calculate(type, Duration.ofSeconds(seconds));

        assertThat(charge).isEqualByComparingTo(expected);
    }
}
