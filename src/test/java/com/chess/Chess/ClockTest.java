package com.chess.Chess;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ClockTest {

  @Test
  public void timeCheck() {
    try {
      Clock c = new Clock(30);
      c.start();
      Thread.sleep(2000);
      c.stop();
      Assertions.assertEquals(29, c.getTimeLeft());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void stopCheck() {
    try {
      Clock c = new Clock(0);
      c.start();
      Thread.sleep(2000);
      Assertions.assertEquals(0, c.getTimeLeft());
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
