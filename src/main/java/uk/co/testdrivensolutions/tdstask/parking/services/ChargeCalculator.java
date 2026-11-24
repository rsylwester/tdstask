package uk.co.testdrivensolutions.tdstask.parking.services;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import uk.co.testdrivensolutions.tdstask.parking.constants.VehicleType;

import java.math.BigDecimal;
import java.time.Duration;

@Component
@NoArgsConstructor
public class ChargeCalculator {
    public BigDecimal calculate(VehicleType type, Duration duration) {

        BigDecimal costPerMinute = null;

        switch (type) {

            case SMALL:
                costPerMinute = BigDecimal.valueOf(0.10);
                break;
            case MEDIUM:
                costPerMinute = BigDecimal.valueOf(0.20);
                break;
            case LARGE:
                costPerMinute = BigDecimal.valueOf(0.40);
                break;
        }

        long minutesSpent = duration.toMinutes();
        long additionalFee = (minutesSpent / 5);

        return costPerMinute.multiply(BigDecimal.valueOf(minutesSpent)).add(BigDecimal.valueOf(additionalFee));
    }
}
