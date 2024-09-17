package dao;

import model.Librarian;

public interface LibrarianDao {
	public void save(Librarian l);
	public void update(Librarian l);
	public Librarian get(Long id);
}
