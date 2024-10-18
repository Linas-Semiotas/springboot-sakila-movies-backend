package lt.ca.javau10.sakila.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lt.ca.javau10.sakila.models.Rating;

@Converter
public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return (rating != null) ? rating.getValue() : null;
    }

    @Override
    public Rating convertToEntityAttribute(String value) {
        return (value != null) ? Rating.fromValue(value) : null;
    }
}
