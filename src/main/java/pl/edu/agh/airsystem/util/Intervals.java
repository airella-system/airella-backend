package pl.edu.agh.airsystem.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Intervals {

    public static List<Interval> generateIntervals(Timespan timespan,
                                                   Duration intervalDuration) {
        LocalDateTime startDate = timespan.getStart();
        LocalDateTime endDate = timespan.getEnd();

        return Stream.iterate(endDate, currentEndDate -> currentEndDate.minus(intervalDuration))
                .limit(Duration.between(startDate, endDate).dividedBy(intervalDuration) + 1)
                .map(currentEndDate -> new Interval(getCurrentStartDate(startDate, currentEndDate, intervalDuration), currentEndDate))
                .collect(Collectors.toList());
    }

    private static LocalDateTime getCurrentStartDate(LocalDateTime startDate,
                                                     LocalDateTime currentEndDate, Duration interval) {
        LocalDateTime currentStartDate = currentEndDate.minus(interval);
        if (currentStartDate.isBefore(startDate)) {
            currentStartDate = startDate;
        }
        return currentStartDate;
    }

}
