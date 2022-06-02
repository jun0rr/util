/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.match.Match;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestMatch {
  
  @Test
  public void notEmpty() {
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.notEmpty("").failIfNotMatch());
  }
  
  @Test
  public void between() {
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.between(6, 0, 5).failIfNotMatch());
  }
  
  @Test
  public void notBetween() {
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.notBetween(4, 0, 5).failIfNotMatch());
  }
  
  @Test
  public void collection_contains() {
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.contains(List.of("a", "b", "c"), "d").failIfNotMatch());
  }
  
  @Test
  public void collection_notContains() {
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.notContains(List.of("a", "b", "c"), "c").failIfNotMatch());
  }
  
  @Test
  public void array_contains() {
    Integer[] is = {0, 1, 2, 3, 4, 5};
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.contains(is, 7).failIfNotMatch());
  }
  
  @Test
  public void array_notContains() {
    Integer[] is = {0, 1, 2, 3, 4, 5};
    Assertions.assertThrows(IllegalArgumentException.class, ()->Match.notContains(is, 2).failIfNotMatch());
  }
  
}
