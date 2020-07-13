package org.boka.cafe.pojo;

import java.util.Objects;

public class Cafe {

    private String name;
    private Coordinates coordinates;
    private String rating;
    private String ratingTotal;
    private String address;
    private String url;
    private long distance;
    private boolean isOpen;
    private boolean consistStatus = true;

    public Cafe() {
    }

    public Cafe(String name, Coordinates coordinates, String rating, String ratingTotal, String address, String url, int distance, boolean isOpen) {
        this.name = name;
        this.coordinates = coordinates;
        this.rating = rating;
        this.ratingTotal = ratingTotal;
        this.url = url;
        this.address = address;
        this.distance = distance;
        this.isOpen = isOpen;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRatingTotal() {
        return ratingTotal;
    }

    public void setRatingTotal(String ratingTotal) {
        this.ratingTotal = ratingTotal;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public boolean consistStatus() {
        return consistStatus;
    }

    public void setConsistStatus(boolean consistStatus) {
        this.consistStatus = consistStatus;
    }

    public long getDistance() {
        return distance;
    }

    public double getDistanceKm() {
        return ((double) distance) / 1000;
    }

    public void setDistance(long distance) {
        this.distance = distance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cafe cafe = (Cafe) o;
        return distance == cafe.distance &&
                isOpen == cafe.isOpen &&
                consistStatus == cafe.consistStatus &&
                Objects.equals(name, cafe.name) &&
                Objects.equals(coordinates, cafe.coordinates) &&
                Objects.equals(url, cafe.url) &&
                Objects.equals(rating, cafe.rating) &&
                Objects.equals(ratingTotal, cafe.ratingTotal) &&
                Objects.equals(address, cafe.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinates, rating, ratingTotal, url, address, distance, isOpen, consistStatus);
    }

    @Override
    public String toString() {
        return "Cafe{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", rating='" + rating + '\'' +
                ", ratingTotal='" + ratingTotal + '\'' +
                ", address='" + address + '\'' +
                ", url='" + url + '\'' +
                ", distance=" + distance +
                ", isOpen=" + isOpen +
                ", consistStatus=" + consistStatus +
                '}';
    }
}
