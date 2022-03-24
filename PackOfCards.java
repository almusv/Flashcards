package flashcards;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class PackOfCards {
    private final Scanner scanner = new Scanner(System.in);
    private String fileName;
    private final String path = "D:\\Programming\\TestJavaTasks\\Flashcards\\Flashcards\\task\\src\\flashcards\\";
    private File file;
    private int count = 0;
    private final List<String> log = new ArrayList<>();
    private final Map<String, String> cards = new LinkedHashMap<>();
    private final SortedMap<String, Integer> mistakes = new TreeMap<>();
    private boolean isFirstDisplayCheckForImport = true;

    void print(String message) {
        System.out.print(message);
        log.add(message);
    }

    String readInput() {
        String input = scanner.nextLine();
        log.add(input + "\n");
        return input;
    }

    void add() {
        print("The card:\n");
        String key = readInput();
        if (cards.containsKey(key)) {
            print("The card \"" + key + "\" already exists.\n\n");
            return;
        }
        print("The definition of the card:\n");
        String value = readInput();
        if (cards.containsValue(value)) {
            print("The definition \"" + value + "\" already exists.\n\n");
            return;
        }
        cards.put(key, value);
        print("The pair (\"" + key + "\":\"" + value + "\") has been added.\n\n");
    }

    void remove() {
        print("Which card?\n");
        String term = readInput();
        if (cards.containsKey(term)) {
            cards.remove(term);
            print("The card has been removed.\n\n");
        } else {
            print("Can't remove \"" + term + "\": there is no such card.\n\n");
        }
    }

    void importCards() {
        count = 0;
        print("File name:\n");
        fileName = readInput();
        file = new File(fileName);
        //file = new File(path + fileName);
        try (Scanner scannerFile = new Scanner(file)) {
            while (scannerFile.hasNextLine()) {
                String[] keyAndValue = scannerFile.nextLine().split(";");
                try {
                    cards.put(keyAndValue[0], keyAndValue[1]);
                    mistakes.put(keyAndValue[0], Integer.parseInt(keyAndValue[2]));
                } catch (NumberFormatException e) {
                    mistakes.clear();
                } finally {
                    count++;
                }
            }
        } catch (FileNotFoundException e) {
            print("File not found.\n\n");
            return;
        }
        print(count + " cards have been loaded.\n\n");
    }

    void checkForImport(String[] args) {
        String fileNameForImport = "";
        if (isFirstDisplayCheckForImport) {
            for (int i = 0; i < args.length; i++) {
                if ("-import".equals(args[i])) {
                    fileNameForImport = args[i + 1];
                    break;
                }
            }
            if (fileNameForImport.isEmpty()) {
                return;
            }
            count = 0;
            file = new File(fileNameForImport);
            //file = new File(path + fileName);
            try (Scanner scannerFile = new Scanner(file)) {
                while (scannerFile.hasNextLine()) {
                    String[] keyAndValue = scannerFile.nextLine().split(";");
                    try {
                        cards.put(keyAndValue[0], keyAndValue[1]);
                        mistakes.put(keyAndValue[0], Integer.parseInt(keyAndValue[2]));
                    } catch (NumberFormatException e) {
                        mistakes.clear();
                    } finally {
                        count++;
                    }
                }
            } catch (FileNotFoundException e) {
                return;
            }
            print(count + " cards have been loaded.\n\n");
        }
        isFirstDisplayCheckForImport = false;
    }

    void export() {
        count = 0;
        print("File name:\n");
        fileName = readInput();
        file = new File(fileName);
        //file = new File(path + fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (var entry : cards.entrySet()) {
                printWriter.printf("%s;%s;%d\n", entry.getKey(), entry.getValue(), mistakes.get(entry.getKey()));
                count++;
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
            return;
        }
        print(count + " cards have been saved.\n\n");
    }

    void checkForExport(String[] args) {
        String fileNameForExport = "";
        for (int i = 0; i < args.length; i++) {
            if ("-export".equals(args[i])) {
                fileNameForExport = args[i + 1];
                break;
            }
        }
        if (!fileNameForExport.isEmpty()) {
            count = 0;
            file = new File(fileNameForExport);
            //file = new File(path + fileNameForExport);
            try (PrintWriter printWriter = new PrintWriter(file)) {
                for (var entry : cards.entrySet()) {
                    printWriter.printf("%s;%s;%d\n", entry.getKey(), entry.getValue(), mistakes.get(entry.getKey()));
                    count++;
                }
            } catch (IOException e) {
                System.out.printf("An exception occurred %s", e.getMessage());
                return;
            }
            print(count + " cards have been saved.\n\n");
        }
    }

    void ask() {
        count = 0;
        print("How many times to ask?\n");
        int numberOfCards = Integer.parseInt(readInput());
        for (int i = 0; i < numberOfCards; i++) {
            for (String mapKey : cards.keySet()) {
                if (count < numberOfCards) {
                    print("Print the definition of \"" + mapKey + "\":\n");
                    String definitionFromUser = readInput();
                    if (cards.get(mapKey).equals(definitionFromUser)) {
                        print("Correct!\n");
                        count++;
                    } else if (cards.containsValue(definitionFromUser)) {
                        String specificKey = "";
                        for (var entry : cards.entrySet()) {
                            if (entry.getValue().equals(definitionFromUser)) {
                                specificKey = entry.getKey();
                                break;
                            }
                        }
                        print("Wrong. The right answer is \"" + cards.get(mapKey) + "\", but your definition is correct for \"" + specificKey + "\".\n");
                        count++;
                        mistakes.put(mapKey, mistakes.getOrDefault(mapKey, 0) + 1);
                    } else {
                        print("Wrong. The right answer is \"" + cards.get(mapKey) + "\".\n");
                        count++;
                        mistakes.put(mapKey, mistakes.getOrDefault(mapKey, 0) + 1);
                    }
                } else {
                    print("\n");
                    return;
                }
            }
        }
    }

    void saveLog() {
        print("File name:\n");
        fileName = readInput();
        file = new File(fileName);
        //file = new File(path + fileName);
        try (PrintWriter printWriter = new PrintWriter(file)) {
            for (String line : log) {
                printWriter.print(line);
            }
        } catch (IOException e) {
            System.out.printf("An exception occurred %s", e.getMessage());
            return;
        }
        print("The log has been saved.\n\n");
    }

    void showHardestCard() {
        int numberOfHardestCard = 0;
        StringBuilder hardestCard = new StringBuilder();
        SortedSet<Integer> numberOfMistakes = new TreeSet<>(mistakes.values()).descendingSet();
        if (numberOfMistakes.size() == 0) {
            print("There are no cards with errors.\n\n");
        } else {
            for (var entry : mistakes.entrySet()) {
                if (numberOfMistakes.first().equals(entry.getValue())) {
                    String nameOfCard = entry.getKey();
                    if (numberOfHardestCard >= 1) {
                        hardestCard.append(", ").append("\"").append(nameOfCard).append("\"");
                    } else {
                        hardestCard.append("\"").append(nameOfCard).append("\"");
                    }
                    numberOfHardestCard++;
                }
            }
            if (numberOfHardestCard == 1) {
                print("The hardest card is " + hardestCard + ". You have " + numberOfMistakes.first() + " errors answering it.\n\n");
            } else {
                print("The hardest cards are " + hardestCard + ". You have " + numberOfMistakes.first() + " errors answering them.\n\n");
            }
        }
    }

    void resetStats() {
        mistakes.clear();
        print("Card statistics have been reset.\n\n");
    }
}
