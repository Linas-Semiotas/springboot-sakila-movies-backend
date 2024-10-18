package lt.ca.javau10.sakila.repositories;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import lt.ca.javau10.sakila.models.Actor;

import java.util.Optional;

@DataJpaTest
public class ActorRepositoryTest {

    @Autowired
    private ActorRepository actorRepository;

    @Test
    public void testExistsByFirstNameAndLastName() {
        // Given: Create and save a new actor
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");
        actorRepository.save(actor);

        // When: Check if actor exists by first and last name
        boolean exists = actorRepository.existsByFirstNameAndLastName("Tom", "Hanks");

        // Then: Verify the actor exists
        assertTrue(exists, "Actor should exist in the database");
    }

    @Test
    public void testDoesNotExistByFirstNameAndLastName() {
        // When: Check for an actor that doesn't exist
        boolean exists = actorRepository.existsByFirstNameAndLastName("Non", "Existent");

        // Then: Verify the actor doesn't exist
        assertFalse(exists, "Non-existent actor should not be found");
    }

    @Test
    public void testFindByFirstNameAndLastName() {
        // Given: Create and save a new actor
        Actor actor = new Actor();
        actor.setFirstName("Tom");
        actor.setLastName("Hanks");
        actorRepository.save(actor);

        // When: Retrieve the actor by first and last name
        Optional<Actor> foundActor = actorRepository.findByFirstNameAndLastName("Tom", "Hanks");

        // Then: Verify the actor was found and the details are correct
        assertTrue(foundActor.isPresent(), "Actor should be found");
        assertEquals("Tom", foundActor.get().getFirstName());
        assertEquals("Hanks", foundActor.get().getLastName());
    }

    @Test
    public void testFindByFirstNameAndLastName_NotFound() {
        // When: Try to find an actor that doesn't exist
        Optional<Actor> foundActor = actorRepository.findByFirstNameAndLastName("Non", "Existent");

        // Then: Verify the actor is not found
        assertFalse(foundActor.isPresent(), "Actor should not be found");
    }
}
