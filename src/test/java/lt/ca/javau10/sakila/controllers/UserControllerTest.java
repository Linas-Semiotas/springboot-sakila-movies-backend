package lt.ca.javau10.sakila.controllers;

import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.*;
import lt.ca.javau10.sakila.services.UserService;
import lt.ca.javau10.sakila.security.utils.JwtUtil;
import lt.ca.javau10.sakila.services.CustomUserDetailsService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false) // Disable security filters for test
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetOrdersForCurrentUser() throws Exception {
        List<OrdersDto> orders = Arrays.asList(
                new OrdersDto(1, LocalDateTime.now(), LocalDateTime.now().plusDays(7), "Order1"),
                new OrdersDto(2, LocalDateTime.now(), LocalDateTime.now().plusDays(5), "Order2")
        );

        Authentication authentication = Mockito.mock(Authentication.class);
        when(authentication.getName()).thenReturn("user");
        when(userService.getOrdersForUser("user")).thenReturn(orders);

        mockMvc.perform(get("/api/user/orders")
                .principal(authentication)) // Inject Authentication here
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Order1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Order2"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetBalance() throws Exception {
    	Principal mockPrincipal = () -> "user";
        when(userService.getUserBalance("user")).thenReturn(500.0);

        mockMvc.perform(get("/api/user/balance")
        		.principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(500.0));
    }

    @Test
    @WithMockUser(username = "user")
    public void testAddBalance() throws Exception {
    	Map<String, Double> request = Map.of("amount", 100.0);
        when(userService.addBalance("user", 100.0)).thenReturn(600.0);

        Principal mockPrincipal = () -> "user"; // Mock Principal

        mockMvc.perform(post("/api/user/balance/add")
                .principal(mockPrincipal) // Pass the mock Principal
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(600.0));
    }

    @Test
    @WithMockUser(username = "user")
    public void testGetPersonalInfo() throws Exception {
        // Mocking User and setting a valid userId
        User mockUser = new User();
        mockUser.setCustomerId(1);  // Assuming the userId is 1

        // Mocking the PersonalInfoDto to be returned
        PersonalInfoDto personalInfoDto = new PersonalInfoDto("John", "Doe", "john.doe@example.com", "555-1234");

        // Mocking the service calls
        when(userService.getUserByUsername("user")).thenReturn(mockUser);
        when(userService.getPersonalInfo(1)).thenReturn(personalInfoDto);  // Mocking the return of PersonalInfoDto

        Principal mockPrincipal = () -> "user"; // Mock Principal

        mockMvc.perform(get("/api/user/profile/personal-info").principal(mockPrincipal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.phone").value("555-1234"));
    }

    // Test UpdatePersonalInfo with Principal Mock
    @Test
    @WithMockUser(username = "user")
    public void testUpdatePersonalInfo() throws Exception {
        // Mocking User and setting a valid userId
        User mockUser = new User();
        mockUser.setCustomerId(1);  // Assuming the userId is 1

        // Mocking the service calls
        PersonalInfoDto personalInfoDto = new PersonalInfoDto("John", "Doe", "john.doe@example.com", "555-1234");
        doNothing().when(userService).updatePersonalInfo(1, personalInfoDto);

        when(userService.getUserByUsername("user")).thenReturn(mockUser);

        Principal mockPrincipal = () -> "user"; // Mock Principal

        mockMvc.perform(put("/api/user/profile/personal-info")
                .principal(mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(personalInfoDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Personal information updated successfully"));
    }

    @Test
    @WithMockUser(username = "user")
    public void testChangePassword() throws Exception {
    	ChangePasswordDto changePasswordDto = new ChangePasswordDto("oldPassword", "newPassword");
        User user = new User();
        user.setPassword(passwordEncoder.encode("oldPassword"));

        when(userService.getUserByUsername("user")).thenReturn(user);
        doNothing().when(userService).changePassword("user", "oldPassword", "newPassword");

        Principal mockPrincipal = () -> "user"; // Mock Principal

        mockMvc.perform(post("/api/user/security/change-password")
                .principal(mockPrincipal) // Pass the mock Principal
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(changePasswordDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password changed successfully"));
    }
}
