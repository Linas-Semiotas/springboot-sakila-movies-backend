package lt.ca.javau10.sakila.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
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
import lt.ca.javau10.sakila.security.responses.MessageResponse;
import lt.ca.javau10.sakila.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {
    
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    //ORDERS
    
    @GetMapping("/orders")
    public ResponseEntity<List<OrdersDto>> getOrdersForCurrentUser(Authentication authentication) {
        String username = authentication.getName();
        List<OrdersDto> orders = service.getOrdersForUser(username);
        return ResponseEntity.ok(orders);
    }
    
    //BALANCE
    
    @GetMapping("/balance")
    public ResponseEntity<Double> getBalance(Principal principal) {
        Double balance = service.getUserBalance(principal.getName());
        return ResponseEntity.ok(balance);
    }

    @PostMapping("/balance/add")
    public ResponseEntity<?> addBalance(@RequestBody Map<String, Double> request, Principal principal) {
        Double amount = request.get("amount");
        Double newBalance = service.addBalance(principal.getName(), amount);
        return ResponseEntity.ok(newBalance);
    }
    
    //PROFILE
    
    @GetMapping("/profile/personal-info")
    public ResponseEntity<PersonalInfoDto> getPersonalInfo(Principal principal) {
        User user = service.getUserByUsername(principal.getName());
        PersonalInfoDto personalInfo = service.getPersonalInfo(user.getUserId());
        return ResponseEntity.ok(personalInfo);
    }

    @PutMapping("/profile/personal-info")
    public ResponseEntity<MessageResponse> updatePersonalInfo(@RequestBody PersonalInfoDto personalInfoDto, Principal principal) {
        User user = service.getUserByUsername(principal.getName());
        service.updatePersonalInfo(user.getUserId(), personalInfoDto);
        return ResponseEntity.ok(new MessageResponse("Personal information updated successfully"));
    }

    @GetMapping("/profile/address-info")
    public ResponseEntity<AddressInfoDto> getAddressInfo(Principal principal) {
        User user = service.getUserByUsername(principal.getName());
        AddressInfoDto addressInfo = service.getAddressInfo(user.getUserId());
        return ResponseEntity.ok(addressInfo);
    }

    @PutMapping("/profile/address-info")
    public ResponseEntity<MessageResponse> updateAddressInfo(@RequestBody AddressInfoDto addressInfoDto, Principal principal) {
        User user = service.getUserByUsername(principal.getName());
        service.updateAddressInfo(user.getUserId(), addressInfoDto);
        return ResponseEntity.ok(new MessageResponse("Address information updated successfully"));
    }
    
    //SECURITY
    
    @PostMapping("/security/change-password")
    public ResponseEntity<MessageResponse> changePassword(@RequestBody ChangePasswordDto changePasswordDto, Principal principal) {
        service.changePassword(principal.getName(), changePasswordDto.getCurrentPassword(), changePasswordDto.getNewPassword());
        return ResponseEntity.ok(new MessageResponse("Password changed successfully"));
    }
}
