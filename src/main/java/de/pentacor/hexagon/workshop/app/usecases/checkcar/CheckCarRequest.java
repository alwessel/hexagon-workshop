package de.pentacor.hexagon.workshop.app.usecases.checkcar;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Input data needed for checking a parked car:
 * - Car plate.
 * - Rate name of the zone where the car is parked.
 */
@Data
public class CheckCarRequest {

    private final String carPlate;

    public List<String> validate() {
        List<String> errorMessages = new ArrayList<String>();
        if (isBlank(this.carPlate)) {
            errorMessages.add("Car plate must be provided");
        }
        return errorMessages;
    }

    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

}
