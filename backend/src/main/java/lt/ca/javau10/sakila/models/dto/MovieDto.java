package lt.ca.javau10.sakila.models.dto;

import java.util.List;

import lt.ca.javau10.sakila.models.Rating;

public class MovieDto {
	private Short id;
    private String title;
    private String description;
    private Integer releaseYear;
    private Short filmLength;
    private Rating rating;
    private String language;
    private String category;
    private List<String> actors;
    
	public MovieDto() {}
	
	public MovieDto(Short id, String title, String description, Integer releaseYear, Short filmLength, Rating rating, String language, String category,
			List<String> actors) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.filmLength = filmLength;
		this.rating = rating;
		this.language = language;
		this.category = category;
		this.actors = actors;
	}

	public Short getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public Short getFilmLength() {
		return filmLength;
	}

	public Rating getRating() {
		return rating;
	}

	public String getLanguage() {
		return language;
	}

	public String getCategory() {
		return category;
	}

	public List<String> getActors() {
		return actors;
	}
}
