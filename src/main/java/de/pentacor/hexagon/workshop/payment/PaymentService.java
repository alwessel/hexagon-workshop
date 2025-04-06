package de.pentacor.hexagon.workshop.payment;


import java.util.Objects;
import java.util.Random;

public class PaymentService {

    private String paymentId = "PID123";

    /**
     * Charge the euros amount to the card,
     * and return the identifier of the payment made.
     * If an error occurs, a {@link PayErrorException} is thrown
     * with information about the error.
     * Possible errors that could occur are listed in {@link PayError}.
     */
    public String pay(PayRequest request) {
        Objects.requireNonNull(request);
        Objects.requireNonNull(request.getEuros());
        Objects.requireNonNull(request.getCard());

        // simulate a flaky payment service
        if (new Random().nextInt(100) < 20) {
            throw new IllegalStateException("Flaky PaymentService failed, try again.");
        }
        return paymentId;
    }
}
