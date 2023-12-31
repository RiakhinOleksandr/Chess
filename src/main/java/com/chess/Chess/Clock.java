package com.chess.Chess;

import java.awt.event.*;
import javax.swing.*;

public class Clock {

  private int elapsedTime = 0;
  private int timeLeft = 0;
  private int wholeTime = 0;

  Timer timer = new Timer(1000, new ActionListener() {

    public void actionPerformed(ActionEvent e) {
      if (timeLeft != 0) {
        elapsedTime += 1000;
        timeLeft = wholeTime - (elapsedTime / 1000);
      } else {
        timer.stop();
      }
    }
  });

  public Clock(int time) {
    this.timeLeft = time;
    this.wholeTime = time;
  }

  public void start() {
    timer.start();
  }

  public void stop() {
    timer.stop();
  }

  public int getTimeLeft() {
    return this.timeLeft;
  }
}
