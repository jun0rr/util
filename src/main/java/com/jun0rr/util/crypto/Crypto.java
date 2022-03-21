/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.Unchecked;
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
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author F6036477
 */
public class Crypto {
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo) {
    return new SecretKeySpec(key, algo.getAlgorithmName());
  }
  
  public static SecretKey createSecretKey(String key, KeyAlgorithm algo, int size) {
    return createSecretKey(key.getBytes(StandardCharsets.UTF_8), algo, size);
  }
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo, int size) {
    return createSecretKey(key, algo, 256, size);
  }
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo, int iteration, int size) {
    int bytes = size / 8;
    byte[] bkey = new byte[bytes];
    byte[] hash = Hash.sha512().put(key).getBytes();
    int x = Math.max(1, Math.round(bytes / Integer.valueOf(hash.length).floatValue()));
    int f = 0;
    while(f < x) {
      for(int i = 0; i < iteration; i++) {
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
  
  public static KeyPair createKeyPair(int keySize, KeyAlgorithm algo) {
    KeyPairGenerator gen = Unchecked.call(()->KeyPairGenerator.getInstance(algo.getAlgorithmName()));
    gen.initialize(keySize);
    return gen.generateKeyPair();
  }
  
  public static PrivateKey createPrivateKey(byte[] bob) {
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bob);
      PrivateKey pk = kf.generatePrivate(spec);
      return pk;
    }
    catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static PublicKey createPublicKey(byte[] bob) {
    try {
      KeyFactory kf = KeyFactory.getInstance("RSA");
      X509EncodedKeySpec spec = new X509EncodedKeySpec(bob);
      PublicKey pub = kf.generatePublic(spec);
      return pub;
    }
    catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static PrivateKey loadPrivateKey(Path pkDer) {
    return createPrivateKey(Unchecked.call(()->Files.readAllBytes(pkDer)));
  }
  
  public static PublicKey loadPublicKey(Path pubDer) {
    return createPublicKey(Unchecked.call(()->Files.readAllBytes(pubDer)));
  }
  
  public static KeyPair loadKeyPair(Path pkDer, Path pubDer) {
    return new KeyPair(loadPublicKey(pubDer), loadPrivateKey(pkDer));
  }
  
  public static Cipher encryptCipher(Key key, IvParameterSpec iv, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.ENCRYPT_MODE, key, iv));
    return cipher;
  }
  
  public static Cipher decryptCipher(Key key, IvParameterSpec iv, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.DECRYPT_MODE, key, iv));
    return cipher;
  }
  
  public static IvParameterSpec randomIV() {
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[16];
    sr.nextBytes(ivb);
    return new IvParameterSpec(ivb);
  }
  
  public static ByteBuffer encryptWithIv(Key key, CryptoAlgorithm algo, ByteBuffer content) {
    IvParameterSpec iv = randomIV();
    Cipher c = encryptCipher(key, randomIV(), algo);
    int encsize = Short.BYTES + iv.getIV().length + c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    output.putShort((short) iv.getIV().length);
    output.put(iv.getIV());
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer encrypt(Key key, IvParameterSpec iv, CryptoAlgorithm algo, ByteBuffer content) {
    Cipher c = encryptCipher(key, iv, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer decryptWithIv(Key key, CryptoAlgorithm algo, ByteBuffer content) {
    int ivlen = content.getShort();
    byte[] ivb = new byte[ivlen];
    content.get(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    Cipher c = decryptCipher(key, iv, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer decrypt(SecretKey key, IvParameterSpec iv, CryptoAlgorithm algo, ByteBuffer content) {
    Cipher c = decryptCipher(key, iv, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
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
  
}
