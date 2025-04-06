package de.pentacor.hexagon.workshop.db;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DatabaseService {

    private static final String SEPARATOR = ";";
    private static final int DB_DELAY_MS = 3_000;
    private final Path csvFile = createTempFile();

    private int nextTicketCode = 0;

    /**
     * Store the given ticket in the repository.
     * If another ticket with the same code exists in the repository already,
     * it is overwriting by the given ticket.
     */
    public void saveTicket(DbTicket ticket) {
        Objects.requireNonNull(ticket);
        writeTicketToFile(ticket);
    }

    /**
     * Return the stored tickets to park the given car.
     */
    public List<DbTicket> getTicketsByCar(String carPlate) {
        return readTicketsFromCsv().stream()
                .filter(t -> t.getCarPlate().equals(carPlate))
                .toList();
    }

    /**
     * Returns and increments, atomically, the next value of the ticket code sequence.
     */
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
