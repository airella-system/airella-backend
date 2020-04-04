package pl.edu.agh.airsystem.util;

import pl.edu.agh.airsystem.model.database.Location;

public class GeographicUtils {

    public static double distance(Location location1, Location location2) {
        final int R = 6371; // Radius of the earth

        double lat1 = location1.getLatitude();
        double lat2 = location2.getLatitude();
        double lon1 = location1.getLongitude();
        double lon2 = location2.getLongitude();


        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c * 1000; // convert to meters

        distance = Math.pow(distance, 2);

        return Math.sqrt(distance);
    }

}
