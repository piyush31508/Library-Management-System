package com.LMS;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import model.Author;
import model.Book;
import model.Librarian;
import model.Loan;
import model.Member;
import service.AuthorImp;
import service.BookImp;
import service.LibrarianImp;
import service.LoanImp;
import service.MemberImp;

public class App {

	private Scanner sc = new Scanner(System.in);
	private MemberImp mI = new MemberImp();
	private LoanImp lI = new LoanImp();
	private AuthorImp aI = new AuthorImp();
	private BookImp bI = new BookImp();
	private LibrarianImp lI2 = new LibrarianImp();

	public void mainMenu() {
		while (true) {
			System.out.println("Hello User, welcome to the Library Management System");
			System.out.println("Enter 1 for Author Activities\n" + "Enter 2 for Member Activities\n"
					+ "Enter 3 for Librarian Activities\n" + "Enter 4 to exit the System");
			int choice = sc.nextInt();
			sc.nextLine(); // Consume newline
			switch (choice) {
			case 1:
				authorOperation();
				break;
			case 2:
				memberOperation();
				break;
			case 3:
				librarianOperation();
				break;
			case 4:
				System.out.println("Exiting Library Management System.......");
				System.exit(0);
				break;
			default:
				System.out.println("Wrong Choice");
			}
		}
	}

	public void librarianOperation() {
		while (true) {
			System.out.println("Welcome to the Librarian Function");
			System.out.println("Enter 1 to add new Librarian\n" + "Enter 2 to update librarian details\n"
					+ "Enter 3 to check the borrowing history\n"
					+ "Enter 4 to check whether a member is eligible to pay the overdue fees\n"
					+ "Enter 5 to check which book is borrowed\n" + "Enter 6 to check active loans\n"
					+ "Enter 7 to exit Librarian System");

			int choice = sc.nextInt();
			sc.nextLine(); // Consume newline
			Librarian librarian = null;
			long librarianId, memberId;
			String email;
			switch (choice) {
			case 1:
				// Add new Librarian
				librarian = new Librarian();
				System.out.println("Enter Librarian's name:");
				String name = sc.nextLine();
				librarian.setName(name);

				System.out.println("Create password for the librarian:");
				String password = sc.nextLine();
				librarian.setPassword(password);

				lI2.save(librarian);
				System.out.println("New Librarian added successfully!\nThe id is " + librarian.getId());
				break;

			case 2:
				// Update Librarian details
				System.out.println("Enter Librarian ID to update details:");
				librarianId = sc.nextLong();
				sc.nextLine(); // Consume newline
				librarian = lI2.get(librarianId);

				if (librarian != null) {
					System.out.println("Enter current password:");
					password = sc.nextLine();

					if (password.equals(librarian.getPassword())) {
						boolean detailsUpdated = false;

						System.out.println("Do you want to change name? (Y/N)");
						if (sc.nextLine().equalsIgnoreCase("Y")) {
							System.out.println("Enter new name:");
							name = sc.nextLine();
							librarian.setName(name);
							detailsUpdated = true;
						}

						System.out.println("Do you want to change password? (Y/N)");
						if (sc.nextLine().equalsIgnoreCase("Y")) {
							System.out.println("Enter new password:");
							String newPassword = sc.nextLine();
							librarian.setPassword(newPassword);
							detailsUpdated = true;
						}

						if (detailsUpdated) {
							lI2.update(librarian);
							System.out.println("Librarian details updated successfully!");
						} else {
							System.out.println("No changes were made.");
						}
					} else {
						System.out.println("Incorrect current password!");
					}
				} else {
					System.out.println("Librarian not found!");
				}
				break;

			case 3:
				System.out.println("Enter Member ID to check borrowing history:");
				memberId = sc.nextLong();
				sc.nextLine(); // Consume newline
				List<Loan> history = lI.borrowingHistory(memberId);

				if (history != null && !history.isEmpty()) {
					System.out.println("Borrowing history for Member ID: " + memberId);
					for (Loan entry : history) {
						System.out.println("Book: " + entry.getBook().getTitle() + ", Borrowed on: "
								+ entry.getLoanDate() + ", Returned on: " + entry.getReturnDate());
					}
				} else {
					System.out.println("No borrowing history found for this member.");
				}
				break;

			case 4:
				// Check if member is eligible to pay overdue fees
				System.out.println("Enter Member ID to check overdue fees eligibility:");
				memberId = sc.nextLong();
				sc.nextLine(); // Consume newline
				double overdueFees = lI.dueFee(memberId);

				if (overdueFees > 0) {
					System.out.println("Member is eligible to pay overdue fees of: $" + overdueFees);
				} else {
					System.out.println("Member has no overdue fees.");
				}
				break;

			case 5:
				// Check currently borrowed books
				List<Loan> borrowedBooks = lI.listLoans();

				if (borrowedBooks != null && !borrowedBooks.isEmpty()) {
					System.out.println("Following books are borrowed:");
					for (Loan book : borrowedBooks) {
						System.out.println("Book Title: " + book.getBook().getTitle() + ", Borrowed Date: "
								+ book.getLoanDate() + ", Borrowed by: " + book.getMember().getName());
					}
				} else {
					System.out.println("Currenty no book is borrowed by any member");
				}
				break;

			case 6:
				List<Loan> active = lI.listOverdueLoans();
				if (!active.isEmpty()) {
					for (Loan a : active) {
						System.out.println(a.getMember() + " " + a.getBook().getTitle() + " " + a.getBook().getAuthor()
								+ " " + a.getLoanDate());
					}
				} else {
					System.out.println("No active due from any member");
				}
				break;

			case 7:
				// Exit Librarian System
				System.out.println("Exiting Librarian System...");
				return;

			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		}
	}

	public void authorOperation() {
		while (true) {
			System.out.println("Welcome to Author Function");
			System.out.println("Enter 1 to add new author\n" + "Enter 2 to add new Book\n"
					+ "Enter 3 to update author details\n" + "Enter 4 to view author details\n"
					+ "Enter 5 to view books by an author\n" + "Enter 6 to exit Author Functions");
			int choice = sc.nextInt();
			sc.nextLine(); // Consume newline
			Author a = null;
			if (choice > 0 && choice < 7) {
				a = new Author();
			}
			String name, d, isbn, password;
			Date date;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			long id;
			switch (choice) {
			case 1:
				// Add new Author
				System.out.println("Enter Author's name:");
				name = sc.nextLine();
				a.setName(name);
				System.out.println("Enter Birthday (YYYY-MM-DD):");
				d = sc.nextLine();
				date = null;
				try {
					date = sdf.parse(d);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				a.setBirthDate(date);
				System.out.println("Enter author's nationality:");
				String nation = sc.nextLine();
				a.setNationality(nation);
				System.out.println("Create password:");
				password = sc.nextLine();
				a.setPassword(password);
				aI.save(a);
				System.out.println(
						a.getId() + " this is your author id, remember this, it will be useful for future operations.");
				break;
			case 2:
				// Add new Book
				Book b = new Book();
				System.out.println("Enter Book's title:");
				name = sc.nextLine();
				b.setTitle(name);
				System.out.println("Enter Author's Id:");
				id = sc.nextLong();
				sc.nextLine(); // Consume newline
				a = aI.get(id);
				if (a == null) {
					throw new NoSuchElementException("Author with ID " + id + " does not exist.");
				}
				b.setAuthor(a);
				System.out.println("Enter published date (YYYY-MM-DD):");
				d = sc.nextLine();
				date = null;
				try {
					date = sdf.parse(d);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				b.setPublishedDate(date);
				System.out.println("Enter ISBN:");
				isbn = sc.nextLine();
				if (isbn.length() == 10 || isbn.length() == 13) {
					b.setIsbn(isbn);
				} else {
					throw new IllegalArgumentException("ISBN length should be 10 (old) or 13 (new)");
				}
				bI.save(b);
				break;
			case 3:
				// Update Author details
				System.out.println("Enter Author ID to update details:");
				id = sc.nextLong();
				sc.nextLine(); // Consume newline
				a = aI.get(id);
				if (a != null) {
					System.out.println("Do you want to change name? (Y/N)");
					if (sc.nextLine().equalsIgnoreCase("Y")) {
						System.out.println("Enter new name:");
						name = sc.nextLine();
						a.setName(name);
					}

					System.out.println("Do you want to change birthdate? (Y/N)");
					if (sc.nextLine().equalsIgnoreCase("Y")) {
						System.out.println("Enter new birthdate (YYYY-MM-DD):");
						d = sc.nextLine();
						try {
							date = sdf.parse(d);
							a.setBirthDate(date);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}

					System.out.println("Do you want to change nationality? (Y/N)");
					if (sc.nextLine().equalsIgnoreCase("Y")) {
						System.out.println("Enter new nationality:");
						name = sc.nextLine();
						a.setNationality(name);
					}

					System.out.println("Do you want to change password? (Y/N)");
					if (sc.nextLine().equalsIgnoreCase("Y")) {
						System.out.println("Enter current password:");
						password = sc.nextLine();
						if (password.equals(a.getPassword())) {
							System.out.println("Enter new password:");
							String newPassword = sc.nextLine();
							a.setPassword(newPassword);
						} else {
							System.out.println("Incorrect current password!");
						}
					}

					aI.update(a);
					System.out.println("Author details updated successfully!");
				} else {
					System.out.println("Author not found!");
				}
				break;
			case 4:
				// View Author details
				System.out.println("Enter Author ID to view details:");
				id = sc.nextLong();
				sc.nextLine(); // Consume newline
				a = aI.get(id);
				if (a != null) {
					System.out.println("Author ID: " + a.getId());
					System.out.println("Name: " + a.getName());
					System.out.println("Birthdate: " + a.getBirthDate());
					System.out.println("Nationality: " + a.getNationality());
					System.out.println("Password: " + a.getPassword());
				} else {
					System.out.println("Author not found!");
				}
				break;
			case 5:
				// View Books by Author
				System.out.println("Enter Author name to view books:");
				name = sc.next();
				sc.nextLine(); // Consume newline
				
				List<Book> books = aI.authorWorks(name);
				if (books != null && !books.isEmpty()) {
					System.out.println("Books by "+name);
					for (Book book : books) {
						System.out.println("Title: " + book.getTitle() + ", Published Date: " + book.getPublishedDate()
								+ ", ISBN: " + book.getIsbn());
					}
				} else {
					System.out.println("No books found for this author.");
				}
				break;
			case 6:
				// Exit Author Functions
				System.out.println("Exiting Author Functions...");
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		}
	}

	public void memberOperation() {
		while (true) {
			System.out.println("Welcome to Member Function");
			System.out
					.println("Enter 1 to add new member\n" + "Enter 2 to borrow a book\n" + "Enter 3 to return a book\n"
							+ "Enter 4 to update member details\nEnter 5 to check the list of the authors\nEnter 6 to check the author's work\nEnter 7 to exit Member System");
			int choice = sc.nextInt();
			sc.nextLine(); // Consume newline
			Member member = null;
			long memberId, bookId;
			String email, password;
			switch (choice) {
			case 1:
				// Add new Member
				member = new Member();
				System.out.println("Enter Member's name:");
				String name = sc.nextLine();
				member.setName(name);
				System.out.println("Enter Member's email:");
				email = sc.nextLine();
				member.setEmail(email);
				System.out.println("Create password:");
				password = sc.nextLine();
				member.setPassword(password);
				mI.save(member);
				System.out.println("New Member added successfully! Member ID: " + member.getId());
				break;
			case 2:
				// Borrow a Book
				System.out.println("Enter Member ID:");
				memberId = sc.nextLong();
				sc.nextLine(); // Consume newline
				System.out.println("Enter Book ID to borrow:");
				bookId = sc.nextLong();
				sc.nextLine(); // Consume newline
				mI.bookBorrow(bookId, memberId);
				break;
			case 3:
				// Return a Book
				System.out.println("Enter Member ID:");
				memberId = sc.nextLong();
				sc.nextLine(); // Consume newline
				System.out.println("Enter Book ID to return:");
				bookId = sc.nextLong();
				sc.nextLine(); // Consume newline
				mI.bookReturn(bookId, memberId);
				break;
			case 4:
				// Update Member details
				System.out.println("Enter Member ID to update details:");
				memberId = sc.nextLong();
				sc.nextLine(); // Consume newline
				member = mI.get(memberId);
				if (member != null) {
					System.out.println("Enter current password:");
					password = sc.nextLine();

					if (password.equals(member.getPassword())) {
						boolean detailsUpdated = false;

						System.out.println("Do you want to change name? (Y/N)");
						if (sc.nextLine().equalsIgnoreCase("Y")) {
							System.out.println("Enter new name:");
							name = sc.nextLine();
							member.setName(name);
							detailsUpdated = true;
						}

						System.out.println("Do you want to change email? (Y/N)");
						if (sc.nextLine().equalsIgnoreCase("Y")) {
							System.out.println("Enter new email:");
							email = sc.nextLine();
							member.setEmail(email);
							detailsUpdated = true;
						}

						System.out.println("Do you want to change password? (Y/N)");
						if (sc.nextLine().equalsIgnoreCase("Y")) {
							System.out.println("Enter new password:");
							String newPassword = sc.nextLine();
							member.setPassword(newPassword);
							detailsUpdated = true;
						}

						if (detailsUpdated) {
							mI.update(member);
							System.out.println("Member details updated successfully!");
						} else {
							System.out.println("No changes were made.");
						}
					} else {
						System.out.println("Incorrect current password! No updates made.");
					}
				} else {
					System.out.println("Member not found!");
				}
				break;

			case 5:
				aI.allAuthor();
				break;
			case 6:
				System.out.println("Enter author's name");
				name=sc.nextLine();
				List<Book> l=aI.authorWorks(name);
				if(!l.isEmpty()) {
				System.out.println("Following books are work of "+name);
				for(Book book:l)
					System.out.println(book.getTitle());}
				else
					System.out.println("No book found under this "+name);
				break;
			case 7:
				// Exit Member System
				System.out.println("Exiting Member System...");
				return;
			default:
				System.out.println("Invalid choice. Please try again.");
				break;
			}
		}
	}

	public static void main(String[] args) {
		App app = new App();
		app.mainMenu();
	}
}
