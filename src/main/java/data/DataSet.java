package data;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "dataset_new")
@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        ),
        @TypeDef(
                name = "int-array",
                typeClass = IntArrayType.class
        )
})
public class DataSet implements Serializable {
    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @Type(type = "integer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "\"docID\"", columnDefinition = "integer")
    @Type(type = "integer")
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    private int docID;

    @Column(name = "\"wordID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int wordID;

    @Column(name = "tf", columnDefinition = "integer")
    @Type(type = "integer")
    private int tf;


    /*@Column(name = "\"topicID\"", columnDefinition = "integer")
    @Type(type = "integer")
    private int topicID;*/

    @Column(name = "type_ne", columnDefinition = "integer")
    @Type(type = "integer")
    private int type_ne;

    public DataSet() {}

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

    public int getTf() {
        return tf;
    }

    public void setTf(int tf) {
        this.tf = tf;
    }

    /*public int getTopicID() {
        return topicID;
    }

    public void setTopicID(int topicID) {
        this.topicID = topicID;
    }*/

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
}