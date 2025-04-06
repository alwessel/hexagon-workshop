package de.pentacor.hexagon.workshop.payment;

import lombok.Getter;

/**
 * Possible errors that may occur when paying.
 */
@Getter
public enum PayError {

    GENERIC_ERROR("An error occurred while paying. Try it again later."),
    CARD_DECLINED("Card was declined. Check the card number or use a different card.");

    private final String description;

    PayError(String description) {
        this.description = description;
    }

    public String getCode() {
        return this.name();
    }

}
