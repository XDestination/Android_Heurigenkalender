package eu.robomaniac.heurigenkalender.models;

import android.location.Location;

/**
 * Created by kgamauf on 10/28/15.
 */
public class Heuriger extends Gastronomiebetrieb {
    public Heuriger(String name, String address, double rating, Location location) {
        super(name, address, rating, location);
    }
}
