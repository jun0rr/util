/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Host;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestHost {
  
  @Test
  public void test() {
    Host h = Host.localhost(5050);
    System.out.println(h);
    System.out.println(h.getIPAddress());
    Assertions.assertEquals("127.0.0.1", h.getHostname());
    Assertions.assertEquals("127.0.0.1", h.getIPAddress());
    Assertions.assertEquals(5050, h.getPort());
  }
  
}
