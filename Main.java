package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PackOfCards flashcards = new PackOfCards();

        while (true) {
            flashcards.checkForImport(args);
            flashcards.print("Input the action (add, remove, import, export, ask, exit, log, hardest card, reset stats):\n");
            String action = flashcards.readInput();
            switch (action) {
                case "add":
                    flashcards.add();
                    break;
                case "remove":
                    flashcards.remove();
                    break;
                case "import":
                    flashcards.importCards();
                    break;
                case "export":
                    flashcards.export();
                    break;
                case "ask":
                    flashcards.ask();
                    break;
                case "log":
                    flashcards.saveLog();
                    break;
                case "hardest card":
                    flashcards.showHardestCard();
                    break;
                case "reset stats":
                    flashcards.resetStats();
                    break;
                case "exit":
                    flashcards.print("Bye bye!\n");
                    flashcards.checkForExport(args);
                    return;
            }
        }
    }
}

