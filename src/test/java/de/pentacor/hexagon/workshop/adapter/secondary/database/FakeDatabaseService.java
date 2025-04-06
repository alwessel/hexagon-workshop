package de.pentacor.hexagon.workshop.adapter.secondary.database;

import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;

import java.util.ArrayList;
import java.util.List;

public class FakeDatabaseService implements ForStoringData {

    private List<Ticket> tickets = new ArrayList<>();

    @Override
    public void saveTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    @Override
    public List<Ticket> getTicketsByCar(String carPlate) {
        return tickets.stream()
                .filter(t -> t.getCarPlate().equals(carPlate))
                .toList();
    }

    @Override
    public String nextTicketCode() {
        return String.valueOf(tickets.size() + 1);
    }
}
