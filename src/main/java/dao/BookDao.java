package dao;

import java.util.List;

import model.Book;

public interface BookDao {
	
	public void save(Book book);
	public Book get(long id);
	public List<Book> searchByTitle(String title);
	
}
