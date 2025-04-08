package com.ieu.ce216.group7.artifactmanager.model;

public class Dimension {
    private Double height;
    private Double width;
    private Double length;

    public Dimension() {
    }

    public Dimension(Double height, Double width, Double length) {
        this.height = height;
        this.width = width;
        this.length = length;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
