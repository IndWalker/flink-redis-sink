package org.example.accumulators;

public class TemperatureAccumulator {
    private String key;
    private int countTemperature = 0;
    private int countInstances = 0;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public TemperatureAccumulator(String key, int countTemperature, int countInstances) {
        this.key = key;
        this.countTemperature = countTemperature;
        this.countInstances = countInstances;
    }

    public int getCountTemperature() {
        return countTemperature;
    }

    public int getCountInstances() {
        return countInstances;
    }

    public void setCountTemperature(int countTemperature) {
        this.countTemperature = countTemperature;
    }

    public void setCountInstances(int countInstances) {
        this.countInstances = countInstances;
    }
}
