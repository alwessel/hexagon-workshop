package de.pentacor.hexagon.workshop.app;

import de.pentacor.hexagon.workshop.adapter.secondary.database.DatabaseService;

public class CheckCarDbTest extends CheckCarTest {

    CheckCarDbTest() {
        this.databaseService = new DatabaseService();
    }
}
