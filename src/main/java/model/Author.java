package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "nationality")
    private String nationality;

    private String password;
    
    public Author() {}

    public Author(String name, Date birthDate, String nationality, String password) {
        this.name = name;
        this.birthDate = birthDate;
        this.nationality = nationality;
        this.password=password;
    }

    public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
