package dao;

import java.util.List;

import model.Loan;

public interface LoanDao {
	public List<Loan> listLoans();
    public List<Loan> listOverdueLoans();
	public double dueFee(long id);
	public List<Loan> borrowingHistory(long id);
}