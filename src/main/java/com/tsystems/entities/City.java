package com.tsystems.entities;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "cities")
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "lat")
    @NotNull
    private Double lat;

    @Column(name = "long")
    @NotNull
    private Double lng;

    @Column(name = "name")
    @NotNull
    private String name;

    public Long getId() {
        return id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLong() {
        return lng;
    }

    public void setLong(Double lng) {
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof City))
            return false;
        if (o == this)
            return true;
        City city = (City) o;
        return ((City) o).name.equals(name) &&
                ((City) o).lat == lat &&
                ((City) o).lng == lng;
    }

    @Override
    public int hashCode() {
        return 307 * name.hashCode() +
                331 * lat.hashCode() +
                397 + lng.hashCode();
    }

    @Override
    public String toString() {
        return "City: " +
                "id = " + id +
                ", lat = " + lat +
                ", lng = " + lng +
                ", name = " + name;
    }
}
