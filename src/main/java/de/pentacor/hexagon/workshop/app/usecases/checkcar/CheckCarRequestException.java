package de.pentacor.hexagon.workshop.app.usecases.checkcar;

import java.util.List;

/**
 * Exception thrown if any input data in the "check car" request is not valid.
 * There might be multiple validation errors, hence the list of error messages.
 */
public class CheckCarRequestException extends RuntimeException {


    public CheckCarRequestException(List<String> errorMessages) {
        super("Check car request is not valid.\n" + errorMessages);
    }

}
