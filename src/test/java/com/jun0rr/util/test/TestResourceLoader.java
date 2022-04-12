/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.ResourceLoader;
import com.jun0rr.util.crypto.Crypto;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.security.PrivateKey;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestResourceLoader {
  
  @Test
  public void loadPrivateKey() {
    try {
      byte[] bs = ResourceLoader.caller().loadContentBytes("doxy-pk.der");
      PrivateKey pk = Crypto.createPrivateKey(bs);
      System.out.println(pk);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  @Test
  public void resourcePath() throws URISyntaxException {
    System.out.println("--- resourcePath ---");
    Path p = Path.of(ResourceLoader.class.getClassLoader().getResource("doxy-pk.der").toURI());
    System.out.println(p);
    System.out.println(ResourceLoader.self().loadPath("doxy-pk.der"));
  }
  
  
}
