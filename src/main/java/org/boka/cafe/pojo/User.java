package org.boka.cafe.pojo;

public class User {

    private int id;
    private int countSend;
    private String name;
    private String phone;
    private String lang;
    private int radius;

    public void setId(int id) {
        this.id = id;
    }

    public void setCountSend(int countSend) {
        this.countSend = countSend;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public int getCountSend() {
        return countSend;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
