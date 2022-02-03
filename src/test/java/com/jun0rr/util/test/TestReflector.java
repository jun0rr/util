/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.CharsetString;
import com.jun0rr.util.Reflector;
import com.jun0rr.util.UTF8String;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestReflector {
  
  public static final String TEXT = "hello world!!";
  
  public static final byte[] BYTES = {104, 101, 108, 108, 111, 32, 119, 111, 114, 108, 100, 33, 33};
  
  public static final Class CLAZZ = UTF8String.class;
  
  @Test
  public void constructor() {
    Reflector ref = Reflector.of(CLAZZ).selectConstructor(String.class);
    Assertions.assertEquals(CLAZZ, ref.getTarget());
    Assertions.assertEquals(Boolean.TRUE, ref.isConstructorPresent());
    Assertions.assertEquals(TEXT, ref.create(TEXT).toString());
  }
  
  @Test
  public void invoke() {
    Reflector ref = Reflector.of(CLAZZ).selectConstructor(String.class);
    ref = Reflector.of(ref.create(TEXT)).selectMethod("getBytes");
    Assertions.assertEquals(Boolean.TRUE, ref.isMethodPresent());
    Assertions.assertArrayEquals(BYTES, (byte[]) ref.invoke());
  }
  
  @Test
  public void field() {
    Reflector ref = Reflector.of(CLAZZ).selectConstructor(String.class);
    ref = Reflector.of(ref.create(TEXT)).selectField("utf8");
    Assertions.assertEquals(Boolean.TRUE, ref.isFieldPresent());
    Assertions.assertEquals("UTF-8", ref.get());
  }
  
}
