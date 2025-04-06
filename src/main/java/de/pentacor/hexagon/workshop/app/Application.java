package de.pentacor.hexagon.workshop.app;


import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicket;
import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicketRequest;
import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicketRequestException;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCar;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCarRequest;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCarRequestException;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCarResult;
import de.pentacor.hexagon.workshop.db.DatabaseService;
import de.pentacor.hexagon.workshop.payment.PayErrorException;
import de.pentacor.hexagon.workshop.payment.PaymentService;
import lombok.AllArgsConstructor;

import java.time.Clock;

@AllArgsConstructor
public class Application {

    private final DatabaseService databaseService;
    private final PaymentService paymentService;
    private final Clock clock;

    public Application() {
        this(new DatabaseService(), new PaymentService(), Clock.systemDefaultZone());
    }

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
    public Ticket buyTicket(BuyTicketRequest request) {
        BuyTicket buyTicket = new BuyTicket(this.databaseService, this.paymentService, this.clock);
        return buyTicket.apply(request);
    }

    /**
     * Check the status of a car parked at a zone regulated by a rate.
     * The status is 'illegally parked' when there is no active ticket in the repository, for the car and rate given in the request.
     * Otherwise, if there is at least one such a ticket in the repository, then the status would be 'legally parked'.
     * A ticket is active if the current date-time is between the ticket starting and ending date-times, both included.
     *
     * @param request Input data needed for checking a car.
     * @throws CheckCarRequestException If any input data in the request is not valid.
     * @return An object with the checking outcome, telling if the car is legally parked of not.
     * @see CheckCarRequest
     * @see CheckCarResult
     */
    public CheckCarResult checkCar(CheckCarRequest request) {
        CheckCar checkCar = new CheckCar(this.databaseService, this.clock);
        return checkCar.apply(request);
    }

}
