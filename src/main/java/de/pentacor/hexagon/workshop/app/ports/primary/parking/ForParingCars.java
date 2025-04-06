package de.pentacor.hexagon.workshop.app.ports.primary.parking;

import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.payment.PayErrorException;

public interface ForParingCars {
    /**
     * Pay for a ticket to park a car at a zone regulated by a rate,
     * and save the ticket in the repository.
     * The validity period of the ticket begins at the current date-time,
     * and its duration is calculated in minutes by applying the rate,
     * based on the amount of euros paid.
     *
     * @param request Input data needed for buying a ticket.
     * @return A ticket valid for parking the car at a zone regulated by the rate,
     * paying the euros amount using the card.
     * The ticket holds a reference to the identifier of the payment that was done.
     * @throws BuyTicketRequestException If any input data in the request is not valid.
     * @throws PayErrorException         If any error occurred while paying.
     * @see BuyTicketRequest
     */
    Ticket buyTicket(BuyTicketRequest request);
}
