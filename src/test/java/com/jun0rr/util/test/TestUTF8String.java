/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.UTF8String;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestUTF8String {
  
  public static final byte[] BYTES = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33, 33};
  
  public static final String TEXT = "hello world!!";
  
  @Test
  public void bytes_to_string() {
    Assertions.assertEquals(TEXT, UTF8String.from(BYTES).toString());
  }
  
  @Test
  public void string_to_bytes() {
    Assertions.assertArrayEquals(BYTES, UTF8String.from(TEXT).getBytes());
  }
  
}
