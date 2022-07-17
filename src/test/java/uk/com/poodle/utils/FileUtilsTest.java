package uk.com.poodle.utils;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static uk.com.poodle.utils.FileUtils.fileToString;

class FileUtilsTest {

    @Test
    void classCanNotBeInstantiated() throws NoSuchMethodException {
        var constructor = FileUtils.class.getDeclaredConstructor();
        assertTrue(Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        assertThrows(InvocationTargetException.class, constructor::newInstance);
    }

    @Test
    void shouldReadContentsOfFileAsString() {
        assertEquals("foo\n", fileToString("test.txt", getClass()));
    }

    @Test
    void shouldPropagateExceptionsReadingContentsOfFileAsString() {
        assertThrows(NullPointerException.class, () -> fileToString(null, getClass()));
    }
}
