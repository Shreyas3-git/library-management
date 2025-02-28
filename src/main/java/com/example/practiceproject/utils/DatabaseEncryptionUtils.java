package com.example.practiceproject.utils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class DatabaseEncryptionUtils {

    private static final String ALGORITHM = "AES"; // Or "AES/CBC/PKCS5Padding" for stronger security
    private static SecretKey secretKey;
    private static boolean isKeyLoaded = false; // Flag to track key loading

    // 1. Key Generation (Call this ONCE to generate and securely store the key)
    public static String generateKey() throws NoSuchAlgorithmException {
        if (!isKeyLoaded) { // Only generate if key hasn't been loaded yet.
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(256, new SecureRandom()); // 256-bit AES
            secretKey = keyGenerator.generateKey();

            String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
            System.out.println("Generated and encoded key (Store this securely!): " + encodedKey);
            isKeyLoaded = true; // Set the flag after successful key generation.

            return encodedKey; // Return the encoded key
        } else {
            System.out.println("Key already generated or loaded.");
            return null; // Return null if the key is already generated
        }
    }

    // 2. Load Key (Call this during application startup)
    public static void loadKey(String encodedKey) {
        if (!isKeyLoaded && encodedKey != null && !encodedKey.isEmpty()) { // Only load if not already loaded and key is provided.
            try {
                byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
                secretKey = new SecretKeySpec(decodedKey, ALGORITHM);
                isKeyLoaded = true; // Set the flag after successful key loading.
                System.out.println("Key loaded successfully.");
            } catch (IllegalArgumentException e) {
                System.err.println("Invalid encoded key. Please check your key."); // Handle potential exceptions
                // Consider throwing an exception here to halt application startup if the key is essential.
            }
        } else if (isKeyLoaded) {
            System.out.println("Key already loaded.");
        } else {
            System.err.println("No encoded key provided for loading.");
        }
    }

    // 3. Encrypt Data to byte[]
    public static byte[] encrypt(String data) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        ensureKeyIsLoaded(); // Make sure the key is loaded
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }

    // 4. Decrypt Data from byte[]
    public static String decrypt(byte[] encryptedData) {
        try {
            ensureKeyIsLoaded(); // Make sure the key is loaded
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedBytes = cipher.doFinal(encryptedData);
            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (NoSuchPaddingException| NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            System.out.println("Error while decrypting data => "+e.getLocalizedMessage());
             return null;
        }
    }

    private static void ensureKeyIsLoaded() {
        if (!isKeyLoaded) {
            throw new IllegalStateException("Encryption key must be loaded before use. Call loadKey() during application startup.");
        }
    }



    public static void main(String[] args) throws Exception { // Example Usage
//         1. Generate Key (Only once, store the encoded key securely)

         // Uncomment to generate a key (do this only once)
        String encodedKey = DatabaseEncryptionUtils.generateKey(); // Replace with your stored encoded key

        // 2. Load Key (During application startup)
        DatabaseEncryptionUtils.loadKey(encodedKey);

        String dataToEncrypt = "This is my secret message!";
        byte[] encryptedBytes = DatabaseEncryptionUtils.encrypt(dataToEncrypt);
        System.out.println("Encrypted bytes (Base64): " + Base64.getEncoder().encodeToString(encryptedBytes));

        String decryptedData = DatabaseEncryptionUtils.decrypt(encryptedBytes);
        System.out.println("Decrypted data: " + decryptedData);

        System.out.println("Is data same : " + dataToEncrypt.equals(decryptedData));



    }
}