package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public class StatisticDoesNotExistException extends BaseErrorException {
    public StatisticDoesNotExistException() {
        super(NOT_FOUND,
                "STATISTIC_DOES_NOT_EXIST",
                "Statistic with this id does not exist.");
    }
}
