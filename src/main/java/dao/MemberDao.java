package dao;

import model.Member;

public interface MemberDao {
	public void save(Member member);
	public Member get(long id);
	public void bookBorrow(long bookId, long memberId);
	public void bookReturn(long bookId, long memberId);
	public void update(Member member);
}