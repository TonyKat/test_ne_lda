package data;

import org.hibernate.annotations.Type;
import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "result_table")
public class ResultTable implements Serializable {
    private static final long serialVersionUID = -870668971426132798L;

    @Id
    @Column(name = "id")
    //@Type(type = "integer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "\"sampleID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int sampleID;


    @Column(name = "\"docID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int docID;

    @Column(name = "\"wordID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int wordID;

    @Column(name = "\"topicID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int topicID;

    @Column(name = "type_ne", columnDefinition = "integer")
    @Type(type = "integer")
    private int type_ne;

    @Column(name = "is_true", columnDefinition = "integer")
    @Type(type = "integer")
    private int is_true;

    public ResultTable(int sampleID, int docID, int wordID, int topicID, int type_ne, int is_true) {
        this.sampleID = sampleID;
        this.docID = docID;
        this.wordID = wordID;
        this.topicID = topicID;
        this.type_ne = type_ne;
        this.is_true = is_true;
    }

    public ResultTable() {}

    public int getDocID() {
        return docID;
    }

    public void setDocID(int docID) {
        this.docID = docID;
    }

    public int getWordID() {
        return wordID;
    }

    public void setWordID(int wordID) {
        this.wordID = wordID;
    }

    public int getType_ne() {
        return type_ne;
    }

    public void setType_ne(int type_ne) {
        this.type_ne = type_ne;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }

    public int getIs_true() {
        return is_true;
    }

    public void setIs_true(int is_true) {
        this.is_true = is_true;
    }

    public int getSampleID() {
        return sampleID;
    }

    public void setSampleID(int sampleID) {
        this.sampleID = sampleID;
    }
}
