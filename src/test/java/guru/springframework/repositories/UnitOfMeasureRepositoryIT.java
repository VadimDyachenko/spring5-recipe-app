package guru.springframework.repositories;

import guru.springframework.domain.UnitOfMeasure;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UnitOfMeasureRepositoryIT {

    @Autowired
    private UnitOfMeasureRepository repository;

    @Test
    public void findByDescriptionTeaspoon() {
        Optional<UnitOfMeasure> uomOptional = repository.findByDescription("Teaspoon");
        assertTrue(uomOptional.isPresent());
        assertEquals("Teaspoon", uomOptional.get().getDescription());
    }

    @Test
    public void findByDescriptionCup() {
        Optional<UnitOfMeasure> uomOptional = repository.findByDescription("Cup");
        assertTrue(uomOptional.isPresent());
        assertEquals("Cup", uomOptional.get().getDescription());
    }
}