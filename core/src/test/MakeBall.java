package test;

public class MakeBall extends Thread
{
  public void run()
  {
    while (Main.isRunning) {
      Main.giveBirth(1, 1, Math.random() * 100.0,
                     Math.random() * 100.0, 100);
      try 
      {
        sleep(500);
      } 
      catch (InterruptedException e) 
      {
      }
    }
  }
}
