package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.model.Notes;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class NotesToNotesCommandTest {

    NotesCommandToNotes converter;
    static final Long notesId = 1L;
    static final String notesDescription = "dummy";

    @Before
    public void setUp() throws Exception {
        converter = new NotesCommandToNotes();
    }

    @Test
    public void convert() {
        NotesCommand command = new NotesCommand();
        command.setId(notesId);
        command.setRecipeNotes(notesDescription);
        Notes notes = converter.convert(command);
        assertNotNull(notes);
        assertEquals(command.getId(), notes.getId());
        assertEquals(command.getRecipeNotes(), notes.getRecipeNotes());
    }

    @Test
    public void convertNull() {
        assertNull(converter.convert(null));
    }
}