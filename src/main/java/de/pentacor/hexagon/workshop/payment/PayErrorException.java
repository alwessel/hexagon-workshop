package de.pentacor.hexagon.workshop.payment;

import lombok.Getter;
import lombok.ToString;

/**
 * Exception thrown by the "pay" method if an error occurs.
 * It holds the code of the error, and its description as the message of the exception.
 */
@Getter
@ToString
public class PayErrorException extends RuntimeException {

    private final String errorCode;

    public PayErrorException(PayError payError) {
        super(payError.getDescription());
        this.errorCode = payError.getCode();
    }

}
