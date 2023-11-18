package com.cahcet.FinalProject.service;

import java.io.*;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
public class EncryptEncodeSplit {
    public static List<String> splitFile(File file, int numParts) throws IOException {
        // Calculate the size of each part
        long fileSize = file.length();
        long partSize = fileSize / numParts;

        // Create a list to store the names of the part files
        List<String> partFiles = new ArrayList<>();

        // Create a buffered input stream to read from the file
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            // Read the file and split it into parts
            for (int i = 0; i < numParts; i++) {
                // Create a byte array to store the part data
                byte[] partData = new byte[(int) partSize];

                // Read the part data from the input stream
                int bytesRead = in.read(partData);

                // Create a file to store the part data
                File partFile = new File(file.getParentFile(), file.getName() + ".part" + i);
                partFiles.add(partFile.getAbsolutePath());

                // Write the part data to the file
                try (FileOutputStream out = new FileOutputStream(partFile)) {
                    out.write(partData, 0, bytesRead);
                }
            }
        }

        // Return the list of part file names in the order they should be merged
        return partFiles;
    }
    public static void mergeFiles(List<String> partFiles, File outputFile) throws IOException {
        // Create a buffered output stream to write to the output file
        try (BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(outputFile))) {
            // Read the data from each part file and write it to the output file
            for (String partFile : partFiles) {
                // Create a buffered input stream to read from the part file
                try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(partFile))) {
                    // Read the data from the part file and write it to the output file
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }
            }
        }
    }

}
