package es.upm.miw.easygive_spring.business_controllers;

import es.upm.miw.easygive_spring.data_services.DatabaseSeederService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class AdminController {

    private final DatabaseSeederService databaseSeederService;

    @Autowired
    public AdminController(DatabaseSeederService databaseSeederService) {
        this.databaseSeederService = databaseSeederService;
    }

    public void deleteDb() {
        this.databaseSeederService.deleteAllAndInitialize();
    }

    public void seedDataBaseJava() {
        this.databaseSeederService.seedDataBaseJava();
    }
}
