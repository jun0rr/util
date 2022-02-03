/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.Unchecked;
import java.io.IOException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Juno Roesler - juno.rr@gmail.com
 */
public class TestUnchecked {
  
  public void ioex() throws IOException {
    throw new IOException("Something wrong");
  }
  
  public void unchecked() {
    Unchecked.call(()->ioex());
  }
  
  @Test
  public void call() {
    Assertions.assertThrows(IOException.class, ()->ioex());
    Assertions.assertThrows(IOException.class, ()->unchecked());
  }
  
}
