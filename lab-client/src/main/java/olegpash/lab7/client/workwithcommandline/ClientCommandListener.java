package olegpash.lab7.client.workwithcommandline;

import olegpash.lab7.client.ClientConfig;
import olegpash.lab7.client.util.CommandToSend;
import olegpash.lab7.common.util.TextColoring;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class ClientCommandListener {

    private final Scanner sc;
    private final String username;


    public ClientCommandListener(InputStream inputStream, String username) {
        sc = new Scanner(inputStream);
        this.username = username;
    }

    public CommandToSend readCommand() {
        try {
            ClientConfig.getConsoleTextPrinter().printText(TextColoring.getBlueText(username + ": "));
            String[] splitedInput = sc.nextLine().trim().split(" ");
            String commandName = splitedInput[0].toLowerCase(Locale.ROOT);
            String[] commandsArgs = Arrays.copyOfRange(splitedInput, 1, splitedInput.length);
            return new CommandToSend(commandName, commandsArgs);
        } catch (NoSuchElementException e) {
            ClientConfig.getConsoleTextPrinter().printlnText(TextColoring.getRedText("An invalid character has been entered, forced shutdown!"));
            System.exit(1);
            return null;
        }
    }

}
