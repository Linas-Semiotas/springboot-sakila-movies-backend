package lt.ca.javau10.sakila.security.responses;

import java.util.List;

public class JwtResponse {
	private String token;
    private String type = "Bearer";
    private String username;
    private List<String> roles;

    // Constructor with fields
    public JwtResponse(String token, String username, String email, List<String> roles) {
        this.token = token;
        this.username = username;
        this.roles = roles;
    }

    // Getters and Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}