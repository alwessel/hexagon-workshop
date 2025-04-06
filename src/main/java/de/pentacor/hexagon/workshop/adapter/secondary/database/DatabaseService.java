package de.pentacor.hexagon.workshop.adapter.secondary.database;

import de.pentacor.hexagon.workshop.app.model.Ticket;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DatabaseService implements ForStoringData {

    private static final String SEPARATOR = ";";
    private static final int DB_DELAY_MS = 3_000;
    private final Path csvFile = createTempFile();

    private int nextTicketCode = 0;

    @Override
    public void saveTicket(Ticket ticket) {
        Objects.requireNonNull(ticket);
        writeTicketToFile(mapFromTicket(ticket));
    }

    private DbTicket mapFromTicket(Ticket ticket) {
        return DbTicket.builder()
                .ticketCode(ticket.getTicketCode())
                .carPlate(ticket.getCarPlate())
                .startingDateTime(ticket.getStartingDateTime().toString())
                .endingDateTime(ticket.getEndingDateTime().toString())
                .price(ticket.getPrice().toString())
                .paymentId(ticket.getPaymentId())
                .build();
    }

    @Override
    public List<Ticket> getTicketsByCar(String carPlate) {
        return readTicketsFromCsv().stream()
                .filter(t -> t.getCarPlate().equals(carPlate))
                .map(this::mapToTicket)
                .toList();
    }

    private Ticket mapToTicket(DbTicket db) {
        return Ticket.builder()
                .ticketCode(db.getTicketCode())
                .carPlate(db.getCarPlate())
                .startingDateTime(LocalDateTime.parse(db.getStartingDateTime()))
                .endingDateTime(LocalDateTime.parse(db.getEndingDateTime()))
                .price(new BigDecimal(db.getPrice()))
                .paymentId(db.getPaymentId())
                .build();
    }


    @Override
    public String nextTicketCode() {
        return String.valueOf(nextTicketCode++);
    }

    private List<DbTicket> readTicketsFromCsv() {
        try {
            // simulate a slow DB
            Thread.sleep(DB_DELAY_MS);

            return Files.readAllLines(csvFile)
                    .stream().map(line -> {
                        String[] fields = line.split(SEPARATOR);
                        return DbTicket.builder()
                                .ticketCode(fields[0])
                                .carPlate(fields[1])
                                .startingDateTime(fields[2])
                                .endingDateTime(fields[3])
                                .price(fields[4])
                                .paymentId(fields[5])
                                .build();
                    }).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void writeTicketToFile(DbTicket ticket) {
        try {
            // simulate a slow DB
            Thread.sleep(DB_DELAY_MS);

            String row = ticket.getTicketCode() + SEPARATOR +
                    ticket.getCarPlate() + SEPARATOR +
                    ticket.getStartingDateTime() + SEPARATOR +
                    ticket.getEndingDateTime() + SEPARATOR +
                    ticket.getPrice() + SEPARATOR +
                    ticket.getPaymentId() + '\n';
            Files.writeString(csvFile, row, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Path createTempFile() {
        try {
            var csv = Files.createTempFile("tickets_", ".csv");
            log.debug("Ticket csv file: {}", csv);
            csv.toFile().deleteOnExit();
            return csv;
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp file", e);
        }
    }
}
