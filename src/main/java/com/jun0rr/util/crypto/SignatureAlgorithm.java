/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.jun0rr.util.crypto;

/**
 *
 * @author F6036477
 */
public enum SignatureAlgorithm implements Algorithm {
  
  NONE_WITH_RSA("NONEwithRSA"),
  MD2_WITH_RSA("MD2withRSA"),
  MD5_WITH_RSA("MD5withRSA"),
  SHA1_WITH_RSA("SHA1withRSA"),
  SHA224_WITH_RSA("SHA224withRSA"),
  SHA256_WITH_RSA("SHA256withRSA"),
  SHA384_WITH_RSA("SHA384withRSA"),
  SHA512_WITH_RSA("SHA512withRSA"),
  SHA512_224_WITH_RSA("SHA512/224withRSA"),
  SHA512_256_WITH_RSA("SHA512/256withRSA"),
  RSASSA_PSS("RSASSA-PSS"),
  NONE_WITH_DSA("NONEwithDSA"),
  SHA1_WITH_DSA("SHA1withDSA"),
  SHA224_WITH_DSA("SHA224withDSA"),
  SHA256_WITH_DSA("SHA256withDSA"),
  SHA384_WITH_DSA("SHA384withDSA"),
  SHA512_WITH_DSA("SHA512withDSA"),
  NONE_WITH_ECDSA("NONEwithECDSA"),
  SHA1_WITH_ECDSA("SHA1withECDSA"),
  SHA224_WITH_ECDSA("SHA224withECDSA"),
  SHA256_WITH_ECDSA("SHA256withECDSA"),
  SHA384_WITH_ECDSA("SHA384withECDSA"),
  SHA512_WITH_ECDSA("SHA512withECDSA");
  
  private SignatureAlgorithm(String name) {
    this.name = name;
  }
  
  private final String name;
  
  @Override
  public String getAlgorithmName() {
    return name;
  }
  
  public static SignatureAlgorithm parse(String name) {
    if(name == null || name.isBlank()) return null;
    else if(NONE_WITH_RSA.name.equals(name)) return NONE_WITH_RSA;
    else if(MD2_WITH_RSA.name.equals(name)) return MD2_WITH_RSA;
    else if(MD5_WITH_RSA.name.equals(name)) return MD5_WITH_RSA;
    else if(SHA1_WITH_RSA.name.equals(name)) return SHA1_WITH_RSA;
    else if(SHA224_WITH_RSA.name.equals(name)) return SHA224_WITH_RSA;
    else if(SHA256_WITH_RSA.name.equals(name)) return SHA256_WITH_RSA;
    else if(SHA384_WITH_RSA.name.equals(name)) return SHA384_WITH_RSA;
    else if(SHA512_WITH_RSA.name.equals(name)) return SHA512_WITH_RSA;
    else if(SHA512_224_WITH_RSA.name.equals(name)) return SHA512_224_WITH_RSA;
    else if(SHA512_256_WITH_RSA.name.equals(name)) return SHA512_256_WITH_RSA;
    else if(RSASSA_PSS.name.equals(name)) return RSASSA_PSS;
    else if(NONE_WITH_DSA.name.equals(name)) return NONE_WITH_DSA;
    else if(SHA1_WITH_DSA.name.equals(name)) return SHA1_WITH_DSA;
    else if(SHA224_WITH_DSA.name.equals(name)) return SHA224_WITH_DSA;
    else if(SHA256_WITH_DSA.name.equals(name)) return SHA256_WITH_DSA;
    else if(SHA384_WITH_DSA.name.equals(name)) return SHA384_WITH_DSA;
    else if(SHA512_WITH_DSA.name.equals(name)) return SHA512_WITH_DSA;
    else if(NONE_WITH_ECDSA.name.equals(name)) return NONE_WITH_ECDSA;
    else if(SHA1_WITH_ECDSA.name.equals(name)) return SHA1_WITH_ECDSA;
    else if(SHA224_WITH_ECDSA.name.equals(name)) return SHA224_WITH_ECDSA;
    else if(SHA256_WITH_ECDSA.name.equals(name)) return SHA256_WITH_ECDSA;
    else if(SHA384_WITH_ECDSA.name.equals(name)) return SHA384_WITH_ECDSA;
    else if(SHA512_WITH_ECDSA.name.equals(name)) return SHA512_WITH_ECDSA;
    else {
      throw new IllegalArgumentException("Unknown SignatureAlgorithm: " + name);
    }
  }
  
}
