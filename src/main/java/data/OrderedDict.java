package data;

import org.hibernate.annotations.Type;

import javax.persistence.*;

@Entity
@Table(name = "dictionary_news_rbc")
public class OrderedDict {
    @Id
    @Column(name = "id", columnDefinition = "integer")
    @Type(type = "integer")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Column(name = "word", columnDefinition = "text")
    @Type(type = "string")
    private String word;

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
}
