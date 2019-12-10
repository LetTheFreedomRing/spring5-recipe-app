package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.model.UnitOfMeasure;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class UnitOfMeasureServiceImplTest {

    @Mock
    UnitOfMeasureRepository repository;

    @InjectMocks
    UnitOfMeasureServiceImpl service;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new UnitOfMeasureServiceImpl(repository, new UnitOfMeasureToUnitOfMeasureCommand());
    }

    @Test
    public void listAllUoms() {
        Set<UnitOfMeasure> uoms = new HashSet<>();
        UnitOfMeasure uom1 = new UnitOfMeasure();
        uom1.setId(1L);
        UnitOfMeasure uom2 = new UnitOfMeasure();
        uom1.setId(2L);
        uoms.add(uom1);
        uoms.add(uom2);

        Mockito.when(repository.findAll()).thenReturn(uoms);
        Set<UnitOfMeasureCommand> commands = service.listAllUoms();
        assertEquals(uoms.size(), commands.size());
        Mockito.verify(repository, Mockito.times(1)).findAll();
    }
}
