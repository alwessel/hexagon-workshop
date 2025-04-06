package de.pentacor.hexagon.workshop.payment;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Input data needed for the 'pay' method:
 * - Euros amount to be paid.
 * - Card used for paying, in the format 'n-c-mmyyyy', where
 * 'n' is the card number (16 digits)
 * 'c' is the verification code (3 digits),
 * 'mmyyyy' is the expiration month and year (6 digits)
 */
@Data
public class PayRequest {

    private final BigDecimal euros;
    private final String card;

}
