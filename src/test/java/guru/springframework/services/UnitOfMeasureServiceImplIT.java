package guru.springframework.services;

import guru.springframework.commands.UnitOfMeasureCommand;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UnitOfMeasureServiceImplIT {

    @Autowired
    UnitOfMeasureService unitOfMeasureService;

    @Before
    public void setUp() {

    }

    @Test
    public void listAllUoms() {
        Set<UnitOfMeasureCommand> commands = unitOfMeasureService.listAllUoms();
        // added 7 uoms in data.sql
        assertEquals(7, commands.size());
    }

}
