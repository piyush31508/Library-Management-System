package service;

import java.util.Date;
import java.util.Scanner;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.MemberDao;
import model.Book;
import model.Loan;
import model.Member;
import service.LoanImp;
import util.HibernateUtil;

public class MemberImp implements MemberDao {
	Scanner sc;
	EntityManager em;
	Member m;

	@Override
	public void save(Member member) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.persist(member);
			em.getTransaction().commit();
			System.out.println("Member added Sucessfully");
		} catch (Exception e) {
			if (em.getTransaction().isActive())
				em.getTransaction().rollback();
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public void bookBorrow(long bookId, long memberId) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
	        em.getTransaction().begin();

			Book b = em.find(Book.class, bookId);
			Member m = em.find(Member.class, memberId);

			if (b == null) {
				throw new IllegalArgumentException("Book not found");
			}

			if (m == null) {
				throw new IllegalArgumentException("Member not found");
			}

			System.out.println(b.getBookStatus());
			if ("Borrowed".equals(b.getBookStatus())) {
				throw new IllegalStateException("Book is already borrowed");
			}
			
			Loan l = new Loan();
			l.setBook(b);
			l.setMember(m);
			l.setLoanDate(new Date());
			l.setReturnDate(null);


			b.setBookStatus("Borrowed");
			
			em.persist(l);

			em.merge(b);
			
	        em.getTransaction().commit();

			
			System.out.println("The book is given to "+m.getName());

		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
		        System.err.println("An error occurred while borrowing the book: " + e.getMessage());

			}
		} finally {
			em.close();
		}

	}

	@Override
	public void bookReturn(long bookId, long memberId) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Book b = em.find(Book.class, bookId);
			Member m = em.find(Member.class, memberId);

			if (b == null) {
				throw new IllegalArgumentException("Book not found");
			}

			if (m == null) {
				throw new IllegalArgumentException("Member not found");
			}
			Query loanQuery = em.createQuery(
					"FROM Loan l WHERE l.book.id = :bookId AND l.member.id = :memberId AND l.returnDate IS NULL",
					Loan.class);
			loanQuery.setParameter("bookId", bookId);
			loanQuery.setParameter("memberId", memberId);

			Loan l = (Loan) loanQuery.getSingleResult();

			if (l == null) {
				throw new IllegalArgumentException("No active loan found for this book and member");
			}

			LoanImp loanImp = new LoanImp();
			sc = new Scanner(System.in);
			int n=1;
			double fee = loanImp.dueFee(l.getId());
			if(fee>0) {
			System.out.println("Due Fee: " + fee);
			System.out.println("Pay this amount\nEnter 1 for yes Enter 2 for no");
			n = sc.nextInt();
			}
			if (n == 1) {
				em.getTransaction().begin();
				l.setReturnDate(new Date());
				b.setBookStatus("Available");
				em.merge(l);
				em.merge(b);
				em.getTransaction().commit();
				System.out.println("The book have been returned, id of the book is:"+b.getId()+" by "+l.getMember().getName());
			} else {
				System.out.println("Pay next time");
				return;
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace();
		} finally {
			em.close();
		}
	}

	@Override
	public Member get(long id) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		m = null;
		try {
			m = em.find(Member.class, id);
		} finally {
			em.close();
		}
		return m;
	}

	@Override
	public void update(Member member) {
		// TODO Auto-generated method stub
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			em.getTransaction().begin();
			em.merge(member);
			em.getTransaction().commit();
			System.out.println("Details updated sucesfully");
		} finally {
			em.close();
		}
	}
}
