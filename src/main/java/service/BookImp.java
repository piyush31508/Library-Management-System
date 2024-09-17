package service;

import java.util.ArrayList;
import java.util.List;

import dao.BookDao;
import model.Book;
import util.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.Query;

public class BookImp implements BookDao {

	EntityManager em;

	@Override
	public void save(Book book) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			 System.out.println("Saving book: " + book);
			em.getTransaction().begin();
			em.persist(book);
			em.getTransaction().commit();
			System.out.println("Book inserted sucessfully");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public Book get(long id) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		Book b = null;
		try {
			b = em.find(Book.class, id);
		} finally {
			em.close();
		}
		return b;
	}

	@Override
	public List<Book> searchByTitle(String title) {
		// TODO Auto-generated method stub
		List<Book> list = new ArrayList<>();
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("from Book as b where b.title like :x",Book.class);
			q.setParameter("x", "%"+title+"%");
			list = q.getResultList();
		}
		finally {
			em.close();
		}
		return list;
	}

}
