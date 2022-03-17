/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.crypto;

import com.jun0rr.util.match.Match;
import java.security.Key;
import javax.crypto.spec.IvParameterSpec;

/**
 *
 * @author F6036477
 */
public class CryptoKey {
  
  private final Key key;
  
  private final IvParameterSpec iv;
  
  public CryptoKey(Key key, IvParameterSpec iv) {
    this.key = Match.notNull(key).getOrFail("Bad null Key");
    this.iv = Match.notNull(iv).getOrFail("Bad null IvParamenterSpec");
  }
  
  public Key getKey() {
    return key;
  }
  
  public IvParameterSpec getIV() {
    return iv;
  }
  
  public byte[] getIVBytes() {
    return iv.getIV();
  }
  
}
