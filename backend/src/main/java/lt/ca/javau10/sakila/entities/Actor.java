package lt.ca.javau10.sakila.entities;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "actor")
public class Actor {

    @Id
    @Column(name = "actor_id")
    private Short actorId;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;

    @ManyToMany(mappedBy = "actors")
    private List<Movie> films;

	public Short getActorId() {
		return actorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public List<Movie> getFilms() {
		return films;
	}

	public void setActorId(Short actorId) {
		this.actorId = actorId;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setFilms(List<Movie> films) {
		this.films = films;
	}
	
	
}
