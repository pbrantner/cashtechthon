package at.ac.tuwien.cashtechthon.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Jackson friendly location list
 * Created by j on 02.06.16.
 */
public class Locations {
    private List<String> locations = new ArrayList<>();

    public List<String> getLocations() {
        return locations;
    }

    public Locations add(String location) {
        this.locations.add(location);
        return this;
    }
}
