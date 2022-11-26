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

/**
 * A utility class containing functions used to read and write from the disk, and help to implement caching of files
 */
public final class IOTools {

    public final static String CACHED_FILES_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\cache\\images";
    public final static String DECKS_LOCATION = System.getProperty("user.dir") + "\\src\\main\\resources\\decks\\";

    private final static String DECK_FILE_EXTENSION = ".deck";

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
     * <a href="https://stackoverflow.com/questions/27599965/java-better-way-to-delete-file-if-exists">StackOverflow</a>
     *
     * @param path of file or folder we want to delete
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

    /**
     * Computes the size of the given directory in bytes
     *
     * @param path of directory that we want to compute the size of
     * @return size in bytes
     */
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

    /**
     * Looks up the names of the decks saved on the current disk
     *
     * @return a List of names of the decks, without the .deck file extension
     */
    public static List<String> readDeckFolder() {
        List<String> deckNames = new ArrayList<>();
        File folder = new File(DECKS_LOCATION);
        if (folder.isDirectory())
            for (File f : Objects.requireNonNull(folder.listFiles()))
                deckNames.add(removeExtension(f.getName()).replace("_", " "));
        return deckNames;
    }

    /**
     * <a href="https://www.techiedelight.com/how-to-remove-a-suffix-from-a-string-in-java/">Remove the suffix of a string</a>
     */
    private static String removeExtension(String s) {
        return s != null && s.endsWith(DECK_FILE_EXTENSION) ?
                s.substring(0, s.length() - DECK_FILE_EXTENSION.length()) : s;
    }

    /**
     * Used to write a Deck to the disk, generating a file with the .deck extension that we can read later
     *
     * @param name the name of the Deck and therefore the file. i.e. for "Lugia Vstar",
     *             the file will be called "Lugia_Vstar.deck"
     * @param deck the deck containing the cards we want to save
     */
    public static void saveDeckToDisk(String name, Deck deck) {
        String path = DECKS_LOCATION + name.replace(" ", "_") + DECK_FILE_EXTENSION;
        File deckFile = new File(path);
        try {
            System.out.println(
                    deckFile.createNewFile() ?
                            "File created: " + deckFile.getName() : "File already exists. Overwriting..."
            );
            // write the deck to disk
            FileWriter writer = new FileWriter(path);
            writer.write(deck.toString());
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not create a new Deck.");
        }
    }

    /**
     * Used to read a Deck from the disk
     *
     * @param name the name of the deck we want to read
     * @return the Deck we read
     */
    public static Deck readDeckFromDisk(String name) {
        File deckFile = new File(DECKS_LOCATION + name + DECK_FILE_EXTENSION);
        List<Card> cards = new ArrayList<>();
        List<String> lines = new ArrayList<>();

        try {
            Scanner reader = new Scanner(deckFile);
            while (reader.hasNextLine())
                lines.add(reader.nextLine());
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
        }

        lines.parallelStream()
                .filter(line -> line.contains("\t- "))
                .forEach(line -> cards.addAll(parseDeckLine(line)));

        return new Deck(cards);
    }

    /**
     * @param deckLine a line of the deck that contains Cards to parse
     * @return a List of the cards that were parse, in case count is > 1
     */
    private static List<Card> parseDeckLine(String deckLine) {
        List<Card> cards = new ArrayList<>();
        String[] splitLine = deckLine.split(" ");

        // The count of the card is the first piece of data after the card prefix
        int count = Integer.parseInt(splitLine[1]);
        // the card ID is the last piece of data after the card name
        String cardId = splitLine[splitLine.length - 1];

        Card c = new Card(PTCGAPI.getCardData(cardId));
        for (int i = 0; i < count; ++i) cards.add(c);
        return cards;
    }

}
