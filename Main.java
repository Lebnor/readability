package readability;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static double words = 0;
    private static double sentences = 1;
    private static double characters = 0;
    private static double syllables = 0;
    private static double polysyllables = 0;

    public static void main(String[] args) {

        // get file name from command line else use default
        String fileName = args.length > 0 ? args[0] : "file.txt";

        // open file
        File file = new File(fileName);

        // parse text data
        parseFile(file);

        // print data to console
        printData();

        // ask user for score calculation method
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all):");
        Scanner scanner = new Scanner(System.in);
        String result = scanner.nextLine();
        scanner.close();

        // print score results to user
        printScore(result);


    } // end main method

    private static void printScore(String result) {
        switch (result) {
            case "all":
                double sum = 0;

                sum += printScore("Automated Readability Index", ariScore());
                sum += printScore("Flesch–Kincaid readability tests", fkScore());
                sum += printScore("Simple Measure of Gobbledygook", smogScore());
                sum += printScore("Coleman–Liau index", clScore());

                System.out.printf("This text should be understood in average by %.2f year olds.", sum / 4);

                break;
            case "ARI":
                printScore("Automated Readability Index", ariScore());
                break;
            case "FK":
                printScore("Flesch–Kincaid readability tests", fkScore());
                break;
            case "SMOG":
                printScore("Simple Measure of Gobbledygook", smogScore());
                break;
            case "CL":
                printScore("Coleman–Liau index", clScore());
                break;
        }
    }

    private static double printScore(String algo, double score) {
        System.out.printf("%s: %.2f (about %d year olds).\n", algo, score, getAge(score));
        return score;
    }

    private static double smogScore() {
        return 1.043 * Math.sqrt(polysyllables * (30.0 / sentences)) + 3.1291;
    }
    private static double clScore() {
        double L = (100 * characters) / words;
        double S = (100 * sentences) / words;
        return 0.0588 * L - 0.296 * S - 15.8;
    }
    private static double fkScore() {
        return 0.39 * (words / sentences) + 11.8 * (syllables / words) - 15.59;
    }
    private static double ariScore() {
        return (4.71 * (characters / words)) + (0.5 * (words / sentences)) - 21.43;
    }

    private static void printData() {
        System.out.printf("Words: %s\n", words);
        System.out.printf("Sentences: %s\n", sentences);
        System.out.printf("Characters: %s\n", characters);
        System.out.printf("Syllables: %s\n", syllables);
        System.out.printf("Polysyllables: %s\n", polysyllables);
    }

    private static void parseFile(File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String token = scanner.next();
                words++;

                int wordSyllables = countSyllables(token);
                syllables += wordSyllables;

                if (wordSyllables > 2) {
                    polysyllables++;
                }
                if (token.endsWith(".") ||
                    token.endsWith("?") ||
                    token.endsWith("!")) {
                    sentences++;
                }

            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // returns syllables count of a word
    private static int countSyllables(String token) {
        int wordSyllables = 0;

        for (int i = 0; i < token.length(); i++) {
            char current = token.charAt(i);
            characters++;
            if (isVowel(current)) {
                wordSyllables++;
                if (i + 1 < token.length() && isVowel(token.charAt(i + 1))) {
                    wordSyllables--;
                }
            }

        }
        if (token.endsWith("e") || token.endsWith("E")) {
            wordSyllables--;
        }

        return wordSyllables == 0 ? 1 : wordSyllables;
    }

    private static boolean isVowel(char current) {
        return current == 'e' || current == 'a' || current == 'i' || current == 'u' || current == 'o' || current == 'y'
                || current == 'E' || current == 'A' || current == 'I' || current == 'U' || current == 'O' || current == 'Y';
    }

    private static int getAge(double score) {
        if (score > 1) {
            return 6;
        }  if (score > 2) {
            return 7;
        }  if (score > 3) {
            return  9;
        }  if (score > 4) {
            return   10;
        }  if (score > 5) {
            return     12;
        }  if (score > 6) {
            return   14;
        }  if (score > 7) {
            return    13;
        }  if (score > 8) {
            return    15;
        }  if (score > 9) {
            return    16;
        }  if (score > 10) {
            return   15;
        }  if (score > 11) {
            return   18;
        }  if (score > 12) {
            return   24;
        }  if (score > 13) {
            return   24;
        }
        return 0;
    }

}
