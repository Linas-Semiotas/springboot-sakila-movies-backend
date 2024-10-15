package lt.ca.javau10.sakila.models.dto;

import java.math.BigDecimal;
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
    private List<String> category;
    private String specialFeatures;
    private BigDecimal rentalRate;
    private BigDecimal replacementCost;
    private Short rentalDuration;
    private List<String> actors;
    
	public MovieDto() {}
	
	public MovieDto(Short id) {
		this.id = id;
	}
	
	public MovieDto(Short id, String title, String description, Integer releaseYear, Short filmLength, Rating rating,
			String language, List<String> category, String specialFeatures, List<String> actors) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.filmLength = filmLength;
		this.rating = rating;
		this.language = language;
		this.category = category;
		this.specialFeatures = specialFeatures;
		this.actors = actors;
	}
	
	public MovieDto(Short id, String title, String description, Integer releaseYear, Short filmLength, Rating rating,
			String language, List<String> category, String specialFeatures, BigDecimal rentalRate, BigDecimal replacementCost,
			Short rentalDuration, List<String> actors) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.releaseYear = releaseYear;
		this.filmLength = filmLength;
		this.rating = rating;
		this.language = language;
		this.category = category;
		this.specialFeatures = specialFeatures;
		this.rentalRate = rentalRate;
		this.replacementCost = replacementCost;
		this.rentalDuration = rentalDuration;
		this.actors = actors;
	}

	public Short getId() {
		return id;
	}

	public void setId(Short id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(Integer releaseYear) {
		this.releaseYear = releaseYear;
	}

	public Short getFilmLength() {
		return filmLength;
	}

	public void setFilmLength(Short filmLength) {
		this.filmLength = filmLength;
	}

	public Rating getRating() {
		return rating;
	}

	public void setRating(Rating rating) {
		this.rating = rating;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public List<String> getCategory() {
		return category;
	}

	public void setCategory(List<String> category) {
		this.category = category;
	}

	public String getSpecialFeatures() {
		return specialFeatures;
	}

	public void setSpecialFeatures(String specialFeatures) {
		this.specialFeatures = specialFeatures;
	}

	public BigDecimal getRentalRate() {
		return rentalRate;
	}

	public void setRentalRate(BigDecimal rentalRate) {
		this.rentalRate = rentalRate;
	}

	public BigDecimal getReplacementCost() {
		return replacementCost;
	}

	public void setReplacementCost(BigDecimal replacementCost) {
		this.replacementCost = replacementCost;
	}

	public Short getRentalDuration() {
		return rentalDuration;
	}

	public void setRentalDuration(Short rentalDuration) {
		this.rentalDuration = rentalDuration;
	}

	public List<String> getActors() {
		return actors;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}
	
	@Override
	public String toString() {
	    return "MovieDto{" +
	            "id=" + id +
	            ", title='" + title + '\'' +
	            ", description='" + description + '\'' +
	            ", releaseYear=" + releaseYear +
	            ", filmLength=" + filmLength +
	            ", rating=" + rating +
	            ", language='" + language + '\'' +
	            ", category=" + category +  // No single quotes around category, it will now print as a list
	            ", specialFeatures='" + specialFeatures + '\'' +
	            ", rentalRate=" + rentalRate +
	            ", replacementCost=" + replacementCost +
	            ", rentalDuration=" + rentalDuration +
	            ", actors=" + actors +  // Same for actors
	            '}';
	}
}
