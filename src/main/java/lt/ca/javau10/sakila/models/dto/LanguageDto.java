package lt.ca.javau10.sakila.models.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LanguageDto {
	
	private Byte id;

    @NotBlank(message = "Language name is required")
    @Size(min = 2, max = 50, message = "Language name must be between 2 and 50 characters")
    private String name;

    public LanguageDto() {}

    public LanguageDto(Byte id, String name) {
        this.id = id;
        this.name = name;
    }
    
	public Byte getId() {
		return id;
	}

	public void setId(Byte id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	} 
}