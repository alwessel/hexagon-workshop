package de.pentacor.hexagon.workshop.app.usecases.checkcar;

import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.db.DatabaseService;
import de.pentacor.hexagon.workshop.db.DbTicket;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class CheckCar implements Function<CheckCarRequest, CheckCarResult> {

    private final DatabaseService dataRepository;
    private final Clock clock;

    @Override
    public CheckCarResult apply(CheckCarRequest request) {
        List<String> requestErrors = request.validate();
        if (!requestErrors.isEmpty()) {
            throw new CheckCarRequestException(requestErrors);
        }
        String carPlate = request.getCarPlate();
        LocalDateTime currentDateTime = LocalDateTime.now(this.clock);
        var ticketsForCar = this.dataRepository.getTicketsByCar(carPlate).stream()
                .map(this::mapToTicket)
                .toList();

        var ticketsIterator = ticketsForCar.iterator();
        boolean activeTicketFound = false;
        while (!activeTicketFound && ticketsIterator.hasNext()) {
            var ticket = ticketsIterator.next();
            if (ticket.isValidAt(currentDateTime)) {
                activeTicketFound = true;
            }
        }
        return new CheckCarResult(carPlate, currentDateTime, activeTicketFound);
    }

    private Ticket mapToTicket(DbTicket db) {
        return Ticket.builder()
                .ticketCode(db.getTicketCode())
                .carPlate(db.getCarPlate())
                .startingDateTime(LocalDateTime.parse(db.getStartingDateTime()))
                .endingDateTime(LocalDateTime.parse(db.getEndingDateTime()))
                .price(new BigDecimal(db.getPrice()))
                .paymentId(db.getPaymentId())
                .build();
    }

}
