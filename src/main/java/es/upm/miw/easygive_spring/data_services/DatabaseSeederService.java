package es.upm.miw.easygive_spring.data_services;

import es.upm.miw.easygive_spring.documents.*;
import es.upm.miw.easygive_spring.repositories.*;
import org.apache.logging.log4j.LogManager;
import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.Base64;

@Service
public class DatabaseSeederService {

    @Value("${easygive.admin.username}")
    private String username;
    @Value("${easygive.admin.password}")
    private String password;
    @Value("${easygive.logo}")
    private String logo;

    private Environment environment;
    private UserRepository userRepository;
    private LotRepository lotRepository;


    @Autowired
    public DatabaseSeederService(
            Environment environment,
            UserRepository userRepository,
            LotRepository lotRepository
    ) {
        this.environment = environment;
        this.userRepository = userRepository;
        this.lotRepository = lotRepository;
    }

    @PostConstruct
    public void constructor() {
        String[] profiles = this.environment.getActiveProfiles();
        if (Arrays.asList(profiles).contains("dev")) {
            this.deleteAllAndInitializeAndSeedDataBase();
        } else if (Arrays.asList(profiles).contains("prod")) {
            this.initialize();
        }
    }

    private void initialize() {
        if (!this.userRepository.findByUsername(this.username).isPresent()) {
            LogManager.getLogger(this.getClass()).warn("------- Create Admin -----------");
            User user = User.builder().username(this.username).password(this.password).roles(Role.ADMIN).build();
            this.userRepository.save(user);
        }
    }

    public void deleteAllAndInitialize() {
        LogManager.getLogger(this.getClass()).warn("------- Delete All -----------");
        // Delete Repositories -----------------------------------------------------
        this.userRepository.deleteAll();
        this.lotRepository.deleteAll();
        // -------------------------------------------------------------------------
        this.initialize();
    }

    public void deleteAllAndInitializeAndSeedDataBase() {
        this.deleteAllAndInitialize();
        this.seedDataBaseJava();
    }

    public void seedDataBaseJava() {
        LogManager.getLogger(this.getClass()).warn("------- Initial Load from JAVA -----------");
        Role[] allRoles = {Role.ADMIN, Role.USER};
        User[] users = {
                User.builder().username("all-roles").password("p000").location("C/EasyGive, 0, MIW").email("u000@gmail.com").roles(allRoles).build(),
                User.builder().username("u001").password("p001").location("C/EasyGive, 1").email("u001@gmail.com").roles(Role.USER).build(),
                User.builder().username("u002").password("p002").location("C/EasyGive, 2").email("u002@gmail.com").roles(Role.USER).build(),
                User.builder().username("u003").password("p003").location("C/EasyGive, 3").email("u003@gmail.com").roles(Role.USER).build(),
                User.builder().username("u004").password("p004").location("C/EasyGive, 4").email("u004@gmail.com").roles(Role.USER).build(),
        };
        this.userRepository.saveAll(Arrays.asList(users));
        LogManager.getLogger(this.getClass()).warn("        ------- users");
        Binary binaryLogo = new Binary(BsonBinarySubType.BINARY, Base64.getDecoder().decode(logo));
        User user = this.userRepository.findByUsername("admin").get();
        Lot[] lots = {
                Lot.builder().image(binaryLogo).title("Milk").description("A box of milk").schedule("12:00 - 14:00").wish(false).food(true).delivered(false).user(user).build(),
                Lot.builder().image(binaryLogo).title("Cookies").description("Two cookie packages").schedule("All day").wish(false).food(true).delivered(false).user(user).build(),
                Lot.builder().image(binaryLogo).title("Fan").description("Fan like new").schedule("At the weekends").wish(false).food(false).delivered(false).user(user).build(),
                Lot.builder().image(binaryLogo).title("Books").description("I'm looking for books to read").schedule("In the morning").wish(true).food(false).delivered(false).user(user).build()
        };
        this.lotRepository.saveAll(Arrays.asList(lots));
        LogManager.getLogger(this.getClass()).warn("        ------- lots");
    }
}



