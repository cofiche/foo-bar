package io.wahman.kata.writer;

import org.springframework.batch.item.*;
import org.springframework.stereotype.Component;
import io.wahman.kata.Constants.Constants;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;


@Component
public class Writer implements ItemWriter<String>, ItemStream {

    private BufferedWriter writer;
    private boolean initialized = false;

    // Lock for synchronizing file access
    private final ReentrantLock lock = new ReentrantLock();

    // Counter for tracking successful writes
    private long itemsWritten = 0;
    private static final String ITEMS_WRITTEN_COUNT = "items.written.count";

    private static final String OUTPUT_FILE_PATH = Constants.OUTPUT_PATH;


    private void initialize() throws IOException {
        writer = new BufferedWriter(new FileWriter(OUTPUT_FILE_PATH, true)); // Append mode
        initialized = true;
    }

    @Override
    public void write(Chunk<? extends String> items) throws Exception {
        if (items.isEmpty()) {
            return;
        }

        lock.lock();
        try {
            if (!initialized) {
                initialize();
            }

            // Write each string item to the file
            for (String item : items) {
                writer.write(item);
                writer.newLine();
                itemsWritten++;
            }

            // Flush to ensure data is written to disk
            writer.flush();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        lock.lock();
        try {
            // If we're restarting, get the count of items already written
            if (executionContext.containsKey(ITEMS_WRITTEN_COUNT)) {
                itemsWritten = executionContext.getLong(ITEMS_WRITTEN_COUNT);
            }
            initialize();
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize writer", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong(ITEMS_WRITTEN_COUNT, itemsWritten);
    }

    @Override
    public void close() throws ItemStreamException {
        lock.lock();
        try {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    throw new ItemStreamException("Failed to close writer", e);
                }
            }
            initialized = false;
        } finally {
            lock.unlock();
        }
    }
}
