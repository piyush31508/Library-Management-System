package service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import dao.LoanDao;
import model.Loan;
import util.HibernateUtil;

public class LoanImp implements LoanDao {

	EntityManager em;

	@Override
	public List<Loan> listOverdueLoans() {
		// TODO Auto-generated method stub
		List<Loan> list = new ArrayList<>();
		em = HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, -15);
			Date overdueDate = calendar.getTime();
			
			Query q = em.createQuery("from Loan as l where l.returnDate is null and l.loanDate < :x");
			q.setParameter("x", overdueDate);
			list = q.getResultList();
		} finally { 	
			em.close();
		}
		return list;
	}

	@Override
	public double dueFee(long id) {
		// TODO Auto-generated method stub
		double dailyFine = 10.0; // Example daily fine
		double dueFee = 0.0;

		em = HibernateUtil.getEntityManagerFactory().createEntityManager();

		try {
			Loan loan = em.find(Loan.class, id);
			if (loan != null) {
				Date loanDate = loan.getLoanDate();
				Date returnDate = loan.getReturnDate();
				Date currentDate = new Date();

				Date endDate = (returnDate != null) ? returnDate : currentDate;
				long diffInMillis = endDate.getTime() - loanDate.getTime();
				long daysDiff = diffInMillis / (1000 * 60 * 60 * 24);

				long overdueDays = daysDiff - 15;
				if (overdueDays > 0) {
					dueFee = overdueDays * dailyFine;
				}
			} else {
				return 0;
			}
		} finally {
			em.close();
		}
		return dueFee;
	}

	@Override
	public List<Loan> listLoans() {
		// TODO Auto-generated method stub
		List<Loan> list = new ArrayList<>();
		em=HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("from Loan as l where l.returnDate is null",Loan.class);
//			q.setParameter("x", memberId);
			list = q.getResultList();
		}
		finally {
			em.close();
		}
		return list;
	}

	@Override
	public List<Loan> borrowingHistory(long memberId) {
		// TODO Auto-generated method stub
		List<Loan> list = new ArrayList<>();
		em=HibernateUtil.getEntityManagerFactory().createEntityManager();
		try {
			Query q = em.createQuery("from Loan as l where l.member.id = :x",Loan.class);
			q.setParameter("x", memberId);
			list = q.getResultList();
		}
		finally {
			em.close();
		}
		return list;
	}
}
