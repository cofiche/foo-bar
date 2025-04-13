package io.wahman.kata.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProcessorTest {

    private final Processor processor = new Processor();

    @Test
    void testProcessor() throws Exception {
        assertEquals("1 \"1\"", processor.process(1));
        assertEquals("3 \"FOOFOO\"", processor.process(3));
        assertEquals("5 \"BARBAR\"", processor.process(5));
        assertEquals("7 \"QUIX\"", processor.process(7));
        assertEquals("9 \"FOO\"", processor.process(9));
        assertEquals("51 \"FOOBAR\"", processor.process(51));
        assertEquals("53 \"BARFOO\"", processor.process(53));
        assertEquals("33 \"FOOFOOFOO\"", processor.process(33));
        assertEquals("15 \"FOOBARBAR\"", processor.process(15));
    }

}
