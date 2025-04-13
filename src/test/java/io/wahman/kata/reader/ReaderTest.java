package io.wahman.kata.reader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ReaderTest {

    private Reader reader;
    private ExecutionContext executionContext;
    private int fileLineCount;


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        File testFile = new ClassPathResource("input/inputFile.txt").getFile();
        List<String> lines = Files.readAllLines(testFile.toPath());
        fileLineCount = lines.size();
        reader = new Reader();
        executionContext = new ExecutionContext();
    }

    @Test
    void testReadAllItems() throws Exception {
        reader.open(executionContext);
        int itemCount = 0;
        while (reader.read() != null) {
            itemCount++;
        }
        assertEquals(fileLineCount, itemCount, "Reader should read exactly the same number of lines as in the file");
    }

}
