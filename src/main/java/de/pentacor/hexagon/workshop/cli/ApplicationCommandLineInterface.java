package de.pentacor.hexagon.workshop.cli;

import de.pentacor.hexagon.workshop.app.Application;
import de.pentacor.hexagon.workshop.app.usecases.buyticket.BuyTicketRequest;
import de.pentacor.hexagon.workshop.app.usecases.checkcar.CheckCarRequest;

import java.util.Scanner;

public class ApplicationCommandLineInterface {

    private final Application application = new Application();

    public static void main(String[] args) {
        System.out.println("Hello, Hexagon Workshop!");
        new ApplicationCommandLineInterface().startCommandLoop();
    }

    private void startCommandLoop() {
        Scanner scanner = new Scanner(System.in);
        boolean exit = false;

        while (!exit) {
            System.out.println("\nWhat to do:");
            System.out.println("1. buy a ticket");
            System.out.println("2. check a parked car");
            System.out.println("3. program exit");
            System.out.print("Your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    requestTicket();
                    break;
                case "2":
                    checkCar();
                    break;
                case "3":
                    exit = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void requestTicket() {
        try {
            System.out.print("Buy a ticket...");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter car plate (DD-GA1123): ");
            String carPlate = scanner.nextLine();
            System.out.print("Enter euro amount (10.95): ");
            String euroAmount = scanner.nextLine();
            System.out.print("Enter credit card (1234567890123456-123-032028): ");
            String cardNo = scanner.nextLine();

            var request = new BuyTicketRequest(carPlate, euroAmount, cardNo);
            var ticket = application.buyTicket(request);
            System.out.println("\nYou got yourself a ticket: " + ticket);
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }

    private void checkCar() {
        try {
            System.out.print("Check a parked car...");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter car plate (DD-GA1123): ");
            String carPlate = scanner.nextLine();

            var request = new CheckCarRequest(carPlate);
            var checkCarResult = application.checkCar(request);
            System.out.println("\nCar check result: " + checkCarResult);
        } catch (Exception e) {
            System.out.println("\nError: " + e.getMessage());
        }
    }
}
