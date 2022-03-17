/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Base64Codec;
import com.jun0rr.util.crypto.Crypto;
import com.jun0rr.util.crypto.CryptoAlgorithm;
import com.jun0rr.util.crypto.KeyAlgorithm;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestCrypto {
  
  public static final String content = "Hello World!! Hello World!! Hello World!! Hello World!! Hello World!!";
  
  @Test
  public void test() {
    ByteBuffer buf = StandardCharsets.UTF_8.encode(content);
    SecretKey key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 32);
    ByteBuffer out = Crypto.encrypt(key, CryptoAlgorithm.AES_CBC_PKCS5PADDING, buf);
    System.out.println("* content..: " + content);
    System.out.println("* base64...: " + Base64Codec.encodeToString(buf.flip()));
    System.out.println("* encrypted: " + Base64Codec.encodeToString(out));
    buf = Crypto.decrypt(key, CryptoAlgorithm.AES_CBC_PKCS5PADDING, out);
    System.out.println("* decrypted: " + Base64Codec.encodeToString(buf));
  }
  
  @Test
  public void test2() throws Exception {
    Cipher c = Cipher.getInstance(CryptoAlgorithm.AES_CBC_PKCS5PADDING.getAlgorithmName());
    c.i
  }
  
}
