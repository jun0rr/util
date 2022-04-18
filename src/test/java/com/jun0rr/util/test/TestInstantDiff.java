/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.jun0rr.util.test;

import com.jun0rr.util.InstantDiff;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 *
 * @author F6036477
 */
public class TestInstantDiff {
  
  @Test
  public void diff() {
    Instant i1 = LocalDateTime.of(2022, 01, 28, 13, 01, 15).toInstant(ZoneOffset.UTC);
    Instant i2 = LocalDateTime.ofInstant(i1, ZoneOffset.UTC)
        .plusMonths(18)
        .plusHours(60)
        .toInstant(ZoneOffset.UTC);
    InstantDiff df = InstantDiff.of(i1, i2);
    Assertions.assertEquals(1, df.years());
    Assertions.assertEquals(6, df.months());
    Assertions.assertEquals(18, df.toMonths());
    Assertions.assertEquals(2, df.days());
    Assertions.assertEquals(12, df.hours());
    Assertions.assertEquals(0, df.minutes());
  }
  
}
