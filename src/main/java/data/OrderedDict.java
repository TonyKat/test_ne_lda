package data;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "dictionary_new2")
public class OrderedDict {
    @Id
    @Column(name = "id", columnDefinition = "integer")
    @Type(type = "integer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "word", columnDefinition = "text")
    @Type(type = "string")
    private String word;


    @Column(name = "type_ne", columnDefinition = "integer")
    @Type(type = "integer")
    private int type_ne;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getType_ne() { return type_ne; }

    public void setType_ne(int type_ne) { this.type_ne = type_ne; }
}
