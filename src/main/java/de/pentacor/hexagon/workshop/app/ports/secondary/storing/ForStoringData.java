package de.pentacor.hexagon.workshop.app.ports.secondary.storing;

import de.pentacor.hexagon.workshop.app.model.Ticket;

import java.util.List;

public interface ForStoringData {
    /**
     * Store the given ticket in the repository.
     * If another ticket with the same code exists in the repository already,
     * it is overwriting by the given ticket.
     */
    void saveTicket(Ticket ticket);

    /**
     * Return the stored tickets to park the given car.
     */
    List<Ticket> getTicketsByCar(String carPlate);

    /**
     * Returns and increments, atomically, the next value of the ticket code sequence.
     */
    String nextTicketCode();
}
