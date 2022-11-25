package com.libra.ptcgt.ptcgtool.api;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

public class InputOutputUtils {


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
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <a href="https://stackoverflow.com/questions/27599965/java-better-way-to-delete-file-if-exists">...</a>
     *
     * @param path
     */
    public static void deleteFile(String path) {
        if (path == null) return;
        File file = new File(path);
        if (file.isDirectory())
            for (File f : Objects.requireNonNull(file.listFiles()))
                deleteFile(f.toString());
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
            System.out.println("Is Direc");
            try {
                for (File f : Objects.requireNonNull(file.listFiles()))
                    acc += Files.size(f.toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return acc;
    }

}
