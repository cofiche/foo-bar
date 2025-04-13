package io.wahman.kata.controller;

import io.wahman.kata.processor.Processor;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@AutoConfigureMockMvc
@SpringBootTest
class ControllerTest {

    @Mock
    private Processor processor;

    @InjectMocks
    private Controller controller;

    private MockMvc mockMvc;


    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        when(processor.process(3)).thenReturn("FOOFOO");
    }


    @Test
    void testConvertNumber() throws Exception {
        mockMvc.perform(get("/api/transform/3")
                        .contentType(MediaType.TEXT_PLAIN_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().string("FOOFOO"));
    }
}
