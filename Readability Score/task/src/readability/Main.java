package readability;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static String readFileAsString(String fileName) throws IOException {
        return new String(Files.readAllBytes(Paths.get(fileName)));
    }

    public static void main(String[] args) throws IOException {
        System.out.println("java Main " + args[0] + "\nThe text is:");
        String text = readFileAsString(args[0]);
        output(text);
    }

    public static void output(String text) {
        System.out.println("Words: " + words(text) + "\n" +
                "Sentences: " + sentences(text) + "\n" +
                "Characters: " + chars(text) + "\n" +
                "Syllables: " + syllables(text) + "\n" +
                "Polysyllables: " + polysyllables(text) + "\n");
        System.out.print("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner = new Scanner(System.in);
        System.out.println(" ");
        String choice = scanner.nextLine();
        double ari = Math.ceil((ari(text)) + 5.0);
        double fk = Math.ceil((fk(text)) + 5.0);
        double smog = Math.ceil((smog(text)) + 5.0);
        double cl = Math.ceil((cl(text)) + 6.0);
        String avg = String.format("%.2f", ((ari + fk + smog + cl) / 4.0));
        switch (choice) {
            case "ARI":
                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds)\n", ari(text), (int) ari);
                break;
            case "FK":
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds)\n", fk(text), (int) fk);
                break;
            case "SMOG":
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds)\n", smog(text), (int) smog);
                break;
            case "CL":
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds)\n", cl(text), (int) cl);
                break;
            case "all":
                System.out.printf("Automated Readability Index: %.2f (about %d-year-olds)\n", ari(text), (int) ari);
                System.out.printf("Flesch–Kincaid readability tests: %.2f (about %d-year-olds)\n", fk(text), (int) fk);
                System.out.printf("Simple Measure of Gobbledygook: %.2f (about %d-year-olds)\n", smog(text), (int) smog);
                System.out.printf("Coleman–Liau index: %.2f (about %d-year-olds)\n", cl(text), (int) cl);
                break;
        }
        System.out.printf("\nThis text should be understood in average by %s-year-olds.\n", avg);
    }

    public static int chars(String text) {
        return text.replaceAll(" ", "").split("").length;
    }

    public static int sentences(String text) {
        String[] inputSentences = text.split("[.?!]");
        return inputSentences.length;
    }

    public static String[] sentencesArray(String text) {
        return text.split("[.?!]");
    }

    public static String[] wordsArray(String text) {
        return text.split(" ");
    }

    public static int words(String text) {
        String[] wordsArray = sentencesArray(text);
        int words = 0;
        for (String inputSentence : wordsArray) {
            String[] currentSentence = inputSentence.split(" ");
            words += currentSentence.length;
            if (currentSentence[0].equals("")) {
                words--;
            }
        }
        return words;
    }

    public static int syllables(String text) {
        String[] wordsArray = wordsArray(text);
        int countSyllables = 0;
        for (String s : wordsArray) {
            countSyllables += Math.max(1, s.toLowerCase()
                    .replaceAll("[.?!]", "")
                    .replaceAll("e$", "")
                    .replaceAll("[aeiouy]{2,}", "a")
                    .replaceAll("[^aeiouy]", "")
                    .length());
        }
        return countSyllables;
    }

    public static int polysyllables(String text) {
        String[] wordsArray = wordsArray(text);
        int countPolysyllables = 0;
        for (String s : wordsArray) {
            if (syllables(s) > 2)
                countPolysyllables++;
        }
        return countPolysyllables;
    }

    public static double ari(String text) {
        return 4.71 * ((double) chars(text) / words(text)) + 0.5 * ((double) words(text) / sentences(text)) - 21.43;
    }

    public static double fk(String text) {
        return 0.39 * ((double) words(text) / (double) sentences(text)) + 11.8 * ((double) syllables(text) / (double) words(text)) - 15.59;
    }

    public static double smog(String text) {
        return 1.043 * Math.sqrt(((double) polysyllables(text)) * (30 / (double) sentences(text))) + 3.1291;
    }

    public static double cl(String text) {
        return 0.0588 * ((double) chars(text) / words(text) * 100) - 0.296 * ((double) sentences(text) / words(text) * 100) - 15.8;
    }
}