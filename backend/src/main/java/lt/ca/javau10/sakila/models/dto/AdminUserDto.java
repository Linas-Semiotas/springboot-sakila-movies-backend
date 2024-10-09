package lt.ca.javau10.sakila.models.dto;

public class AdminUserDto {
    private int userId;
    private String username;
    private boolean userRole;
    private boolean adminRole;
    private boolean enabled;

    public AdminUserDto() {}

	public AdminUserDto(int userId, String username, boolean userRole, boolean adminRole, boolean enabled) {
		this.userId = userId;
		this.username = username;
		this.userRole = userRole;
		this.adminRole = adminRole;
		this.enabled = enabled;
	}
	
	public AdminUserDto(boolean userRole, boolean adminRole, boolean enabled) {
		super();
		this.userRole = userRole;
		this.adminRole = adminRole;
		this.enabled = enabled;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isUserRole() {
		return userRole;
	}

	public void setUserRole(boolean userRole) {
		this.userRole = userRole;
	}

	public boolean isAdminRole() {
		return adminRole;
	}

	public void setAdminRole(boolean adminRole) {
		this.adminRole = adminRole;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
