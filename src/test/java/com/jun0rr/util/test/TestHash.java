/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Hash;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestHash {
  
  public static final String TEXT = "hello world!!";
  
  public static final String MD5 = "570599D420ACC25723B337B0DB95C7C7";
  
  public static final String SHA1 = "13CCCF0A41DE644625FAAD47EB59D388BC50E6C0";
  
  public static final String SHA256 = "8380C4C6720E0D5CE4789BF72DF03A6E1B3ED80891F3ADBE8833C760399B8E91";
  
  public static final String SHA512 = "34CD46F9EA56BF3F8861C4745D5828CE93680B862BB1ACF636D5A730A64726BC8D03E85E626B6C991974E5DD8166A55E043B4E197569BFC523BF103967321B2E";
  
  @Test
  public void hash() {
    Assertions.assertEquals(MD5, Hash.md5().of(TEXT));
    Assertions.assertEquals(SHA1, Hash.sha1().of(TEXT));
    Assertions.assertEquals(SHA256, Hash.sha256().of(TEXT));
    Assertions.assertEquals(SHA512, Hash.sha512().of(TEXT));
  }
  
}
