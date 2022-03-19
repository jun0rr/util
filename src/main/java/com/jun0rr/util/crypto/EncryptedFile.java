/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.Unchecked;
import com.jun0rr.util.match.Match;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.function.Consumer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import us.pserver.tools.StringPad;

/**
 *
 * @author juno
 */
public class EncryptedFile {
  
  public static class Progress {
    private final EncryptedFile file;
    private final long total;
    private final long current;
    public Progress(EncryptedFile f, long t, long c) {
      this.file = f;
      this.total = t;
      this.current = c;
    }
    public EncryptedFile getEncryptedFile() { return file; }
    public long getTotal() { return total; }
    public long getCurrent() { return current; }
    public String getProgressBar() {
      StringPad ppad = StringPad.of("");
      StringPad spad = StringPad.of("");
      int len = 18;
      float perc = getCurrent() / Long.valueOf(getTotal()).floatValue();
      int cur = Math.round(len * perc);
      return String.format("[%s>%s] %.1f%%", ppad.lpad("=", cur), spad.rpad(" ", len - cur), perc * 100);
    }
  }
  
  public static final int IV_SIZE = 16;
  
  public static final int MAC_SIZE = 32;
  
  public static final int IV_ENC_SIZE = 24;
  
  public static final int BUFFER_SIZE = 4096;
  
  private final Path src;
  
  private final Path dst;
  
  private final SecretKey key;
  
  private final boolean base64;
  
  private final boolean gzip;
  
  private final long split;
  
  private EncryptedFile(SecretKey key, Path src, Path dst, boolean base64, boolean gzip, long split) {
    this.key = Match.notNull(key).getOrFail("Bad null SecretKey");
    this.src = Match.notNull(src).getOrFail("Bad null srouce Path");
    this.dst = Match.notNull(dst).getOrFail("Bad null dest Path");
    this.base64 = base64;
    this.gzip = gzip;
    this.split = split;
  }
  
  public EncryptedFile split(long split) {
    return new EncryptedFile(key, src, dst, base64, gzip, split);
  }
  
  public EncryptedFile withSrc(Path src) {
    return new EncryptedFile(key, src, dst, base64, gzip, split);
  }
  
  public EncryptedFile withDst(Path dst) {
    return new EncryptedFile(key, src, dst, base64, gzip, split);
  }
  
  public EncryptedFile enableBase64Codec() {
    return new EncryptedFile(key, src, dst, true, gzip, split);
  }
  
  public EncryptedFile disableBase64Codec() {
    return new EncryptedFile(key, src, dst, false, gzip, split);
  }
  
  public EncryptedFile enableGzipCodec() {
    return new EncryptedFile(key, src, dst, base64, true, split);
  }
  
  public EncryptedFile disableGzipCodec() {
    return new EncryptedFile(key, src, dst, base64, false, split);
  }
  
  public EncryptedFile encrypt() throws IOException {
    return encrypt(p->{});
  }
  
  private OutputStream getOutput(Path p, Cipher c) throws IOException {
    OutputStream out = new FileOutputStream(p.toFile());
    if(gzip) {
      out = new GZIPOutputStream(out);
    }
    if(base64) {
      out = Base64.getEncoder().wrap(out);
    }
    if(c != null) {
      out = new CipherOutputStream(out, c);
    }
    return out;
  }
  
  public EncryptedFile encrypt(Consumer<Progress> cs) throws IOException {
    SecureRandom sr = new SecureRandom();
    byte[] ivb = new byte[IV_SIZE];
    byte[] buf = new byte[BUFFER_SIZE];
    sr.nextBytes(ivb);
    IvParameterSpec iv = new IvParameterSpec(ivb);
    InputStream in = null;
    OutputStream out = null;
    long total = Files.size(src);
    ByteBuffer hdr = ByteBuffer.allocate(Long.BYTES);
    hdr.putLong(total);
    long count = 0;
    long fcount = 0;
    try {
      Cipher c = Unchecked.call(()->Cipher.getInstance(CryptoAlgorithm.AES_CBC_PKCS5PADDING.getAlgorithmName()));
      Unchecked.call(()->c.init(Cipher.ENCRYPT_MODE, key, iv));
      in = new FileInputStream(src.toFile());
      if(!Files.exists(dst)) {
        Files.createFile(dst);
      }
      int fn = 0;
      Path p = dst;
      if(split > 0) {
        p = Paths.get(String.format("%s.%d", dst, fn));
      }
      out = getOutput(p, null);
      out.write(hdr.array());
      out.write(ivb);
      out = new CipherOutputStream(out, c);
      int read;
      while((read = in.read(buf)) != -1) {
        count += read;
        fcount += read;
        out.write(buf, 0, read);
        cs.accept(new Progress(this, total, count));
        if(split > 0 && fcount >= split && count < total) {
          fn++;
          fcount = 0;
          p = Paths.get(String.format("%s.%d", dst, fn));
          if(!Files.exists(p)) {
            Files.createFile(p);
          }
          out.close();
          out = getOutput(p, c);
        }
      }
    }
    finally {
      in.close();
      out.close();
    }
    return this;
  }
  
  public EncryptedFile decrypt() throws IOException {
    return decrypt(p->{});
  }
  
  private InputStream getInput(Path p, Cipher c) throws IOException {
    InputStream in = new FileInputStream(p.toFile());
    if(gzip) {
      in = new GZIPInputStream(in);
    }
    if(base64) {
      in = Base64.getDecoder().wrap(in);
    }
    if(c != null) {
      in = new CipherInputStream(in, c);
    }
    return in;
  }
  
  public EncryptedFile decrypt(Consumer<Progress> cs) throws IOException {
    InputStream in = null;
    OutputStream out = null;
    try {
      int fn = 0;
      Path p = src;
      if(split > 0) {
        p = Paths.get(String.format("%s.%d", src, fn));
      }
      if(!Files.exists(dst)) {
        Files.createFile(dst);
      }
      out = new FileOutputStream(dst.toFile());
      byte[] hdr = new byte[Long.BYTES];
      byte[] ivb = new byte[IV_SIZE];
      byte[] buf = new byte[BUFFER_SIZE];
      in = getInput(p, null);
      in.read(hdr);
      ByteBuffer bhd = ByteBuffer.wrap(hdr);
      long total = bhd.getLong();
      in.read(ivb);
      IvParameterSpec iv = new IvParameterSpec(ivb);
      Cipher c = Unchecked.call(()->Cipher.getInstance(CryptoAlgorithm.AES_CBC_PKCS5PADDING.getAlgorithmName()));
      Unchecked.call(()->c.init(Cipher.DECRYPT_MODE, key, iv));
      in = new CipherInputStream(in, c);
      long count = 0;
      while(true) {
        int read;
        while((read = in.read(buf)) != -1) {
          out.write(buf, 0, read);
          count += read;
          cs.accept(new Progress(this, total, count));
        }
        if(split > 0) {
          fn++;
          p = Paths.get(String.format("%s.%d", src, fn));
          if(!Files.exists(p)) break;
          in.close();
          in = getInput(p, c);
        }
        else break;
      }
    }
    finally {
      in.close();
      out.close();
    }
    return this;
  }
  
  
  public static EncryptedFile of(SecretKey key, Path src, Path dst) {
    return new EncryptedFile(key, src, dst, true, true, 0);
  }
  
  public static EncryptedFile of(String pwd, Path src, Path dst) {
    return new EncryptedFile(Crypto.createSecretKey(pwd, KeyAlgorithm.AES, 32), src, dst, true, true, 0);
  }
  
}
