package java1.main.threadsJava;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class MyThread implements Runnable {
    Integer counter = 0;

    public MyThread(int indexThread){
        this.counter = indexThread;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " " + counter);
    }
}

public class Main {

    public static void main(String[] args) {
        List<Runnable> tasks = new ArrayList<>();
        tasks.add(new MyThread(1));
        tasks.add(new MyThread(2));
        MultiExecutor multiExecutor = new MultiExecutor(tasks);
        multiExecutor.executeAll();
    }
}
