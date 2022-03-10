/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Condition;
import com.jun0rr.util.Host;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestCondition {
  
  @Test
  public void test_integer() {
    Condition<Integer> c = Condition.<Integer>of(i->i <= 5)
        .then(i->System.out.printf("%d is lesser then 5%n", i))
        .elseIf(i->i <= 10)
        .then(i->System.out.printf("%d is lesser then 10%n", i))
        .elseIf(i->i <= 15)
        .then(i->System.out.printf("%d is lesser then 15%n", i))
        .elseIf(i->i <= 20)
        .then(i->System.out.printf("%d is lesser then 20%n", i))
        .elseAccept(i->System.out.printf("%d is greater then 20%n", i));
    c.clone().eval(7);
    c.clone().eval(3);
    c.clone().eval(11);
    c.clone().eval(19);
    c.clone().eval(22);
  }
  
  @Test
  public void test_instanceof() {
    Condition c = Condition.of(Integer.class)
        .then(o->System.out.printf("'%s' is an Integer%n", o))
        .instanceOf(Number.class)
        .then(o->System.out.printf("'%s' is a Number%n", o))
        .instanceOf(Long.class)
        .then(o->System.out.printf("'%s' is a Long%n", o))
        .instanceOf(Boolean.class)
        .then(o->System.out.printf("'%s' is a Boolean%n", o))
        .instanceOf(String.class)
        .then(o->System.out.printf("'%s' is a String%n", o))
        .other(o->System.out.printf("'%s' is an Object%n", o));
    c.clone().eval("Hello");
    c.clone().eval(false);
    c.clone().eval(Host.localhost(5555));
    c.clone().eval(1f);
    c.clone().eval(1);
  }
  
}
