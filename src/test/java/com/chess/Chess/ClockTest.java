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
    Clock c = new Clock(0);
    c.start();

    try {
      Thread.sleep(2000);
    } catch (Exception e) {
      String m = e.getMessage();
      Assertions.assertEquals("time is over", m);
    }
  }

}
