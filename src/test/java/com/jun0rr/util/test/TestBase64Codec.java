/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Base64Codec;
import java.nio.ByteBuffer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestBase64Codec {
  
  public static final String CONTENT = "Hello World!!";
  
  public static final byte[] CONTENTBUF = new byte[] {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33, 33};
  
  public static final String BASE64STR = "SGVsbG8gV29ybGQhIQ==";
  
  public static final byte[] BASE64BUF = new byte[] {83, 71, 86, 115, 98, 71, 56, 103, 86, 50, 57, 121, 98, 71, 81, 104, 73, 81, 61, 61};
  
  @Test
  public void encode_buf() {
    Assertions.assertEquals(ByteBuffer.wrap(BASE64BUF), Base64Codec.encode(ByteBuffer.wrap(CONTENTBUF)));
  }
  
  @Test
  public void encode_str() {
    Assertions.assertEquals(ByteBuffer.wrap(BASE64BUF), Base64Codec.encode(CONTENT));
  }
  
  @Test
  public void encodeToString_buf() {
    Assertions.assertEquals(BASE64STR, Base64Codec.encodeToString(ByteBuffer.wrap(CONTENTBUF)));
  }
  
  @Test
  public void encodeToString_str() {
    Assertions.assertEquals(BASE64STR, Base64Codec.encodeToString(CONTENT));
  }
  
  @Test
  public void decode_buf() {
    Assertions.assertEquals(ByteBuffer.wrap(CONTENTBUF), Base64Codec.decode(ByteBuffer.wrap(BASE64BUF)));
  }
  
  @Test
  public void decode_str() {
    Assertions.assertEquals(ByteBuffer.wrap(CONTENTBUF), Base64Codec.decode(BASE64STR));
  }
  
  @Test
  public void decodeToString_buf() {
    Assertions.assertEquals(CONTENT, Base64Codec.decodeToString(ByteBuffer.wrap(BASE64BUF)));
  }
  
  @Test
  public void decodeToString_str() {
    Assertions.assertEquals(CONTENT, Base64Codec.decodeToString(BASE64STR));
  }
  
}
