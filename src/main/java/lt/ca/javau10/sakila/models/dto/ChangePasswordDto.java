package lt.ca.javau10.sakila.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ChangePasswordDto {

	@NotBlank(message = "Current password is required")
	@Size(min = 8, max = 64, message = "Current password must be between 8 and 64 characters")
	private String currentPassword;

	@NotBlank(message = "New password is required")
	@Size(min = 8, max = 64, message = "New password must be between 8 and 64 characters")
	private String newPassword;

    public ChangePasswordDto() {
    }

    public ChangePasswordDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
    
    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}