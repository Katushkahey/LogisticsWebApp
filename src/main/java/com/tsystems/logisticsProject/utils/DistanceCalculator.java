package com.tsystems.logisticsProject.utils;

import com.tsystems.logisticsProject.entity.City;
import com.tsystems.logisticsProject.entity.Waypoint;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DistanceCalculator {

    private final int EARTH_RADIUS_IN_KILOMETRES = 6373;

    /**
     * calculate total distance to be driven during completing the order.
     *  Includes distance between home and firs waypoint of order and distance
     *  between last waypoint and home.
     * @return final distance to be driven depending on home city.
     */
    public double calculateTotalDistanceForOrderDependingOnStartCity(City city, List<Waypoint> listOfWaypoints) {
        City startCity = listOfWaypoints.get(0).getCity();
        City endCity = listOfWaypoints.get(listOfWaypoints.size() - 1).getCity();
        double distanceToFirstWaypoint = calculateDistanceBetweenTwoCities(city, startCity);
        double distanceFromLastWaypointToHome = calculateDistanceBetweenTwoCities(endCity, city);
        return distanceToFirstWaypoint + calculateDistanceBetweenWaypointsForOrder(listOfWaypoints) + distanceFromLastWaypointToHome;
    }

    /**
     * calculate total distance between all waypoints of order.
     * @return total order`s distance.
     */

    public Double calculateDistanceBetweenWaypointsForOrder(List<Waypoint> listOfWaypoints) {
        Waypoint waypointFrom;
        Waypoint waypointTo;
        double distanceBetweenWaypointsOfOrder = 0;

        int i = 0;
        while (i < listOfWaypoints.size() - 1) {
            waypointFrom = listOfWaypoints.get(i);
            waypointTo = listOfWaypoints.get(i + 1);
            double distanceBetweenCurrentWaypoints = calculateDistanceBetweenTwoCities(waypointFrom.getCity(), waypointTo.getCity());
            distanceBetweenWaypointsOfOrder += distanceBetweenCurrentWaypoints;
            i++;
        }
        return distanceBetweenWaypointsOfOrder;
    }

    public double calculateDistanceBetweenTwoCities(City cityFrom, City cityTo) {
        return getDist(cityFrom.getLat(), cityFrom.getLng(), cityTo.getLat(), cityTo.getLng());
    }

    /**
     * calculate distance between two points by their lat and long.
     * This uses the ‘haversine’ formula to calculate the great-circle distance between two points – that is,
     * the shortest distance over the earth’s surface.
     * @return distance between two points.
     */
    public double getDist(double lat1, double lon1, double lat2, double lon2) {
        double lat1rad = Math.toRadians(lat1);
        double lat2rad = Math.toRadians(lat2);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double deltaLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1rad) * Math.cos(lat2rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_IN_KILOMETRES * c;
    }
}
