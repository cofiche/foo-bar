package io.wahman.kata.reader;

import org.springframework.batch.item.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;



@Component
public class Reader implements ItemReader<Integer>, ItemStream {

    private BufferedReader bufferedReader;
    private final AtomicLong lineCount = new AtomicLong(0);
    private boolean initialized = false;

    // Lock for synchronizing file access
    private final ReentrantLock lock = new ReentrantLock();

    // Key used to store state in the execution context
    private static final String CURRENT_LINE_COUNT = "current.line.count";

    @Value("${app.file.input-path}")
    private String filePath;


    private void initialize() throws IOException {
        bufferedReader = new BufferedReader(new FileReader(filePath));
        initialized = true;
    }

    @Override
    public Integer read() throws Exception {

        Integer result = null;

        // Synchronize access to the file
        lock.lock();
        try {
            if (!initialized) {
                initialize();
            }

            String line = bufferedReader.readLine();

            // Return null when end of file is reached (signals end of data to Spring Batch)
            if (line == null) {
                return null;
            }

            // Increment line count for each successful read
            lineCount.incrementAndGet();

            try {
                result = Integer.parseInt(line.trim());
            } catch (NumberFormatException e) {
                throw new ParseException("Unable to parse line as integer: " + line, e);
            }
        } finally {
            lock.unlock();
        }

        return result;

    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        lock.lock();
        try {
            initialize();

            // restore state, if it exists
            if (executionContext.containsKey(CURRENT_LINE_COUNT)) {
                long savedLineCount = executionContext.getLong(CURRENT_LINE_COUNT);
                lineCount.set(savedLineCount);

                // Skip to non read files
                // It's important in case of restart
                for (int i = 0; i < savedLineCount; i++) {
                    bufferedReader.readLine();
                }
            }
        } catch (IOException e) {
            throw new ItemStreamException("Failed to initialize reader", e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        executionContext.putLong(CURRENT_LINE_COUNT, lineCount.get());
    }

    @Override
    public void close() throws ItemStreamException {
        lock.lock();
        try {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    throw new ItemStreamException("Failed to close reader", e);
                }
            }
            initialized = false;
        } finally {
            lock.unlock();
        }
    }

}
