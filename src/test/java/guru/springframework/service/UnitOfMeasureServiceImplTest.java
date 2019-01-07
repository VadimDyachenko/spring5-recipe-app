package guru.springframework.service;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository repository;
    private UnitOfMeasureToUnitOfMeasureCommand converter = new UnitOfMeasureToUnitOfMeasureCommand();
    private UnitOfMeasureService service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(repository, converter);
    }

    @Test
    public void listAllUom() {
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom2.setId(2L);

        Set<UnitOfMeasure> unitOfMeasures = new HashSet<>(Arrays.asList(uom1, uom2));

        when(repository.findAll()).thenReturn(unitOfMeasures);

        Set<UnitOfMeasureCommand> result = service.listAllUom();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(repository, times(1)).findAll();
    }
}