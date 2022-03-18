/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util;

import com.jun0rr.util.match.Match;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 *
 * @author F6036477
 */
public class Base64Codec {
  
  public static ByteBuffer encode(ByteBuffer buf) {
    return Base64.getEncoder().encode(Match.notNull(buf).getOrFail());
  }
  
  public static ByteBuffer encode(String str) {
    return encode(StandardCharsets.UTF_8.encode(Match.notEmpty(str).getOrFail()));
  }
  
  public static String encodeToString(ByteBuffer buf) {
    return StandardCharsets.UTF_8.decode(encode(buf)).toString();
  }
  
  public static String encodeToString(String str) {
    return StandardCharsets.UTF_8.decode(encode(str)).toString();
  }
  
  public static ByteBuffer decode(ByteBuffer buf) {
    return Base64.getDecoder().decode(Match.notNull(buf).getOrFail());
  }
  
  public static ByteBuffer decode(String str) {
    return decode(StandardCharsets.UTF_8.encode(Match.notEmpty(str).getOrFail()));
  }
  
  public static String decodeToString(ByteBuffer buf) {
    return StandardCharsets.UTF_8.decode(decode(buf)).toString();
  }
  
  public static String decodeToString(String str) {
    return StandardCharsets.UTF_8.decode(decode(str)).toString();
  }
  
}
