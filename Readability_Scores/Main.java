package readability;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.text.DecimalFormat;

public class Main {
    static int sumWords = 0, sumChars = 0, sumVowels = 0, sumSyllables = 0, sumSentences = 0;
    static int roundedScore, sumPolysyllables = 0;
    static double total = 0;
    static String[] sentences = new String[0];

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner scanner = new Scanner(file);
        Scanner userInput = new Scanner(System.in);
        String text = "";

        while (scanner.hasNext()) {
            text = scanner.nextLine().trim();
            if (text.charAt(text.length()-1)=='.') {
                sumChars++;
            }
            sentences = text.split("[.!?]");
            for (int i = 0; i < sentences.length; i++) {
                String[] words = sentences[i].trim().split(" ");
                for (String s : words) {
                    sumVowels=0;
                    sumWords++;
                    sumChars += s.length();
                    for (int a = 0; a<s.length(); a++) {
                        if (vowels(s.charAt(a))) {
                            if (a!=0 && !vowels(s.charAt(a-1))) {
                                if (!(a==s.length()-1 && s.charAt(s.length()-1)=='e')){
                                    sumVowels++;
                                }
                            }
                            if (a==0) {
                                sumVowels++;
                            }
                        }
                    }
                    if (sumVowels==0) {
                        sumSyllables++;
                    } else {
                        sumSyllables+=sumVowels;
                    }
                    if (sumVowels>2) {
                        sumPolysyllables++;
                    }
                }
            }
        }

        sumChars+=sentences.length-1;
        scanner.close();
        sumSentences = sentences.length;
        System.out.println("Words: " + sumWords);
        System.out.println("Sentences: " + sumSentences);
        System.out.println("Characters: " + sumChars);
        System.out.println("Syllables: " + sumSyllables);
        System.out.println("Polysyllables: " + sumPolysyllables);
        System.out.println("Enter the score you want to calculate (ARI, FK, SMOG, CL, all): ");
        String command = userInput.nextLine();
        if (command.equals("ARI")) {
            System.out.println("Automated Readability Index: " + age(ARI()));
        } else if (command.equals("FK")) {
            System.out.println("Flesch–Kincaid readability tests: " + age(fleschKincaid()));
        } else if (command.equals("SMOG")) {
            System.out.println("Simple Measure of Gobbledygook: " + age(smog()));
        } else if (command.equals("CL")) {
            System.out.println("Coleman–Liau index: " + age(colemanLiau()));
        } else {
            System.out.println("Automated Readability Index: " + age(ARI()));
            System.out.println("Flesch–Kincaid readability tests: " + age(fleschKincaid()));
            System.out.println("Simple Measure of Gobbledygook: " + age(smog()));
            System.out.println("Coleman–Liau index: " + age(colemanLiau()));
            System.out.println("The text should be understood in average by " + total/4 + " year olds.");
        }
    }

    public static boolean vowels(char a) {
        return (a=='a' || a=='e' || a=='i' || a=='o' || a=='u' || a=='y');
    }

    public static String age(double score) {
        roundedScore = (int) Math.ceil(score);
        double formattedScore = rounding(score);
        switch (roundedScore) {
            case 1:
                total+=6;
                return(formattedScore + " (about 6 year olds).");
            case 2:
                total+=7;
                return(formattedScore + " (about 7 year olds).");
            case 3:
                total+=9;
                return(formattedScore + " (about 9 year olds).");
            case 4:
                total+=10;
                return(formattedScore + " (about 10 year olds).");
            case 5:
                total+=11;
                return(formattedScore + " (about 11 year olds).");
            case 6:
                total+=12;
                return(formattedScore + " (about 12 year olds).");
            case 7:
                total+=13;
                return(formattedScore + " (about 13 year olds).");
            case 8:
                total+=14;
                return(formattedScore + " (about 14 year olds).");
            case 9:
                total+=15;
                return(formattedScore + " (about 15 year olds).");
            case 10:
                total+=16;
                return(formattedScore + " (about 16 year olds).");
            case 11:
                total+=17;
                return(formattedScore + " (about 17 year olds).");
            case 12:
                total+=18;
                return(formattedScore + " (about 18 year olds).");
            case 13:
                total+=24;
                return(formattedScore + " (about 24 year olds).");
            case 14:
                total+=24;
                return(formattedScore + " (about 24+ year olds).");
        }
        return (formattedScore + " (about 24+ year olds).");
    }

    public static double ARI(){
        return((4.71*((double)sumChars/sumWords))+(0.5*((double)sumWords/sumSentences))-21.43);
    }

    public static double fleschKincaid() {
        return ((0.39*((double)sumWords/sumSentences))+(11.8*((double)sumSyllables/sumWords))-15.59);
    }

    public static double smog() {
        return (1.043*Math.sqrt(sumPolysyllables*((double)30/sumSentences))+3.1291);
    }

    public static double colemanLiau() {
        double L = ((double)sumChars/sumWords)*100;
        double S = ((double)sumSentences/sumWords)*100;
        return (0.0588*L-(0.296*S)-15.8);
    }

    public static double rounding(double number){
        DecimalFormat df2 = new DecimalFormat("#.##");
        return Double.valueOf(df2.format(number));
    }
}

