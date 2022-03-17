/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.Unchecked;
import javax.crypto.Cipher;

/**
 *
 * @author F6036477
 */
public enum CryptoAlgorithm implements Algorithm {
  
  /**
   * KEY_SIZE=128
   */
  AES_CBC_NOPADDING("AES/CBC/NoPadding"),
  
  /**
   * KEY_SIZE=128
   */
  AES_CBC_PKCS5PADDING("AES/CBC/PKCS5Padding"),
  
  /**
   * KEY_SIZE=128
   */
  AES_ECB_NOPADDING("AES/ECB/NoPadding"),
  
  /**
   * KEY_SIZE=128
   */
  AES_ECB_PKCS5PADDING("AES/ECB/PKCS5Padding"),
  
  /**
   * KEY_SIZE=128
   */
  AES_GCM_NOPADDING("AES/GCM/NoPadding"),
  
  /**
   * KEY_SIZE=56
   */
  DES_CBC_NOPADDING("DES/CBC/NoPadding"),
  
  /**
   * KEY_SIZE=56
   */
  DES_CBC_PKCS5PADDING("DES/CBC/PKCS5Padding"),
  
  /**
   * KEY_SIZE=56
   */
  DES_ECB_NOPADDING("DES/ECB/NoPadding"),
  
  /**
   * KEY_SIZE=56
   */
  DES_ECB_PKCS5PADDING("DES/ECB/PKCS5Padding"),
  
  /**
   * KEY_SIZE=168
   */
  DES_EDE_CBC_NOPADDING("DESede/CBC/NoPadding"),
  
  /**
   * KEY_SIZE=168
   */
  DES_EDE_CBC_PKCS5PADDING("DESede/CBC/PKCS5Padding"),
  
  /**
   * KEY_SIZE=168
   */
  DES_EDE_ECB_NOPADDING("DESede/ECB/NoPadding"),
  
  /**
   * KEY_SIZE=168
   */
  DES_EDE_ECB_PKCS5PADDING("DESede/ECB/PKCS5Padding"),
  
  /**
   * KEY_SIZE=(1024, 2048)
   */
  RSA_ECB_PKCS1PADDING("RSA/ECB/PKCS1Padding"),
  
  /**
   * KEY_SIZE=(1024, 2048)
   */
  RSA_ECB_OAEP_WITH_SHA_1_AND_MGF1PADDING("RSA/ECB/OAEPWithSHA-1AndMGF1Padding"),
  
  /**
   * KEY_SIZE=(1024, 2048)
   */
  RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1PADDING("RSA/ECB/OAEPWithSHA-256AndMGF1Padding");
  
  private CryptoAlgorithm(String name) {
    this.name = name;
  }
  
  private final String name;
  
  @Override
  public String getAlgorithmName() {
    return name;
  }
  
  
  public static CryptoAlgorithm parse(String name) {
    if(name == null || name.isBlank()) return null;
    else if(AES_CBC_NOPADDING.name.equals(name)) return AES_CBC_NOPADDING;
    else if(AES_CBC_PKCS5PADDING.name.equals(name)) return AES_CBC_PKCS5PADDING;
    else if(AES_ECB_NOPADDING.name.equals(name)) return AES_ECB_NOPADDING;
    else if(AES_ECB_PKCS5PADDING.name.equals(name)) return AES_ECB_PKCS5PADDING;
    else if(AES_GCM_NOPADDING.name.equals(name)) return AES_GCM_NOPADDING;
    else if(DES_CBC_NOPADDING.name.equals(name)) return DES_CBC_NOPADDING;
    else if(DES_CBC_PKCS5PADDING.name.equals(name)) return DES_CBC_PKCS5PADDING;
    else if(DES_ECB_NOPADDING.name.equals(name)) return DES_ECB_NOPADDING;
    else if(DES_ECB_PKCS5PADDING.name.equals(name)) return DES_ECB_PKCS5PADDING;
    else if(DES_EDE_CBC_NOPADDING.name.equals(name)) return DES_EDE_CBC_NOPADDING;
    else if(DES_EDE_CBC_PKCS5PADDING.name.equals(name)) return DES_EDE_CBC_PKCS5PADDING;
    else if(DES_EDE_ECB_NOPADDING.name.equals(name)) return DES_EDE_ECB_NOPADDING;
    else if(DES_EDE_ECB_PKCS5PADDING.name.equals(name)) return DES_EDE_ECB_PKCS5PADDING;
    else if(RSA_ECB_PKCS1PADDING.name.equals(name)) return RSA_ECB_PKCS1PADDING;
    else if(RSA_ECB_OAEP_WITH_SHA_1_AND_MGF1PADDING.name.equals(name)) return RSA_ECB_OAEP_WITH_SHA_1_AND_MGF1PADDING;
    else if(RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1PADDING.name.equals(name)) return RSA_ECB_OAEP_WITH_SHA_256_AND_MGF1PADDING;
    else {
      throw new IllegalArgumentException("Unknown CryptoAlgorithm: " + name);
    }
  }
  
}
