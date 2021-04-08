import java.util.Comparator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class MainClass {

    private static final int maxQueueSize = 5;

    public static int getRamdomNo() {
        return ThreadLocalRandom.current().nextInt(1,6);
    }
    public static char getRandomChar(){
        Random r = new Random();
        String alphabet = "ABCDE";
        return alphabet.charAt(r.nextInt(alphabet.length()));
    }

    public static void main(String[] args){
        T element;
        CustomPriorityBlockingQueue<T> prioQueue
                = new CustomPriorityBlockingQueue<T>(
                maxQueueSize, new Comparator<T>() {
            public int compare(T a, T b)
            {
                if (a.getPriority() != b.getPriority()) {
                    return a.getPriority() - b.getPriority();
                }
                else {
                    return a.getTimestamp() - b.getTimestamp() < 0 ? -1 : a.getTimestamp() - b.getTimestamp() > 0 ? 1 : 0;
                }
            }
        });

        for(int i=0;i<5;i++){
            element = new T();
            element.setPriority(getRamdomNo());
            element.setObj(getRandomChar());
            prioQueue.customEnqueue(element);
        }


    }
}
