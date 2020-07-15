package org.boka.cafe.pojo;

import java.util.Objects;

public class UserSettings {

    private int id;
    private int countSend;
    private String nameUser;
    private String radius;
    private String preferLang;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountSend() {
        return countSend;
    }

    public void setCountSend(int countSend) {
        this.countSend = countSend;
    }

    public String getNameUser() {
        return nameUser;
    }

    public void setNameUser(String nameUser) {
        this.nameUser = nameUser;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getPreferLang() {
        return preferLang;
    }

    public void setPreferLang(String preferLang) {
        this.preferLang = preferLang;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSettings that = (UserSettings) o;
        return id == that.id &&
                countSend == that.countSend &&
                Objects.equals(nameUser, that.nameUser) &&
                Objects.equals(radius, that.radius) &&
                Objects.equals(preferLang, that.preferLang);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, countSend, nameUser, radius, preferLang);
    }

    @Override
    public String toString() {
        return "UserSettings{" +
                "id=" + id +
                ", countSend=" + countSend +
                ", nameUser='" + nameUser + '\'' +
                ", radius='" + radius + '\'' +
                ", preferLang='" + preferLang + '\'' +
                '}';
    }
}
