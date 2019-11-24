package com.aslam.co321_project.Common;

public class BoxList {
    private String one;
    private String two;
    private String three;
    private String four;
    private String five;
    private String six;

    public BoxList(String one) {
        this.one = one;
    }

    public BoxList(String one, String two) {
        this.one = one;
        this.two = two;
    }

    public BoxList(String one, String two, String three) {
        this.one = one;
        this.two = two;
        this.three = three;
    }

    public BoxList(String one, String two, String three, String four, String five, String six) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
        this.six = six;
    }

    public BoxList(String one, String two, String three, String four) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
    }

    public BoxList(String one, String two, String three, String four, String five) {
        this.one = one;
        this.two = two;
        this.three = three;
        this.four = four;
        this.five = five;
    }

    public String getOne() {
        return one;
    }

    public String getTwo() {
        return two;
    }

    public String getThree() {
        return three;
    }

    public String getFour() {
        return four;
    }

    public String getFive() {
        return five;
    }

    public String getSix() {
        return six;
    }
}
