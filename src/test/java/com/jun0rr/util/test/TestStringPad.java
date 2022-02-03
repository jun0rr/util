/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.pserver.tools.StringPad;

/**
 *
 * @author F6036477
 */
public class TestStringPad {
  
  public static final int LEN = 20;
  
  public static final String FILL = "*";
  
  public static final String TEXT = "Hello";
  
  public static final String LPAD = "***************Hello";
  
  public static final String RPAD = "Hello***************";
  
  public static final String CPAD = "*******Hello********";
  
  @Test
  public void lpad() {
    Assertions.assertEquals(LPAD, StringPad.of(TEXT).lpad(FILL, LEN));
  }
  
  @Test
  public void rpad() {
    Assertions.assertEquals(RPAD, StringPad.of(TEXT).rpad(FILL, LEN));
  }
  
  @Test
  public void cpad() {
    Assertions.assertEquals(CPAD, StringPad.of(TEXT).cpad(FILL, LEN));
  }
  
}
