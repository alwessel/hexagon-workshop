package de.pentacor.hexagon.workshop.app;

import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicketRequest;
import de.pentacor.hexagon.workshop.db.DatabaseService;
import de.pentacor.hexagon.workshop.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuyTicketTest {

    private TestClock clock = TestClock.freeze("2024/12/15 11:00");
    private DatabaseService databaseService = new DatabaseService();

    private Application application;

    @BeforeEach
    void setup() {
        application = new Application(databaseService, new PaymentService(), clock);
    }

    @Test
    void buy_ticket() {
        var carPlate = "DD-GM123";
        var request = buyTicketRequest(carPlate);

        var ticket = application.buyTicket(request);

        assertEquals(carPlate, ticket.getCarPlate());
        assertEquals(clock.now(), ticket.getStartingDateTime());
        assertNotNull(ticket.getEndingDateTime());
        assertTrue(clock.now().isBefore(ticket.getEndingDateTime()));

        assertEquals(1, databaseService.getTicketsByCar(carPlate).size());
    }

    private BuyTicketRequest buyTicketRequest(String carPlate) {
        return new BuyTicketRequest(carPlate, "12.34", "1234567890123456-123-032028");
    }

}
