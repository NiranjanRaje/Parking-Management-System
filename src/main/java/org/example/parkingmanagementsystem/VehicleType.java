package org.example.parkingmanagementsystem;

public enum VehicleType {
    TWO_WHEELER("Two Wheeler"),
    FOUR_WHEELER("Four Wheeler");

    private final String displayName;

    VehicleType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
