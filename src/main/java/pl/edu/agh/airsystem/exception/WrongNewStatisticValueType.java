package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class WrongNewStatisticValueType extends BaseErrorException {
    public WrongNewStatisticValueType() {
        super(NOT_FOUND,
                "WRONG_NEW_STATISTIC_VALUE_TYPE",
                "Type of new statistic value is wrong.");
    }
}
