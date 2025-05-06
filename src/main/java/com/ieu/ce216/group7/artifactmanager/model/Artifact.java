package com.ieu.ce216.group7.artifactmanager.model;

import java.util.Date;

public class Artifact {
    private String artifactId;
    private String artifactName;
    private String category;
    private String civilization;
    private String discoveryLocation;
    private Date discoverydate;
    private String currentLocation;
    private Dimension dimensions;
    private Double weight;
    private String composition;
    private String tags;

    public Artifact() {
    }

    public Artifact(String artifactId, String artifactName, String category, String civilization, String discoveryLocation, Date discoverydate, String currentLocation, Dimension dimensions, Double weight, String composition, String tags) {
        this.artifactId = artifactId;
        this.artifactName = artifactName;
        this.category = category;
        this.civilization = civilization;
        this.discoveryLocation = discoveryLocation;
        this.discoverydate = discoverydate;
        this.currentLocation = currentLocation;
        this.dimensions = dimensions;
        this.weight = weight;
        this.composition = composition;
        this.tags = tags;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getArtifactName() {
        return artifactName;
    }

    public void setArtifactName(String artifactName) {
        this.artifactName = artifactName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCivilization() {
        return civilization;
    }

    public void setCivilization(String civilization) {
        this.civilization = civilization;
    }

    public String getDiscoveryLocation() {
        return discoveryLocation;
    }

    public void setDiscoveryLocation(String discoveryLocation) {
        this.discoveryLocation = discoveryLocation;
    }

    public Date getDiscoverydate() {
        return discoverydate;
    }

    public void setDiscoverydate(Date discoverydate) {
        this.discoverydate = discoverydate;
    }

    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public Dimension getDimensions() {
        return dimensions;
    }

    public void setDimensions(Dimension dimensions) {
        this.dimensions = dimensions;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getComposition() {
        return composition;
    }

    public void setComposition(String composition) {
        this.composition = composition;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
