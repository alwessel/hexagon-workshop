package de.pentacor.hexagon.workshop.app.model;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Class representing objects with the data of a parking ticket:
 * code				Unique identifier of the ticket
 * It is a 10-digit number with leading zeros if necessary
 * carPlate			Plate of the car that has been parked
 * rateName			Rate name of the zone where the car is parked at
 * startingDateTime	When the parking period begins
 * endingDateTime		When the parking period expires
 * price				Amount of euros paid for the ticket
 * paymentId           Unique identifier of the payment made to get the ticket.
 */
@Data
@Builder
public class Ticket {

    private final String ticketCode;
    private final String carPlate;
    private final LocalDateTime startingDateTime;
    private final LocalDateTime endingDateTime;
    private final BigDecimal price;
    private final String paymentId;

    /**
     * A ticket is valid if the given date-time is between
     * the starting and ending date-times of the ticket, both included.
     */
    public boolean isValidAt(LocalDateTime dateTime) {
        if (dateTime.isBefore(this.startingDateTime)) {
            return false;
        }
        if (dateTime.isAfter(this.endingDateTime)) {
            return false;
        }
        return true;
    }

}
