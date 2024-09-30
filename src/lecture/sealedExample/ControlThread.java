package lecture.sealedExample;

import java.util.Random;

// TODO once i would do more in Java so far not related to my job
public class ControlThread extends Thread {
    Random rand = new Random();
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + rand.nextInt());
    }
}



