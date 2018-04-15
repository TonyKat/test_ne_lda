package generative_process;

public class ItemTopic {
    private int topicID;
    private double probability;

    public ItemTopic(int topicID, double probability) {
        this.topicID = topicID;
        this.probability = probability;
    }

    public ItemTopic() {
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }
}
