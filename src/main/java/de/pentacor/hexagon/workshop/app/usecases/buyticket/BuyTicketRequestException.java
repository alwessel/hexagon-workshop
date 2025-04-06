package de.pentacor.hexagon.workshop.app.usecases.buyticket;

import java.util.List;

/**
 * Exception thrown if any input data in the "buy ticket" request is not valid.
 * There might be multiple validation errors, hence the list of error messages.
 */
public class BuyTicketRequestException extends RuntimeException {

    public BuyTicketRequestException(List<String> errorMessages) {
        super("Buy ticket request is not valid.\n" + errorMessages);
    }

}
