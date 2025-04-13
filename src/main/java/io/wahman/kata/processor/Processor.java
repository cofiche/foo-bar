package io.wahman.kata.processor;

import io.wahman.kata.service.TransformationService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class Processor implements ItemProcessor<Integer, String> {

    @Override
    public String process(Integer number) throws Exception {
        return number + " \"" + TransformationService.processNumber(number) + "\"";
    }
}
