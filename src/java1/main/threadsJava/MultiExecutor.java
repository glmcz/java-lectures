package java1.main.threadsJava;

import java.util.ArrayList;
import java.util.List;

public class MultiExecutor {

    // Add any necessary member variables here
    private final List<Runnable> collection;
    /*
     * @param tasks to executed concurrently
     */
    public MultiExecutor(List<Runnable> tasks) {
        // Complete your code here
        this.collection = tasks;

    }

    public void executeAll() {
        // complete your code here
//        for (Runnable r : this.collection) {
//            r.run();
//        }
        List<Thread> threads = new ArrayList<>(collection.size());

        for (int i =0 ; i < collection.size() ; i++) {
            Thread thread = new Thread(collection.get(i));
            threads.add(thread);
        }
        for (Thread thread : threads) {
            thread.start();
        }
    }
}
