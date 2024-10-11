package lt.ca.javau10.sakila.models.dto;

public class LanguageDto {
	private Byte id;
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