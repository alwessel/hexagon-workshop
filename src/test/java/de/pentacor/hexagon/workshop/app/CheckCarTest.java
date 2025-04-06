package de.pentacor.hexagon.workshop.app;

import de.pentacor.hexagon.workshop.adapter.secondary.database.FakeDatabaseService;
import de.pentacor.hexagon.workshop.adapter.secondary.payment.PaymentService;
import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarRequest;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CheckCarTest {

    private TestClock clock = TestClock.freeze("2024/12/15 11:00");

    private Application application;

    protected ForStoringData databaseService = new FakeDatabaseService();

    @BeforeEach
    void setup() {
        application = new Application(databaseService, new PaymentService(), clock);
    }

    @Test
    void check_car_with_existing_ticket() {
        var carPlate = "DD-GM123";
        var request = checkCarRequest(carPlate);
        saveExistingTicket(carPlate);

        var checkResult = application.checkCar(request);

        assertEquals(carPlate, checkResult.getCarPlate());
        assertEquals(clock.now(), checkResult.getDateTime());
        assertTrue(checkResult.getLegallyParked());
    }

    @Test
    void check_car_without_ticket() {
        var carPlate = "DD-GM123";
        var request = checkCarRequest(carPlate);

        var checkResult = application.checkCar(request);

        assertEquals(carPlate, checkResult.getCarPlate());
        assertFalse(checkResult.getLegallyParked());
    }

    @Test
    void check_car_with_expired_ticket() {
        var carPlate = "DD-GM123";
        saveExistingTicket(carPlate);
        var request = checkCarRequest(carPlate);
        clock.passTime(Duration.ofHours(24));

        var checkResult = application.checkCar(request);

        assertEquals(carPlate, checkResult.getCarPlate());
        assertFalse(checkResult.getLegallyParked());
    }

    private void saveExistingTicket(String carPlate) {
        var ticket = ticket(carPlate);
        databaseService.saveTicket(ticket);
    }

    private CheckCarRequest checkCarRequest(String carPlate) {
        return new CheckCarRequest(carPlate);
    }

    private Ticket ticket(String carPlate) {
        var now = LocalDateTime.now(clock);
        return Ticket.builder()
                .ticketCode("ticketCode")
                .carPlate(carPlate)
                .startingDateTime(now)
                .endingDateTime(now.plusMinutes(60))
                .price(new BigDecimal("12.34"))
                .paymentId("paymentId")
                .build();
    }

}
