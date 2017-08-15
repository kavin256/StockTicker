package stockTicker;

import java.util.*;
import java.util.logging.*;
import java.sql.Timestamp;

public class Stock extends Thread {
    final Random rand = new Random();
    protected int price = -1;
    protected long time;

    /**
     * Periodically updates stock price and notifies sevlet threads.
     */
    @Override
    public void run() {
        while (!Thread.interrupted())
            try {
                synchronized (this) {
                    price = 90 + rand.nextInt(20);
                    Timestamp ts = new Timestamp(System.currentTimeMillis());
                    time = ts.getTime();
                    notifyAll();
                }
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Logger.getGlobal().log(Level.INFO, "Stock updates terminated!");
                break;
            }
    }
}
