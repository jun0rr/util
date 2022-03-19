/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.crypto.Crypto;
import com.jun0rr.util.crypto.DigestAlgorithm;
import com.jun0rr.util.crypto.Hash;
import com.jun0rr.util.crypto.KeyAlgorithm;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestHash {
  
  public static final String TEXT = "hello world!!";
  
  public static final String MD5 = "570599D420ACC25723B337B0DB95C7C7";
  
  public static final String SHA1 = "13CCCF0A41DE644625FAAD47EB59D388BC50E6C0";
  
  public static final String SHA256 = "8380C4C6720E0D5CE4789BF72DF03A6E1B3ED80891F3ADBE8833C760399B8E91";
  
  public static final String SHA512 = "34CD46F9EA56BF3F8861C4745D5828CE93680B862BB1ACF636D5A730A64726BC8D03E85E626B6C991974E5DD8166A55E043B4E197569BFC523BF103967321B2E";
  
  @Test
  public void hash() {
    Assertions.assertEquals(MD5, Hash.md5().of(TEXT));
    Assertions.assertEquals(SHA1, Hash.sha1().of(TEXT));
    Assertions.assertEquals(SHA256, Hash.sha256().of(TEXT));
    Assertions.assertEquals(SHA512, Hash.sha512().of(TEXT));
  }
  
  
  @Test
  public void hash_size() {
    System.out.println("--- hash_size ---");
    System.out.println("SHA-256.length.: " + Hash.sha256().put(TEXT).getBytes().length);
    System.out.println("SHA-1..........: " + Hash.sha1().of(TEXT));
    System.out.println("SHA-1.length...: " + Hash.sha1().put(TEXT).getBytes().length);
    System.out.println("SHA-512........: " + Hash.sha512().of(TEXT));
    System.out.println("SHA-512.length.: " + Hash.sha512().put(TEXT).getBytes().length);
    System.out.println("SHA3-512.......: " + Hash.create(DigestAlgorithm.SHA3_512).of(TEXT));
    System.out.println("SHA3-512.length: " + Hash.create(DigestAlgorithm.SHA3_512).put(TEXT).getBytes().length);
    System.out.println("SHA-384........: " + Hash.create(DigestAlgorithm.SHA_384).of(TEXT));
    System.out.println("SHA-384.length.: " + Hash.create(DigestAlgorithm.SHA_384).put(TEXT).getBytes().length);
  }
  
  @Test
  public void test_mac() throws NoSuchAlgorithmException, InvalidKeyException, InvalidAlgorithmParameterException {
    SecretKey key = Crypto.createSecretKey("inadonuj", KeyAlgorithm.AES, 32);
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[16];
    sr.nextBytes(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    ByteBuffer input = StandardCharsets.UTF_8.encode("0123456789");
    Mac mac = Mac.getInstance(KeyAlgorithm.HMAC_SHA256.getAlgorithmName());
    mac.init(key);
    mac.update(input);
    byte[] hmac = mac.doFinal();
    System.out.println("* hmac=" + Arrays.toString(hmac));
    System.out.println("* hmac.length=" + hmac.length);
    
    mac = Mac.getInstance(KeyAlgorithm.HMAC_SHA256.getAlgorithmName());
    mac.init(key);
    mac.update(input.flip());
    hmac = mac.doFinal();
    System.out.println("* hmac=" + Arrays.toString(hmac));
    System.out.println("* hmac.length=" + hmac.length);
    
    input = StandardCharsets.UTF_8.encode("01234567890123456789012345678901234567890123456789");
    mac = Mac.getInstance(KeyAlgorithm.HMAC_SHA256.getAlgorithmName());
    mac.init(key);
    mac.update(input);
    hmac = mac.doFinal();
    System.out.println("* hmac=" + Arrays.toString(hmac));
    System.out.println("* hmac.length=" + hmac.length);
  }
  
}
