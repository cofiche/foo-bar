package io.wahman.kata.service;


public class TransformationService {

    public static String processNumber(Integer number) {
        StringBuilder result = new StringBuilder();

        if (number % 3 == 0) {
            result.append("FOO");
        }
        if (number % 5 == 0) {
            result.append("BAR");
        }

        for (char digit : number.toString().toCharArray()) {
            if (digit == '3') {
                result.append("FOO");
            } else if (digit == '5') {
                result.append("BAR");
            } else if (digit == '7') {
                result.append("QUIX");
            }
        }

        return !result.isEmpty() ? result.toString() : String.valueOf(number);
    }
}
