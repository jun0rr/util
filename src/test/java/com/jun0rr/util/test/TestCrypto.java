/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Base64Codec;
import com.jun0rr.util.crypto.Crypto;
import com.jun0rr.util.crypto.CryptoAlgorithm;
import com.jun0rr.util.crypto.KeyAlgorithm;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyPair;
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
    SecretKey key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 256);
    CryptoAlgorithm alg = CryptoAlgorithm.AES_CBC_PKCS5PADDING;
    ByteBuffer out = Crypto.encryptWithIv(key, alg, buf);
    System.out.println("* out......: " + out);
    System.out.println("* content..: " + content);
    System.out.println("* base64...: " + Base64Codec.encodeToString(buf.flip()));
    System.out.println("* encrypted: " + Base64Codec.encodeToString(out));
    key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 256);
    buf = Crypto.decryptWithIv(key, alg, out.flip());
    System.out.println("* decrypted: " + StandardCharsets.UTF_8.decode(buf).toString());
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void gen_keypair() {
    System.out.println("--- gen_keypair() ---");
    KeyPair pair = Crypto.createKeyPair(4096, KeyAlgorithm.RSA);
    System.out.println("* PrivateKey.: " + Base64Codec.encodeToString(ByteBuffer.wrap(pair.getPrivate().getEncoded())));
    System.out.println("* PublicKey..: " + Base64Codec.encodeToString(ByteBuffer.wrap(pair.getPublic().getEncoded())));
  }
  
  @Test
  public void load_keypair() throws IOException {
    System.out.println("--- load_keypair() ---");
    KeyPair pair = Crypto.loadKeyPair(Paths.get("C:/Java/dodge-pk.der"), Paths.get("C:/Java/dodge-pub.der"));
    System.out.println("* PrivateKey.: " + Base64Codec.encodeToString(ByteBuffer.wrap(pair.getPrivate().getEncoded())));
    System.out.println("* PublicKey..: " + Base64Codec.encodeToString(ByteBuffer.wrap(pair.getPublic().getEncoded())));
  }
  
  @Test
  public void rsa_encrypt_password() {
    System.out.println("--- rsa_encrypt_password() ---");
    try {
    //SecretKey key = Crypto.createSecretKey("32132155", KeyAlgorithm.AES, 256);
    SecretKey key = Crypto.createSecretKey(256, KeyAlgorithm.AES);
    ByteBuffer buf = ByteBuffer.wrap(key.getEncoded());
    System.out.println("* SecretKey...........: " + Base64Codec.encodeToString(buf));
    buf.flip();
    KeyPair pair = Crypto.loadKeyPair(Paths.get("C:/Java/dodge-pk.der"), Paths.get("C:/Java/dodge-pub.der"));
    Cipher c = Cipher.getInstance(CryptoAlgorithm.RSA_ECB_PKCS1PADDING.getAlgorithmName());
    c.init(Cipher.ENCRYPT_MODE, pair.getPublic());
    ByteBuffer enc = ByteBuffer.allocate(c.getOutputSize(buf.remaining()));
    c.doFinal(buf, enc);
    enc.flip();
    System.out.println("* Encrypted SecretKey.: " + Base64Codec.encodeToString(enc));
    enc.flip();
    c = Cipher.getInstance(CryptoAlgorithm.RSA_ECB_PKCS1PADDING.getAlgorithmName());
    c.init(Cipher.DECRYPT_MODE, pair.getPrivate());
    buf = ByteBuffer.allocate(c.getOutputSize(enc.remaining()));
    c.doFinal(enc, buf);
    System.out.println(buf.flip());
    System.out.println("* Decrypted SecretKey.: " + Base64Codec.encodeToString(buf));
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
  
}
