import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomPriorityBlockingQueue<E> extends PriorityBlockingQueue<E> {

    private final ReentrantLock lock;
    private final Condition notFull;
    private final int maxQueueSize; // this is the max capacity of the queue, used to make the PriorityBlockingQueue a bounded one
    private transient Comparator<? super T> comparator;
    private int throttleRate = 2; // change this value for any throttle rate
    private int nextPriority = 0; // determining what would be the priority of the next item if two items having the same priority have been removed
    int currentPriority=0;
    Map<Integer, Integer> hmap = new HashMap<Integer, Integer>();

    public CustomPriorityBlockingQueue(int fixedSize,
                                 Comparator<? super T> comparator) {
        if (fixedSize < 1)
            throw new IllegalArgumentException();
        else this.maxQueueSize = fixedSize;
        this.comparator = comparator;
        this.lock = new ReentrantLock();
        this.notFull = lock.newCondition();
    }

    private void resequence(){
        Date date = new Date();
        long time = date.getTime();  //getTime() returns current time in milliseconds
        try{
            Iterator<T> iterator = (Iterator<T>) super.iterator();

            while (iterator.hasNext()) {
                if(time - iterator.next().getTimestamp() > 10000){
                    iterator.next().setPriority(1);
                }
            }
        }
        catch (Exception e){
            System.out.println(e);
        }

    }

    public boolean offer(E e) {
        if (e == null)
            throw new NullPointerException();
        this.lock.lock();
        try {
            while (this.size() == this.maxQueueSize)
                notFull.await();
            boolean success = super.offer(e);
            return success;
        } catch (InterruptedException ie) {
            notFull.signal(); // propagate to a non-interrupted thread
            return false;
        } finally {
            resequence();
            this.lock.unlock();
        }
    }

    public boolean customEnqueue(E element){
        return this.offer(element);
    }

    public T customDequeue() throws InterruptedException {
        T result;
        // priority of the item currently being dequeued
        if(nextPriority != 0){ // if the flag nextPriority is 0 that would mean we're not yet at a point where we need to remove the element of one higher priority
            currentPriority = nextPriority;
             T obj = new T();
             obj.setPriority(nextPriority);// setup this object with nextPriority
            super.remove(obj); // removing an object having priority one higher than the last dequeued object's priority
            nextPriority = 0;
            result = obj;
        }
        else{
            T t = (T)this.take();
            currentPriority = t.getPriority();
            result = t;
        }

        notFull.signal();

        if(hmap.containsKey(currentPriority)){
            hmap.computeIfPresent(currentPriority, (k, v) -> v + 1);
            if(hmap.get(currentPriority) == throttleRate) {
                nextPriority = currentPriority + 1;
                hmap.clear();
            }
        }
        else{
            hmap.put(currentPriority, 1);
        }
        return result;
    }
}
