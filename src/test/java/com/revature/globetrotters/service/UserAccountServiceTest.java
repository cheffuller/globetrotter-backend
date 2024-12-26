package com.revature.globetrotters.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.revature.globetrotters.entity.UserAccount;
import com.revature.globetrotters.repository.UserAccountRepository;




public class UserAccountServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterSuccess() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        when(userAccountRepository.save(account)).thenReturn(account);

        UserAccount result = accountService.register(account);
        assertEquals(account, result);
    }

    @Test
    public void testRegisterNullAccount() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(null);
        });
        assertEquals("Account is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingUsername() {
        UserAccount account = new UserAccount();
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Username is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingPassword() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Password is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingAddress() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Address is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingCity() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("City is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingCountry() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Country is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingEmail() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setFirstName("First");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Email is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingFirstName() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setLastName("Last");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("First name is required.", exception.getMessage());
    }

    @Test
    public void testRegisterMissingLastName() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Last name is required.", exception.getMessage());
    }

    @Test
    public void testRegisterUsernameTaken() {
        UserAccount account = new UserAccount();
        account.setUsername("newuser");
        account.setPassword("password");
        account.setPasswordSalt("password_salt");
        account.setAddress("123 Street");
        account.setCity("City");
        account.setCountry("Country");
        account.setEmail("email@example.com");
        account.setFirstName("First");
        account.setLastName("Last");

        when(userAccountRepository.findByUsername("newuser")).thenReturn(new UserAccount());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.register(account);
        });
        assertEquals("Username is already taken.", exception.getMessage());
    }

    @Test
    public void testAuthenticateSuccess() {
        UserAccount account = new UserAccount();
        account.setUsername("testuser");
        account.setPassword("password");

        when(userAccountRepository.findByUsername("testuser")).thenReturn(account);

        UserAccount result = accountService.authenticate("testuser", "password");
        assertEquals(account, result);
    }

    @Test
    public void testAuthenticateNullUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.authenticate(null, "password");
        });
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyUsername() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.authenticate("", "password");
        });
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateNullPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.authenticate("testuser", null);
        });
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateEmptyPassword() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            accountService.authenticate("testuser", "");
        });
        assertEquals("Username and password are required.", exception.getMessage());
    }

    @Test
    public void testAuthenticateInvalidCredentials() {
        when(userAccountRepository.findByUsername("testuser")).thenReturn(null);

        UserAccount result = accountService.authenticate("testuser", "password");
        assertEquals(null, result);
    }
}