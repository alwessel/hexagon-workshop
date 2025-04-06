package de.pentacor.hexagon.workshop.app;


import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarRequest;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarResult;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.ForIssueFines;
import de.pentacor.hexagon.workshop.app.ports.primary.parking.BuyTicketRequest;
import de.pentacor.hexagon.workshop.app.ports.primary.parking.ForParingCars;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;
import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicket;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCar;
import de.pentacor.hexagon.workshop.payment.PaymentService;
import lombok.AllArgsConstructor;

import java.time.Clock;

@AllArgsConstructor
public class Application implements ForIssueFines, ForParingCars {

    private final ForStoringData databaseService;
    private final PaymentService paymentService;
    private final Clock clock;

    @Override
    public Ticket buyTicket(BuyTicketRequest request) {
        BuyTicket buyTicket = new BuyTicket(this.databaseService, this.paymentService, this.clock);
        return buyTicket.apply(request);
    }

    @Override
    public CheckCarResult checkCar(CheckCarRequest request) {
        CheckCar checkCar = new CheckCar(this.databaseService, this.clock);
        return checkCar.apply(request);
    }

}
