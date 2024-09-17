package service;

import javax.persistence.EntityManager;

import model.Librarian;
import dao.LibrarianDao;
import util.HibernateUtil;

public class LibrarianImp implements LibrarianDao{

	EntityManager em;
	@Override
	public void save(Librarian l) {
		// TODO Auto-generated method stub
		em=HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(l);
			em.getTransaction().commit();	
		}finally {
			em.close();
		}
	}

	@Override
	public void update(Librarian l) {
		// TODO Auto-generated method stub
		em=HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(l);
			em.getTransaction().commit();	
		}finally {
			em.close();
		}
	}

	@Override
	public Librarian get(Long id) {
		// TODO Auto-generated method stub
		em=HibernateUtil.getEntityManagerFactory().createEntityManager();
		Librarian l;
		try {
			l=em.find(Librarian.class, id);
			
		}finally {
			em.close();
		}
		return l;
	}

}
