package com.libra.ptcgt.ptcgtool.api;

import com.libra.ptcgt.ptcgtool.objects.Card;
import com.libra.ptcgt.ptcgtool.objects.Deck;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class InputOutputUtils {

    public final static String CACHED_FILES_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\cache\\images";
    public final static String DECKS_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\decks\\";

    /**
     * <a href="https://stackoverflow.com/questions/10292792/getting-image-from-url-java">Stack Overflow Solution</a>
     * This function fetches an image from a URL and downloads it to the desired destination file
     *
     * @param imageUrl        the URL we want to download the image from
     * @param destinationPath the absolute Path on the disk, we can get the Project's path using System.getProperty("user.dir")
     */
    public static void saveImage(String imageUrl, String destinationPath, String destinationFileName) {
        String destinationFile = destinationPath + destinationFileName;
        try {
            URL url = new URL(imageUrl);
            InputStream is = url.openStream();
            OutputStream os = new FileOutputStream(destinationFile);

            byte[] b = new byte[(int) Math.pow(2, 9)];
            int length;
            while ((length = is.read(b)) != -1)
                os.write(b, 0, length);

            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            try {
                Files.createDirectories(Path.of(destinationPath));
                saveImage(imageUrl, destinationPath, destinationFileName);
            } catch (IOException ex) {
                System.out.println("Image not Found");
            }
        } catch (IOException e) {
            System.out.println("Image not Found");
        }
    }

    /**
     * <a href="https://stackoverflow.com/questions/27599965/java-better-way-to-delete-file-if-exists">...</a>
     *
     * @param path
     */
    public static void deleteFromDisk(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.isDirectory())
            for (File f : Objects.requireNonNull(file.listFiles()))
                deleteFromDisk(f.toString());
        try {
            Files.deleteIfExists(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static long directorySize(String path) {
        File file = new File(path);
        long acc = 0;
        if (file.isDirectory()) {
            try {
                for (File f : Objects.requireNonNull(file.listFiles()))
                    acc += Files.size(f.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return acc;
    }

    public static List<String> readDeckFolder() {
        List<String> deckNames = new ArrayList<>();
        File folder = new File(DECKS_LOCATION);
        if (folder.isDirectory())
            for (File f : Objects.requireNonNull(folder.listFiles()))
                deckNames.add(removeSuffix(f.getName(), ".deck"));
        return deckNames;
    }

    /**
     * <a href="https://www.techiedelight.com/how-to-remove-a-suffix-from-a-string-in-java/">...</a>
     */
    private static String removeSuffix(final String s, final String suffix) {
        if (s != null && suffix != null && s.endsWith(suffix))
            return s.substring(0, s.length() - suffix.length());
        return s;
    }

    public static void writeNewDeck(String name, Deck deck) {
        String path = DECKS_LOCATION + name + ".deck";
        try {
            File deckFile = new File(path);
            System.out.println(
                    deckFile.createNewFile() ? "File created: " + deckFile.getName() : "File already exists."
            );
            FileWriter writer = new FileWriter(path);
            writer.write(deck.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static Deck readDeckFromDisk(String name) {
        String path = DECKS_LOCATION + name + ".deck";
        File deckFile = new File(path);
        List<Card> cards = new ArrayList<>();
        try {
            Scanner reader = new Scanner(deckFile);
            List<String> lines = new ArrayList<>();
            while (reader.hasNextLine())
                lines.add(reader.nextLine());
            reader.close();

            lines.parallelStream().filter(line -> line.contains("\t- ")).forEach(
                    line -> cards.addAll(parseCard(line)));

        } catch (FileNotFoundException e) {
            System.out.println("File " + path + " was not found.");
        }
        return new Deck(cards);
    }

    private static List<Card> parseCard(String line) {
        List<Card> cards = new ArrayList<>();
        String[] splitLine = line.split(" ");

        int count = Integer.parseInt(splitLine[1]);
        String cardId = splitLine[splitLine.length - 1];
        Card c = new Card(PTCGAPI.getCardData(cardId));
        for (int i = 0; i < count; ++i)
            cards.add(c);
        return cards;
    }

}
