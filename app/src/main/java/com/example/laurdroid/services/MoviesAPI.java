package com.example.laurdroid.services;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

public class MoviesAPI {
    private final KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
    private static byte[] encryptedApiKey;

    public MoviesAPI() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException, NoSuchProviderException, InvalidAlgorithmParameterException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        keyStore.load(null);

        KeyStore.Entry entry = keyStore.getEntry("MovieAPIkey", null);
        if (entry == null) {
            byte[] apiKey = "49d8a5ab8851e0743364d9db494cac36".getBytes(StandardCharsets.UTF_8);
            KeyGenParameterSpec keySpec = new KeyGenParameterSpec.Builder("MovieAPIkey", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .setKeySize(256)
                    .build();
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
            keyGenerator.init(keySpec);
            SecretKey secretKey = keyGenerator.generateKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedApiKey = cipher.doFinal(apiKey);
            keyStore.setEntry("MovieAPIkey", new KeyStore.SecretKeyEntry(secretKey), null);
        } else if (!(entry instanceof KeyStore.SecretKeyEntry)) {
            // Handle error
        }

    }

    public String getApiKey() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException, UnrecoverableEntryException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        keyStore.load(null);
        KeyStore.Entry entry = keyStore.getEntry("MovieAPIkey", null);
        if (entry instanceof KeyStore.SecretKeyEntry) {
            SecretKey secretKey = ((KeyStore.SecretKeyEntry) entry).getSecretKey();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] decryptedApiKey = cipher.doFinal(encryptedApiKey);
            return new String(decryptedApiKey, StandardCharsets.UTF_8);

        } else {
            return "FAILED";
        }
    }



}
