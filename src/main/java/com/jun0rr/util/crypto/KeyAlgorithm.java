/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jun0rr.util.crypto;

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
