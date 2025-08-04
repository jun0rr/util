/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Condition;
import com.jun0rr.util.Host;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

/**
 *
 * @author F6036477
 */
public class TestCondition {
  
  @Test
  public void test_integer() {
    Condition<Integer> c = Condition.<Integer>of(i->i < 0)
        .thenThrows(i->{
          IOException ex = new IOException(String.format("%d is lesser then 0", i));
          System.out.println(ex);
          return ex;
        })
        .elseIf(i->i <= 5)
        .then(i->System.out.printf("%d is lesser then 5%n", i))
        .elseIf(i->i <= 10)
        .then(i->System.out.printf("%d is lesser then 10%n", i))
        .elseIf(i->i <= 15)
        .then(i->System.out.printf("%d is lesser then 15%n", i))
        .elseIf(i->i <= 20)
        .then(i->System.out.printf("%d is lesser then 20%n", i))
        .elseIf(i->i > 20)
        .elseThrows(i->{
          IOException ex = new IOException(String.format("%d is greater then 20", i));
          System.out.println(ex);
          return ex;
        })
        ;
    c.eval(7);
    c.eval(3);
    //c.eval(-1);
    c.eval(11);
    c.eval(19);
    //c.eval(22);
    Assertions.assertThrows(IOException.class, ()->c.eval(-1));
    Assertions.assertThrows(IOException.class, ()->c.eval(22));
  }
  
  @Test
  public void test_instanceof() {
    Condition<Object> c = Condition.of(Integer.class)
        .then(o->System.out.printf("'%s' is an Integer%n", o))
        .instanceOf(Number.class)
        .then(o->System.out.printf("'%s' is a Number%n", o))
        .instanceOf(Long.class)
        .then(o->System.out.printf("'%s' is a Long%n", o))
        .instanceOf(Boolean.class)
        .then(o->System.out.printf("'%s' is a Boolean%n", o))
        .instanceOf(String.class)
        .then(o->System.out.printf("'%s' is a String%n", o))
        .otherwise(o->System.out.printf("'%s' is an Object%n", o))
        //.map(o->(Object)o)
        //.elseThrows(o->{
          //IOException ex = new IOException(String.format("'%s' is an object%n", o));
          //System.out.println(ex);
          //return ex;
        //})
        ;
    c.eval("Hello");
    c.eval(false);
    c.eval(1f);
    c.eval(1);
    c.eval(Host.localhost(5555));
    //Assertions.assertThrows(IOException.class, ()->c.eval(Host.localhost(5555)));
  }
  
}
