/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jun0rr.util.crypto;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author F6036477
 */
public enum KeyAlgorithm implements Algorithm {
  
  AES("AES"),
  ARCFOUR("ARCFOUR"),
  BLOWFISH("Blowfish"),
  DES("DES"),
  DES_EDE("DESede"),
  HMAC_MD5("HmacMD5"),
  HMAC_SHA1("HmacSHA1"),
  HMAC_SHA224("HmacSHA224"),
  HMAC_SHA256("HmacSHA256"),
  HMAC_SHA384("HmacSHA384"),
  HMAC_SHA512("HmacSHA512"),
  RC2("RC2");
  
  private KeyAlgorithm(String name) {
    this.name = name;
  }
  
  private final String name;
  
  @Override
  public String getAlgorithmName() {
    return name;
  }
  
  public String getPBEKeyAlgorithm(KeyAlgorithm algo) {
    List<KeyAlgorithm> digest = List.of(HMAC_MD5, HMAC_SHA1, HMAC_SHA224, HMAC_SHA256, HMAC_SHA384, HMAC_SHA512);
    Optional<KeyAlgorithm> isDigest = digest.stream().filter(k->k == this).findAny();
    Optional<KeyAlgorithm> algoDigest = digest.stream().filter(k->k == algo).findAny();
    StringBuffer sb = new StringBuffer("PBEWith");
    if(isDigest.isPresent()) {
      sb.append(this.name).append("And").append(algo.name);
    }
    else {
      sb.append(algo.name).append("And").append(this.name);
    }
    return sb.toString();
  }
  
  public String getPBKDF2KeyAlgorithm() {
    List<KeyAlgorithm> digest = List.of(HMAC_MD5, HMAC_SHA1, HMAC_SHA224, HMAC_SHA256, HMAC_SHA384, HMAC_SHA512);
    Optional<KeyAlgorithm> isDigest = digest.stream().filter(k->k == this).findAny();
    if(isDigest.isEmpty()) {
      throw new IllegalStateException("Invalid algorithm to Password-Based Key-Derivation: " + this.name);
    }
    return String.format("PBKDF2With%s", this.name); 
  }
  
  public static KeyAlgorithm parse(String name) {
    if(name == null || name.isBlank()) return null;
    else if(AES.name.equals(name)) return AES;
    else if(ARCFOUR.name.equals(name)) return ARCFOUR;
    else if(BLOWFISH.name.equals(name)) return BLOWFISH;
    else if(DES.name.equals(name)) return DES;
    else if(DES_EDE.name.equals(name)) return DES_EDE;
    else if(HMAC_MD5.name.equals(name)) return HMAC_MD5;
    else if(HMAC_SHA1.name.equals(name)) return HMAC_SHA1;
    else if(HMAC_SHA224.name.equals(name)) return HMAC_SHA224;
    else if(HMAC_SHA256.name.equals(name)) return HMAC_SHA256;
    else if(HMAC_SHA384.name.equals(name)) return HMAC_SHA384;
    else if(HMAC_SHA512.name.equals(name)) return HMAC_SHA512;
    else if(RC2.name.equals(name)) return RC2;
    else {
      throw new IllegalArgumentException("Unknown KeyGeneratorAlgorithm: " + name);
    }
  }
  
}
