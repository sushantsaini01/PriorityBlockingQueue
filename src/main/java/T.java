public class T implements Comparable<T> {
    private int priority;
    private Object obj;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    @Override
    public int compareTo(T o) {
        if (this.getPriority() != o.getPriority()) {
            return this.getPriority() - o.getPriority();
        }
        else {
            return this.getTimestamp() - o.getTimestamp() < 0 ? -1 : this.getTimestamp() - o.getTimestamp() > 0 ? 1 : 0;
        }
    }
}
