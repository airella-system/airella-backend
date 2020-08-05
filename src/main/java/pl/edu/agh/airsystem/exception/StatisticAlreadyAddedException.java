package pl.edu.agh.airsystem.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class StatisticAlreadyAddedException extends BaseErrorException {
    public StatisticAlreadyAddedException() {
        super(BAD_REQUEST,
                "STATISTIC_ALREADY_ADDED",
                "Statistic with this id is already added.");
    }
}
