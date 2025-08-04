/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.FCondition;
import com.jun0rr.util.Host;
import java.io.IOException;
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
        .elseThrows(o->{
          IOException ex = new IOException(String.format("%s is greater then 20", o));
          System.out.println(ex);
          return ex;
        })
        //.elseApply(i->String.format("%d is greater then 20", i))
        ;
    Assertions.assertEquals("7 is lesser then 10", c.eval(7));
    Assertions.assertEquals("3 is lesser then 5", c.eval(3));
    Assertions.assertEquals("11 is lesser then 15", c.eval(11));
    Assertions.assertEquals("16 is lesser then 20", c.eval(16));
    //Assertions.assertEquals("500 is greater then 20", c.clone().eval(500));
    Assertions.assertThrows(IOException.class, ()->c.eval(500));
  }
  
  @Test
  public void test_instanceof() {
    FCondition<Object,String> c = FCondition.<String,String>of(String.class)
        .then(s->String.format("'%s' is a String", s))
        .instanceOf(Integer.class)
        .then(o->String.format("'%s' is a Integer", o))
        .instanceOf(Long.class)
        .then(o->String.format("'%s' is a Long", o))
        .instanceOf(Number.class)
        .then(o->String.format("'%s' is a Number", o))
        .map(o->(Object)o)
        //.otherwise(o->String.format("'%s' is an Object", o))
        .elseThrows(o->{
          IOException ex = new IOException(String.format("'%s' is an Object", o));
          System.out.println(ex);
          return ex;
        })
        ;
    Assertions.assertEquals("'5' is a Integer", c.eval(5));
    Assertions.assertEquals("'100' is a Long", c.eval(100L));
    Assertions.assertEquals("'5' is a Long", c.eval(5L));
    Assertions.assertEquals("'hello' is a String", c.eval("hello"));
    Assertions.assertEquals("'1.5' is a Number", c.eval(1.5f));
    //Assertions.assertEquals("'127.0.0.1:5000' is an Object", c.eval(Host.localhost(5000)));
    Assertions.assertThrows(IOException.class, ()->c.eval(Host.localhost(5000)));
  }
  
}
