package dbService.dataSets;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "expense_notes")
public class NoteDataSet implements Serializable {

    private static final long serialVersionUID = -8706689714326132798L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "date", nullable = false)
    private java.util.Date date;

    @Column(name = "value", nullable = false)
    private int value;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id", nullable = false)
    private long user_id;

    //TODO: getters and constructs

    protected NoteDataSet() { }

    public NoteDataSet(String name, int value, java.util.Date date, String description, long user_id) {
        this.name = name;
        this.value = value;
        this.date = date;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public long getUser_id() {
        return user_id;
    }
}
