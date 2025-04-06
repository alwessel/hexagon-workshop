package de.pentacor.hexagon.workshop.app.usecases.checkcar;

import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarRequest;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarRequestException;
import de.pentacor.hexagon.workshop.app.ports.primary.fines.CheckCarResult;
import de.pentacor.hexagon.workshop.app.ports.secondary.storing.ForStoringData;
import lombok.RequiredArgsConstructor;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class CheckCar implements Function<CheckCarRequest, CheckCarResult> {

    private final ForStoringData dataRepository;
    private final Clock clock;

    @Override
    public CheckCarResult apply(CheckCarRequest request) {
        List<String> requestErrors = request.validate();
        if (!requestErrors.isEmpty()) {
            throw new CheckCarRequestException(requestErrors);
        }
        String carPlate = request.getCarPlate();
        LocalDateTime currentDateTime = LocalDateTime.now(this.clock);
        var ticketsForCar = this.dataRepository.getTicketsByCar(carPlate).stream()
                .toList();

        var ticketsIterator = ticketsForCar.iterator();
        boolean activeTicketFound = false;
        while (!activeTicketFound && ticketsIterator.hasNext()) {
            var ticket = ticketsIterator.next();
            if (ticket.isValidAt(currentDateTime)) {
                activeTicketFound = true;
            }
        }
        return new CheckCarResult(carPlate, currentDateTime, activeTicketFound);
    }

}
