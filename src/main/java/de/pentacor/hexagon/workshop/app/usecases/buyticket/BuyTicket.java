package de.pentacor.hexagon.workshop.app.usecases.buyticket;

import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.ports.primary.parking.BuyTicketRequest;
import de.pentacor.hexagon.workshop.app.ports.primary.parking.BuyTicketRequestException;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;
import de.pentacor.hexagon.workshop.payment.PayRequest;
import de.pentacor.hexagon.workshop.payment.PaymentService;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class BuyTicket implements Function<BuyTicketRequest, Ticket> {

    private static final BigDecimal MINUTES_PER_HOUR = new BigDecimal("60.00");
    private static final BigDecimal EURO_RATE_PER_HOUR = new BigDecimal("1.50");

    private final ForStoringData dataRepository;
    private final PaymentService paymentService;
    private final Clock clock;

    @Override
    public Ticket apply(BuyTicketRequest request) {
        LocalDateTime currentDateTime = LocalDateTime.now(clock);
        // Current date-time is needed to validate the card expiration
        List<String> requestErrors = request.validate(currentDateTime);
        if (!requestErrors.isEmpty()) {
            throw new BuyTicketRequestException(requestErrors);
        }

        // Pay for the ticket
        BigDecimal eurosToPay = new BigDecimal(request.getEuros());
        String paymentCard = request.getCard();
        PayRequest payRequest = new PayRequest(eurosToPay, paymentCard);
        String paymentId = this.paymentService.pay(payRequest);

        // Build the ticket
        LocalDateTime endingDateTime = calculateEndingDateTime(currentDateTime, eurosToPay);
        Ticket ticket = Ticket.builder()
                .ticketCode(this.dataRepository.nextTicketCode())
                .carPlate(request.getCarPlate())
                .startingDateTime(currentDateTime)
                .endingDateTime(endingDateTime)
                .price(eurosToPay)
                .paymentId(paymentId)
                .build();

        // Store the ticket
        this.dataRepository.saveTicket(ticket);
        return ticket;
    }


    /**
     * minutes = (euros * minutesPerHour) / eurosPerHour
     * endingDateTime = startingDateTime + minutes
     */
    private LocalDateTime calculateEndingDateTime(LocalDateTime startingDateTime, BigDecimal euros) {
        BigDecimal minutes = euros.multiply(MINUTES_PER_HOUR).divide(BuyTicket.EURO_RATE_PER_HOUR, 0, RoundingMode.HALF_UP);
        return startingDateTime.plusMinutes(minutes.longValue());
    }

}
