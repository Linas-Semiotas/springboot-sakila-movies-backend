package lt.ca.javau10.sakila.models;

import jakarta.persistence.*;

@Entity
@Table(name = "language")
public class Language {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "language_id")
    private Byte languageId;

    private String name;
    
    public Language() {}
    
    public Language(String name) {
        this.name = name;
    }

	public Byte getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Byte id) {
		this.languageId = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
