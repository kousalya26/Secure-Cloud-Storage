package com.cahcet.FinalProject.service;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
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
public class Encrypt {
    public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(n);
        SecretKey key = keyGenerator.generateKey();
        return key;
    }

    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
    public static void encrypt(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }
    public static void decrypt(String algorithm, SecretKey key, IvParameterSpec iv,
                                   File inputFile, File outputFile) throws IOException, NoSuchPaddingException,
            NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException {

        Cipher cipher = Cipher.getInstance(algorithm);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        FileInputStream inputStream = new FileInputStream(inputFile);
        FileOutputStream outputStream = new FileOutputStream(outputFile);
        byte[] buffer = new byte[64];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                outputStream.write(output);
            }
        }
        byte[] outputBytes = cipher.doFinal();
        if (outputBytes != null) {
            outputStream.write(outputBytes);
        }
        inputStream.close();
        outputStream.close();
    }

    private static String rot47e(String message) {
        int key = 47;
        String encrypText = "";
        for (int i = 0; i < message.length(); i++) {
            int temp = (int)message.charAt(i) + key;
            if((int)message.charAt(i) == 32){
                encrypText += " ";
            } else if(temp > 126){
                temp -= 94;
                encrypText += (char)temp;
            } else {
                encrypText += (char)temp;
            }
        }
        return encrypText;
    }
    private static String rot47d(String message) {
        int key = 47;
        String decrypText = "";
        for (int i = 0; i < message.length(); i++) {
            int temp = (int)message.charAt(i) - key;
            if((int)message.charAt(i) == 32){
                decrypText += " ";
            } else if(temp < 32){
                temp += 94;
                decrypText += (char)temp;
            } else {
                decrypText += (char)temp;
            } // if-else
        } // for

        return decrypText;
    }
    public static String bytetoString(File file){
        String encodedDoc="";
        try {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //create FileInputStream which obtains input bytes from a file in a file system
            //FileInputStream is meant for reading streams of raw bytes such as image data. For reading streams of characters, consider using FileReader.

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];
            try {
                for (int readNum; (readNum = fis.read(buf)) != -1;) {
                    //Writes to this byte array output stream
                    bos.write(buf, 0, readNum);
                    // System.out.println("read " + readNum + " bytes,");
                }
            } catch (IOException ex) {
                System.err.println("error in reading");
            }

            byte[] bytes = bos.toByteArray();
            bos.close(); // should be inside a finally block
            //We have the bytes now convert to String

            // ENCODING
            encodedDoc= Base64.getEncoder().encodeToString(bytes);
        } catch (IOException e) {
            // handle IOException
            System.out.println(e.getMessage());
        }
        return encodedDoc;
    }
    public static void stringtoByte(String encodedDoc,String fileLocation) throws IOException{
        byte[] bytes = Base64.getDecoder().decode(encodedDoc);
        int size = bytes.length;
        InputStream isfilecontent = null;
        //byte[] b = new byte[size];
        isfilecontent = new ByteArrayInputStream(Base64.getDecoder().decode(encodedDoc));
        //change file loaction before running
        //todo change file location
        File file2 = new File(fileLocation);
        //writing the downloaded data into a PDF file
        FileOutputStream fileOutput = new FileOutputStream(file2);

        /* use binary I/O to prevent line based operation messing with the encoding.*/
        byte[] buf2 = new byte[2048];
        int b_read = 0;
        while ( (b_read = isfilecontent.read(buf2)) > 0) {
            fileOutput.write(buf2, 0, b_read);
        }
        fileOutput.flush();
        //closed the output stream
        fileOutput.close();
    }
    public static String convertSecretKeyToString(SecretKey secretKey) throws NoSuchAlgorithmException {
        byte[] rawData = secretKey.getEncoded();
        String encodedKey = Base64.getEncoder().encodeToString(rawData);
        return encodedKey;
    }

    public static SecretKey convertStringToSecretKey(String encodedKey) {
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
        return originalKey;
    }


}
