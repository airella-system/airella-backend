package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class WrongNewStatisticValueType extends BaseErrorException {
    public WrongNewStatisticValueType() {
        super(BAD_REQUEST,
                "WRONG_NEW_STATISTIC_VALUE_TYPE",
                "Type of new statistic value is wrong.");
    }
}
