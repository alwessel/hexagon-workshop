package de.pentacor.hexagon.workshop.app;

import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCarRequest;
import de.pentacor.hexagon.workshop.db.DatabaseService;
import de.pentacor.hexagon.workshop.db.DbTicket;
import de.pentacor.hexagon.workshop.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CheckCarTest {

    private TestClock clock = TestClock.freeze("2024/12/15 11:00");
    private DatabaseService databaseService = new DatabaseService();

    private Application application;

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
        var dbTicket = dbTicket(carPlate);
        databaseService.saveTicket(dbTicket);
    }

    private CheckCarRequest checkCarRequest(String carPlate) {
        return new CheckCarRequest(carPlate);
    }

    private DbTicket dbTicket(String carPlate) {
        var now = LocalDateTime.now(clock);
        return DbTicket.builder()
                .ticketCode("ticketCode")
                .carPlate(carPlate)
                .startingDateTime(now.toString())
                .endingDateTime(now.plusMinutes(60).toString())
                .price("12.34")
                .paymentId("paymentId")
                .build();
    }

}
