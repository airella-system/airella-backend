package pl.edu.agh.airsystem.util;

import pl.edu.agh.airsystem.model.database.Location;

public class GeographicUtils {

    public static final double R = 6372.8; // in kilometers

    //Haversine formula for calculating distance bettwen two points
    public static double distance(Location location1, Location location2) {
        double lat1 = location1.getLatitude();
        double lat2 = location2.getLatitude();
        double lon1 = location1.getLongitude();
        double lon2 = location2.getLongitude();


        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.pow(Math.sin(dLon / 2), 2) * Math.cos(lat1) * Math.cos(lat2);
        double c = 2 * Math.asin(Math.sqrt(a));
        return R * c * 1000; // convert to meters
    }

}
