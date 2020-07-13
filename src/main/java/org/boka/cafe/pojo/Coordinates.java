package org.boka.cafe.pojo;

import java.util.Objects;

public class Coordinates {

    private float latitude;
    private float longitude;

    public Coordinates(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public String getLatitudeString() {
        return String.valueOf(latitude).replace(',', '.');
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public String getLongitudeString() {
        return String.valueOf(longitude).replace(',', '.');
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public String formatToRequest() {
        return this.getLatitude() + "," + this.getLongitude();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinates that = (Coordinates) o;
        return Float.compare(that.latitude, latitude) == 0 &&
                Float.compare(that.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }

    @Override
    public String toString() {
        return "Сoordinates{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}
