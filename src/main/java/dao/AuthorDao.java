package dao;

import java.util.List;

import model.Author;
import model.Book;

public interface AuthorDao {
	
	public void save(Author a);
	public Author get(long id);
	public void update(Author author);	
	public List<Book> authorWorks(String name);
}
