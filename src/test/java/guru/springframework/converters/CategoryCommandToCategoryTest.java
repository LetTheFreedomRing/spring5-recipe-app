package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.model.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryCommandToCategoryTest {

    CategoryCommandToCategory converter;
    final static Long categoryId = 1L;
    final static String categoryDescription = "Dummy description";

    @Before
    public void setUp() throws Exception {
        converter = new CategoryCommandToCategory();
    }

    @Test
    public void convert() {
        CategoryCommand command = new CategoryCommand();
        command.setId(categoryId);
        command.setDescription(categoryDescription);
        Category category = converter.convert(command);
        assertNotNull(category);
        assertEquals(command.getId(), category.getId());
        assertEquals(command.getDescription(), category.getDescription());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}