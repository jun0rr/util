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
import java.nio.file.Path;
import java.nio.file.Paths;
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
  public void encrypt_secretkey() {
    System.out.println("--- encrypt_secretkey() ---");
    try {
    ByteBuffer buf = StandardCharsets.UTF_8.encode(content);
    SecretKey key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 32);
    CryptoAlgorithm alg = CryptoAlgorithm.AES_CBC_PKCS5PADDING;
    ByteBuffer out = Crypto.encryptWithIv(key, alg, buf);
    System.out.println("* out......: " + out);
    System.out.println("* content..: " + content);
    System.out.println("* base64...: " + Base64Codec.encodeToString(buf.flip()));
    System.out.println("* encrypted: " + Base64Codec.encodeToString(out));
    key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 32);
    buf = Crypto.decryptWithIv(key, alg, out.flip());
    System.out.println("* decrypted: " + StandardCharsets.UTF_8.decode(buf).toString());
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void encrypt_file() {
    try {
    Path src = Paths.get("C:/Users/F6036477/Documents/Certificado Internet das Coisas.pdf");
    Path dst = Paths.get("C:/Users/F6036477/Documents/Certificado Internet das Coisas.txt");
    SecretKey key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 32);
    Crypto.encryptFile(key, CryptoAlgorithm.AES_CBC_PKCS5PADDING, src, dst, true);
    src = dst;
    dst = Paths.get("C:/Users/F6036477/Documents/Certificado Internet das Coisas Decrypt.pdf");
    Crypto.decryptFile(key, CryptoAlgorithm.AES_CBC_PKCS5PADDING, src, dst, true);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  //@Test
  public void test2() throws Exception {
    Cipher c = Cipher.getInstance(CryptoAlgorithm.AES_CBC_PKCS5PADDING.getAlgorithmName());
    //c.i
  }
  
}
