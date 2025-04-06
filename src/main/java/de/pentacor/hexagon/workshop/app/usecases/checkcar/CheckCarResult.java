package de.pentacor.hexagon.workshop.app.usecases.checkcar;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Output data returned after checking a parked car:
 * - Car plate that has been checked.
 * - Rate name of the zone where the checked car is parked.
 * - Date-time at which the checking was done.
 * - Status of the checked car: Legally or illegally parked.
 */
@Data
public class CheckCarResult {

    private final String carPlate;
    private final LocalDateTime dateTime;
    private final Boolean legallyParked;

    @Override
    public String toString() {
        return String.format("Check Car Result: [ Car:'%s' , Date-time:'%s' , Status:'%s' ]",
                this.carPlate,
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm").format(this.dateTime),
                (this.legallyParked) ? ("Legally parked") : ("Illegally parked")
        );
    }

}
