package model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book") // Optional: specifying table name
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name = "author_id", nullable = false) // This is the foreign key column in the "book" table
    private Author author;
    
    private String bookStatus;

    @Column(name = "published_date")
    @Temporal(TemporalType.DATE) 
    private Date publishedDate;

    @Column(unique = true, nullable = false) 
    private String isbn;

    
    public Book() {
        bookStatus="Available";
    }

   
    public Book(String title, Author author, Date publishedDate, String isbn) {
        this.title = title;
        this.author = author;
        this.publishedDate = publishedDate;
        this.isbn = isbn;
        bookStatus="Available";
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    @Override
    public String toString() {
        return "Book [id=" + id + ", title=" + title + ", author=" + author.getName() + 
                ", publishedDate=" + publishedDate + ", isbn=" + isbn + 
                ", status=" + bookStatus + "]";
    }


	public String getBookStatus() {
		return bookStatus;
	}


	public void setBookStatus(String bookStatus) {
		this.bookStatus = bookStatus;
	}
}
