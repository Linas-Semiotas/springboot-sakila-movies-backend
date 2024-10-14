package lt.ca.javau10.sakila.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lt.ca.javau10.sakila.models.User;
import lt.ca.javau10.sakila.models.dto.AddressInfoDto;
import lt.ca.javau10.sakila.models.dto.ChangePasswordDto;
import lt.ca.javau10.sakila.models.dto.OrdersDto;
import lt.ca.javau10.sakila.models.dto.PersonalInfoDto;
import lt.ca.javau10.sakila.repositories.UserRepository;
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService service;
    
    @Autowired
    private UserRepository userRepository;

    //ORDERS
    
    @GetMapping("/orders")
    public ResponseEntity<List<OrdersDto>> getOrdersForCurrentUser(Authentication authentication) {
        // Get the username from the authenticated user
        String username = authentication.getName();

        // Find the custom User entity from your database by username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Fetch orders for the current user using their ID
        List<OrdersDto> orders = service.getOrdersForUser(user.getUserId());

        return ResponseEntity.ok(orders);
    }
    
    //BALANCE
    
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(Principal principal) {
        Double balance = service.getUserBalance(principal.getName());
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/balance/add")
    public ResponseEntity<Double> addBalance(@RequestBody Map<String, Double> request, Principal principal) {
        Double amount = request.get("amount");
        Double newBalance = service.addBalance(principal.getName(), amount);
        return ResponseEntity.ok(newBalance);
    }
    
    //PROFILE
    
    @GetMapping("/profile/personal-info")
    public ResponseEntity<PersonalInfoDto> getPersonalInfo(Principal principal) {
        String username = principal.getName();
        int userId = service.getUserIdByUsername(username);
        PersonalInfoDto personalInfo = service.getPersonalInfo(userId);
        return ResponseEntity.ok(personalInfo);
    }

    @PutMapping("/profile/personal-info")
    public ResponseEntity<String> updatePersonalInfo(@RequestBody PersonalInfoDto personalInfoDto, Principal principal) {
        String username = principal.getName();
        int userId = service.getUserIdByUsername(username);
        service.updatePersonalInfo(userId, personalInfoDto);
        return ResponseEntity.ok("Personal information updated successfully");
    }
    
    @GetMapping("/profile/address-info")
    public ResponseEntity<AddressInfoDto> getAddressInfo(Principal principal) {
    	String username = principal.getName();
        int userId = service.getUserIdByUsername(username);
        AddressInfoDto addressInfo = service.getAddressInfo(userId);
        return ResponseEntity.ok(addressInfo);
    }

    @PutMapping("/profile/address-info")
    public ResponseEntity<String> updateAddressInfo(@RequestBody AddressInfoDto addressInfoDto, Principal principal) {
    	String username = principal.getName();
        int userId = service.getUserIdByUsername(username);
        service.updateAddressInfo(userId, addressInfoDto);
        return ResponseEntity.ok("Address information updated successfully");
    }
    
    //SECURITY
    
    @PostMapping("/security/change-password")
    public ResponseEntity<?> changePassword(@RequestBody ChangePasswordDto changePasswordDto, Principal principal) {
        try {
            String username = principal.getName();
            service.changePassword(username, changePasswordDto.getCurrentPassword(), changePasswordDto.getNewPassword());
            return ResponseEntity.ok(new MessageResponse("Password updated successfully"));
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new MessageResponse("Current password is incorrect"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new MessageResponse("Error updating password"));
        }
    }
}
