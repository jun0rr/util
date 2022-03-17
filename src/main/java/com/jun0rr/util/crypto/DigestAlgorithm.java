/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

/**
 *
 * @author F6036477
 */
public enum DigestAlgorithm implements Algorithm {
  
  MD2("MD2"),
  MD5("MD5"),
  SHA_1("SHA-1"),
  SHA_224("SHA-224"),
  SHA_256("SHA-256"),
  SHA_384("SHA-384"),
  SHA_512("SHA-512"),
  SHA_512_224("SHA-512/224"),
  SHA_512_256("SHA-512/256"),
  SHA3_224("SHA3-224"),
  SHA3_256("SHA3-256"),
  SHA3_384("SHA3-384"),
  SHA3_512("SHA3-512");
  
  private DigestAlgorithm(String name) {
    this.name = name;
  }
  
  private final String name;
  
  @Override
  public String getAlgorithmName() {
    return name;
  }
  
  public Hash getHash() {
    return new Hash(this);
  }
  
  public static DigestAlgorithm parse(String name) {
    if(name == null || name.isBlank()) return null;
    else if(MD2.name.equals(name)) return MD2;
    else if(MD5.name.equals(name)) return MD5;
    else if(SHA_1.name.equals(name)) return SHA_1;
    else if(SHA_224.name.equals(name)) return SHA_224;
    else if(SHA_256.name.equals(name)) return SHA_256;
    else if(SHA_384.name.equals(name)) return SHA_384;
    else if(SHA_512.name.equals(name)) return SHA_512;
    else if(SHA_512_224.name.equals(name)) return SHA_512_224;
    else if(SHA_512_256.name.equals(name)) return SHA_512_256;
    else if(SHA3_224.name.equals(name)) return SHA3_224;
    else if(SHA3_256.name.equals(name)) return SHA3_256;
    else if(SHA3_384.name.equals(name)) return SHA3_384;
    else if(SHA3_512.name.equals(name)) return SHA3_512;
    else {
      throw new IllegalArgumentException("Unknown DigestAlgorithm: " + name);
    }
  }
  
}
