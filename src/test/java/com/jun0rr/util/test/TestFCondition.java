/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.FCondition;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestFCondition {
  
  @Test
  public void test_integer() {
    FCondition<Integer,String> c = FCondition.<Integer,String>of(i->i <= 5)
        .then(i->String.format("%d is lesser then 5", i))
        .elseIf(i->i <= 10)
        .then(i->String.format("%d is lesser then 10", i))
        .elseIf(i->i <= 15)
        .then(i->String.format("%d is lesser then 15", i))
        .elseIf(i->i <= 20)
        .then(i->String.format("%d is lesser then 20", i))
        .elseApply(i->String.format("%d is greater then 20", i));
    Assertions.assertEquals("7 is lesser then 10", c.clone().eval(7));
    Assertions.assertEquals("3 is lesser then 5", c.clone().eval(3));
    Assertions.assertEquals("11 is lesser then 15", c.clone().eval(11));
    Assertions.assertEquals("16 is lesser then 20", c.clone().eval(16));
    Assertions.assertEquals("500 is greater then 20", c.clone().eval(500));
  }
  
}
