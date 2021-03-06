package olegpash.lab7.common.util;

import olegpash.lab7.common.abstractions.AbstractTextPrinter;

public class ConsoleTextPrinter extends AbstractTextPrinter {

    public void printText(String text) {
        System.out.print(text);
    }

    public void printlnText(String text) {
        System.out.println(text);
    }
}
