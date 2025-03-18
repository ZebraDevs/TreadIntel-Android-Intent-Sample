package com.zebra.treadreadersampleintentapi.models;

public class TreadData {
    private float metricTreadDepth;
    private String position;
    private int confidenceLevel;
    private String status;

    public float getMetricTreadDepth() {
        return metricTreadDepth;
    }

    public String getPosition() {
        return position;
    }

    public int getConfidenceLevel() {
        return confidenceLevel;
    }

    public String getStatus() {
        return status;
    }
}
