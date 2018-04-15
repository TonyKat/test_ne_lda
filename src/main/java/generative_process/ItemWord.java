package generative_process;

public class ItemWord {
    public long docID;
    public long wordID;
    public long topicID;
    public ItemNe _ne;

    public ItemWord(long docID, long wordID, long topicID, ItemNe _ne){
        this.docID = docID;
        this.wordID = wordID;
        this.topicID = topicID;
        this._ne = _ne;
    }

    public ItemWord(){}
}
