package com.revature.globetrotters.repository;

import com.revature.globetrotters.GlobeTrottersApplication;
import com.revature.globetrotters.entity.ModeratorAccount;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

@SpringBootTest
public class ModeratorAccountTests {
    @Autowired
    ModeratorAccountRepository moderatorAccountRepository;
    ApplicationContext app;

    @BeforeEach
    public void setUp() throws InterruptedException {
        String[] args = new String[]{};
        app = SpringApplication.run(GlobeTrottersApplication.class, args);
        Thread.sleep(500);
    }

    @AfterEach
    public void tearDown() throws InterruptedException {
        Thread.sleep(500);
        SpringApplication.exit(app);
    }

    @ParameterizedTest
    @CsvSource({
            "10, 'admin@gmail.com', 'Barry', 'Allen', 'password1', 'password1_salt', 'admin'"
    })
    public void findModeratorAccount(Integer id, String email, String firstName, String lastName, String password,
                                     String passwordSalt, String username) {

        ModeratorAccount expectedAccount = new ModeratorAccount(id, email, firstName, lastName, password,
                passwordSalt, username);
        Optional<ModeratorAccount> actualAccount = moderatorAccountRepository.findById(10);
        Assertions.assertEquals(expectedAccount, actualAccount.get(),
                "Expected: " + expectedAccount + ". Actual: " + actualAccount);
    }
}
