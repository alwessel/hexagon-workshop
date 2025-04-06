package de.pentacor.hexagon.workshop.adapter.secondary.database;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DbTicket {

    private final String ticketCode;
    private final String carPlate;
    private final String startingDateTime;
    private final String endingDateTime;
    private final String price;
    private final String paymentId;

}
