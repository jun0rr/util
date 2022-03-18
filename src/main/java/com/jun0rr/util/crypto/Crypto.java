/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.Unchecked;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
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
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
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
    return createSecretKey(key, algo, 256, 32);
  }
  
  public static SecretKey createSecretKey(byte[] key, KeyAlgorithm algo, int iteration, int size) {
    byte[] bkey = new byte[size];
    byte[] hash = Hash.sha512().put(key).getBytes();
    int x = Math.max(1, Math.round(size / Integer.valueOf(hash.length).floatValue()));
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
  
  public static Cipher encryptCipher(SecretKey key, IvParameterSpec iv, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.ENCRYPT_MODE, key, iv));
    return cipher;
  }
  
  public static Cipher decryptCipher(SecretKey key, IvParameterSpec iv, CryptoAlgorithm algo) {
    Cipher cipher = Unchecked.call(()->Cipher.getInstance(algo.getAlgorithmName()));
    Unchecked.call(()->cipher.init(Cipher.DECRYPT_MODE, key, iv));
    return cipher;
  }
  
  public static CipherInputStream encryptInputStream(SecretKey key, IvParameterSpec iv, InputStream in, CryptoAlgorithm algo) {
    return new CipherInputStream(in, encryptCipher(key, iv, algo));
  }
  
  public static CipherInputStream decryptInputStream(SecretKey key, IvParameterSpec iv, InputStream in, CryptoAlgorithm algo) {
    return new CipherInputStream(in, decryptCipher(key, iv, algo));
  }
  
  public static CipherOutputStream encryptOutputStream(SecretKey key, IvParameterSpec iv, OutputStream out, CryptoAlgorithm algo) {
    return new CipherOutputStream(out, encryptCipher(key, iv, algo));
  }
  
  public static CipherOutputStream decryptOutputStream(SecretKey key, IvParameterSpec iv, OutputStream out, CryptoAlgorithm algo) {
    return new CipherOutputStream(out, decryptCipher(key, iv, algo));
  }
  
  public static ByteBuffer encryptWithIv(SecretKey key, CryptoAlgorithm algo, ByteBuffer content) {
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[16];
    sr.nextBytes(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    Cipher c = encryptCipher(key, iv, algo);
    int encsize = Short.BYTES + iv.getIV().length + c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    output.putShort((short) iv.getIV().length);
    output.put(iv.getIV());
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer encrypt(SecretKey key, IvParameterSpec iv, CryptoAlgorithm algo, ByteBuffer content) {
    Cipher c = encryptCipher(key, iv, algo);
    int encsize = c.getOutputSize(content.remaining());
    ByteBuffer output = content.isDirect() ? ByteBuffer.allocateDirect(encsize) : ByteBuffer.allocate(encsize);
    Unchecked.call(()->c.doFinal(content, output));
    return output.flip();
  }
  
  public static ByteBuffer decryptWithIv(SecretKey key, CryptoAlgorithm algo, ByteBuffer content) {
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
  
  public static void encryptFile(SecretKey key, CryptoAlgorithm algo, Path src, Path dst, boolean base64encode) {
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[16];
    sr.nextBytes(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    try(
        ReadableByteChannel rc = FileChannel.open(src, StandardOpenOption.READ);
        WritableByteChannel wc = base64encode 
            ? Channels.newChannel(Base64.getEncoder().wrap(new FileOutputStream(dst.toFile())))
            : FileChannel.open(dst, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
    ) {
      Cipher c = encryptCipher(key, iv, algo);
      wc.write(ByteBuffer.wrap(ivb));
      encryptChannel(c, rc, wc);
    }
    catch(Exception e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static void encryptFile2(SecretKey key, CryptoAlgorithm algo, Path src, Path dst, boolean base64encode) {
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[16];
    sr.nextBytes(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    Cipher c = encryptCipher(key, iv, algo);
    InputStream in;
    OutputStream out;
    try {
      if(!Files.exists(dst)) {
        Files.createFile(dst);
      }
      in = new FileInputStream(src.toFile());
      out = new FileOutputStream(dst.toFile());
      out.write(ivb);
      if(base64encode) {
        out = Base64.getEncoder().wrap(out);
      }
      out = new CipherOutputStream(out, c);
      int read;
      byte[] buf = new byte[2048];
      while((read = in.read(buf)) != -1) {
        
      }
    }
    catch(Exception e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static void encryptChannel(Cipher c, ReadableByteChannel rc, WritableByteChannel wc) {
    ByteBuffer rbuf = ByteBuffer.allocateDirect(2048);
    ByteBuffer wbuf = ByteBuffer.allocateDirect(4096);
    try {
      int read;
      while((read = rc.read(rbuf)) != -1) {
        c.update(rbuf.flip(), wbuf);
        wc.write(wbuf.flip());
        rbuf.compact();
        wbuf.compact();
      }
      wc.write(ByteBuffer.wrap(c.doFinal()));
    }
    catch(Exception e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static void decryptFile(SecretKey key, CryptoAlgorithm algo, Path src, Path dst, boolean base64decode) {
    try(
        ReadableByteChannel rc = base64decode 
            ? Channels.newChannel(Base64.getDecoder().wrap(new FileInputStream(src.toFile()))) 
            : FileChannel.open(src, StandardOpenOption.READ);
        WritableByteChannel wc = FileChannel.open(dst, StandardOpenOption.WRITE, StandardOpenOption.CREATE);
    ) {
      ByteBuffer ivb = ByteBuffer.allocate(16);
      rc.read(ivb);
      IvParameterSpec iv = new IvParameterSpec(ivb.array());
      Cipher c = decryptCipher(key, iv, algo);
      decryptChannel(c, rc, wc);
    }
    catch(Exception e) {
      throw Unchecked.unchecked(e);
    }
  }
  
  public static void decryptChannel(Cipher c, ReadableByteChannel rc, WritableByteChannel wc) {
    try {
      ByteBuffer rbuf = ByteBuffer.allocateDirect(2048);
      ByteBuffer wbuf = ByteBuffer.allocateDirect(4096);
      int read;
      while((read = rc.read(rbuf)) != -1) {
        c.update(rbuf.flip(), wbuf);
        wc.write(wbuf.flip());
        rbuf.compact();
        wbuf.compact();
      }
      wc.write(ByteBuffer.wrap(c.doFinal()));
    }
    catch(Exception e) {
      throw Unchecked.unchecked(e);
    }
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
