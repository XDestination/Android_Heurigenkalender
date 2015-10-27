package eu.robomaniac.heurigenkalender.models;

import android.location.Location;

/**
 * Created by kgamauf on 10/27/15.
 */
public class Gastronomiebetrieb {
    protected String name;
    protected String address;
    protected double rating;
    protected Location location;

    public Gastronomiebetrieb(String name, String address, double rating, Location location) {
        this.name = name;
        this.address = address;
        this.rating = rating;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getRating() {
        return rating;
    }

    public Location getLocation() {
        return location;
    }
}
