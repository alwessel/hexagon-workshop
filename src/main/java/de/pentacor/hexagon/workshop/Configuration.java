package de.pentacor.hexagon.workshop;

import de.pentacor.hexagon.workshop.adapter.primary.ApplicationCommandLineInterface;
import de.pentacor.hexagon.workshop.adapter.secondary.database.DatabaseService;
import de.pentacor.hexagon.workshop.app.Application;
import de.pentacor.hexagon.workshop.payment.PaymentService;

import java.time.Clock;

public class Configuration {

    public static void main(String[] args) {
        System.out.println("Hello, Hexagon Workshop!");
        var application = new Application(new DatabaseService(), new PaymentService(), Clock.systemDefaultZone());
        new ApplicationCommandLineInterface(application, application).startCommandLoop();
    }

}
