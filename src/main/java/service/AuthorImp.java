package service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.AuthorDao;
import model.Author;
import model.Book;
import util.HibernateUtil;

public class AuthorImp implements AuthorDao {

	EntityManager em;

	@Override
	public void save(Author a) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(a);
			em.getTransaction().commit();
			System.out.println("Author Details inserted sucessfully");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public Author get(long id) {
		// TODO Auto-generated method stub
		Author a = null;
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			a = em.find(Author.class, id);
		} finally {
			em.close();
		}
		return a;
	}

	@Override
	public void update(Author author) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(author);
			em.getTransaction().commit();

			System.out.println("Author updated successfully.");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public List<Book> authorWorks(String name) {
	    List<Book> listb = new ArrayList<>(); // To hold books by the author(s)
	    List<Author> list = authorByName(name); // Get the list of authors matching the name
	    
	    if (list.isEmpty()) {
	        System.out.println("No authors found with the name: " + name);
	        return listb; // Return empty list if no authors found
	    }

	    em = HibernateUtil.getEntityManagerFactory().createEntityManager();
	    try {
	        // Query to fetch books where the author's ID matches the found author IDs
	        Query q = em.createQuery(
	            "SELECT b FROM Book b WHERE b.author.id IN :authorIds", Book.class);

	        // Extract the author IDs from the list of authors
	        List<Long> authorIds = new ArrayList<>();
	        for (Author author : list) {
	            authorIds.add(author.getId());
	        }

	        q.setParameter("authorIds", authorIds); // Set the list of author IDs to the query
	        listb = q.getResultList(); // Fetch the books for the matching authors

	    } finally {
	        em.close();
	    }

	    return listb;
	}

	public void allAuthor() {
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
	    try {
	        // Query to fetch books where the author's name matches the provided name
	        Query q = em.createQuery(
	            "FROM Author", Author.class);
	        List<Author> list = q.getResultList();
	        for(Author auth:list)
	        System.out.println(auth.getId()+" "+auth.getName()+" "+auth.getNationality());
	       
	    } finally {
	        em.close();
	    }

	}
	
	public List<Author> authorByName(String name) {
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		 List<Author> list= new ArrayList<>();
	    try {
	        // Query to fetch books where the author's name matches the provided name
	        Query q = em.createQuery(
	            "FROM Author where name like :x", Author.class);
	        q.setParameter("x", "%"+name.toLowerCase()+"%");
	        list = q.getResultList();
	       
	    } finally {
	        em.close();
	    }
	    return list;
	}
}


/*public List<Book> authorWorks(String name) {
		List<Book> list = new ArrayList<>();
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			Query q = em.createQuery("FROM Book b WHERE b.author.name = :authorName", Book.class);
			q.setParameter("authorName", name);
			list = q.getResultList();
		} finally {
			em.close();
		}

		return list;
	}
There is author id in book table*/
