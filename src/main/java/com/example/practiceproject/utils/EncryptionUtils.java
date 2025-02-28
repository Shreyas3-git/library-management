package com.example.practiceproject.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class EncryptionUtils {

    private static final String AES_ALGORITHM = "AES"; // AES-256
    private static final String RSA_ALGORITHM = "RSA";
    private static final int AES_KEY_SIZE = 256; // Use 256-bit AES
    private static final int RSA_KEY_SIZE = 2048; // RSA key size (2048 or higher recommended)


    private SecretKey generateAesKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(AES_ALGORITHM);
        keyGenerator.init(AES_KEY_SIZE, new SecureRandom());
        return keyGenerator.generateKey();
    }

    public String encryptWithAes(String data, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptWithAes(String encryptedData, SecretKey aesKey) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }



    public KeyPair generateRsaKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(RSA_ALGORITHM);
        keyPairGenerator.initialize(RSA_KEY_SIZE, new SecureRandom());
        return keyPairGenerator.generateKeyPair();
    }

    public String encryptWithRsa(String data, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public String decryptWithRsa(String encryptedData, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    public static void main(String[] args) throws Exception {
        EncryptionUtils encryptionUtil = new EncryptionUtils();

        // 1. Generate AES key (Do this for each request/response)
        SecretKey aesKey = encryptionUtil.generateAesKey();
        String encodedAesKey = Base64.getEncoder().encodeToString(aesKey.getEncoded()); // Store securely

        // 2. Generate RSA key pair (Do this ONCE and store the private key securely)
        KeyPair rsaKeyPair = encryptionUtil.generateRsaKeyPair();
        PublicKey publicKey = rsaKeyPair.getPublic();
        PrivateKey privateKey = rsaKeyPair.getPrivate();

        // 3. Encrypt data with AES
        String dataToEncrypt = "This is my secret message!";
        String encryptedData = encryptionUtil.encryptWithAes(dataToEncrypt, aesKey);
        System.out.println("Encrypted data (AES): " + encryptedData);


        // 4. Encrypt AES key with RSA public key
        String encryptedAesKey = encryptionUtil.encryptWithRsa(encodedAesKey, publicKey);
        System.out.println("Encrypted AES key (RSA): " + encryptedAesKey);

        // --- Simulate sending encrypted data and key ---

        // 5. Decrypt AES key with RSA private key
        String decryptedAesKey = encryptionUtil.decryptWithRsa(encryptedAesKey, privateKey);
        SecretKey retrievedAesKey = new SecretKeySpec(Base64.getDecoder().decode(decryptedAesKey), AES_ALGORITHM);


        // 6. Decrypt data with decrypted AES key
        String decryptedData = encryptionUtil.decryptWithAes(encryptedData, retrievedAesKey);
        System.out.println("Decrypted data: " + decryptedData);

        System.out.println("Is data same : " + dataToEncrypt.equals(decryptedData));


    }
}
