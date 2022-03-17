/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.Unchecked;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author F6036477
 */
public class Crypto {
  
  public static SecretKey createSecretKey(String key, KeyAlgorithm algo) {
    return createSecretKey(key.getBytes(StandardCharsets.UTF_8), algo);
  }
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo) {
    return new SecretKeySpec(key, algo.getAlgorithmName());
  }
  
  private static int positiveMin(int... is) {
    int min = Integer.MAX_VALUE;
    for(int i = 0; i < is.length; i++) {
      if(is[i] >= 0) {
        min = Math.min(min, is[i]);
      }
    }
    return min;
  }
  
  public static SecretKey createSecretKey(String key, KeyAlgorithm algo, int size) {
    return createSecretKey(key.getBytes(StandardCharsets.UTF_8), algo, size);
  }
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo, int size) {
    byte[] bkey = new byte[size];
    byte[] hash = Hash.sha512().put(key).getBytes();
    int x = Math.max(1, Math.round(size / Integer.valueOf(hash.length).floatValue()));
    int f = 0;
    while(f < x) {
      for(int i = 0; i < f; i++) {
        hash = Hash.sha512().put(hash).getBytes();
      }
      System.arraycopy(hash, 0, bkey, f * hash.length, positiveMin(hash.length, bkey.length, bkey.length - f * hash.length));
      f++;
    }
    return new SecretKeySpec(bkey, algo.getAlgorithmName());
  }
  
  public static SecretKey createSecretKey(int keySize, KeyAlgorithm algo) {
    KeyGenerator gen = Unchecked.call(()->KeyGenerator.getInstance(algo.getAlgorithmName()));
    gen.init(keySize);
    return gen.generateKey();
  }
  
  public static KeyPair createKeyPair(int keySize, CryptoAlgorithm algo) {
    KeyPairGenerator gen = Unchecked.call(()->KeyPairGenerator.getInstance(algo.getAlgorithmName()));
    gen.initialize(keySize);
    return gen.generateKeyPair();
  }
  
  public static PrivateKey createPrivateKey(byte[] bob) throws IOException {
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bob);
      PrivateKey pk = kf.generatePrivate(spec);
      return pk;
    }
    catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  public static PublicKey createPublicKey(byte[] bob) throws IOException {
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec spec = new X509EncodedKeySpec(bob);
      PublicKey pub = kf.generatePublic(spec);
      return pub;
    }
    catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw new IOException(e.toString(), e);
    }
  }
  
  public static PrivateKey loadPrivateKey(Path pkDer) throws IOException {
    return createPrivateKey(Files.readAllBytes(pkDer));
  }
  
  public static PublicKey loadPublicKey(Path pubDer) throws IOException {
    return createPublicKey(Files.readAllBytes(pubDer));
  }
  
  public static KeyPair loadKeyPair(Path pkDer, Path pubDer) throws IOException {
    return new KeyPair(loadPublicKey(pubDer), loadPrivateKey(pkDer));
  }
  
  public static Cipher encryptCipher(Key key, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.ENCRYPT_MODE, key));
    return cipher;
  }
  
  public static Cipher decryptCipher(Key key, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.DECRYPT_MODE, key));
    return cipher;
  }
  
  public static CipherInputStream encryptInputStream(InputStream in, Key key, CryptoAlgorithm algo) {
    return new CipherInputStream(in, encryptCipher(key, algo));
  }
  
  public static CipherInputStream decryptInputStream(InputStream in, Key key, CryptoAlgorithm algo) {
    return new CipherInputStream(in, decryptCipher(key, algo));
  }
  
  public static CipherOutputStream encryptOutputStream(OutputStream out, Key key, CryptoAlgorithm algo) {
    return new CipherOutputStream(out, encryptCipher(key, algo));
  }
  
  public static CipherOutputStream decryptOutputStream(OutputStream out, Key key, CryptoAlgorithm algo) {
    return new CipherOutputStream(out, decryptCipher(key, algo));
  }
  
  public static ByteBuffer encrypt(SecretKey key, CryptoAlgorithm algo, ByteBuffer content) {
    Cipher c = encryptCipher(key, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer decrypt(SecretKey key, CryptoAlgorithm algo, ByteBuffer content) {
    Cipher c = decryptCipher(key, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
}
