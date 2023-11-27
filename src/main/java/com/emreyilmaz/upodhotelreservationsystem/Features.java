package com.emreyilmaz.upodhotelreservationsystem;



public class Features {
    private int featureId;
    private String featureName;

    public Features(int featureId, String featureName) {
        this.featureId = featureId;
        this.featureName = featureName;
    }

    public Features() {

    }

    public int getFeatureId() {
        return featureId;
    }

    public void setFeatureId(int featureId) {
        this.featureId = featureId;
    }

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }
}
