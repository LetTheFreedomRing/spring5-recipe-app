package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.model.Category;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CategoryToCategoryCommandTest {

    CategoryToCategoryCommand converter;
    final static Long categoryId = 1L;
    final static String categoryDescription = "Dummy description";

    @Before
    public void setUp() throws Exception {
        converter = new CategoryToCategoryCommand();
    }

    @Test
    public void convert() {
        Category category = new Category();
        category.setId(categoryId);
        category.setDescription(categoryDescription);
        CategoryCommand command = converter.convert(category);
        assertNotNull(command);
        assertEquals(category.getId(), command.getId());
        assertEquals(category.getDescription(), command.getDescription());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}