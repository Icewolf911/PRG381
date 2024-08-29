package Model;

import java.util.List;

public class BorrowerModel extends PersonModel{
    private List<BookModel> borrowedBooks;

    public BorrowerModel(String name, String surname, String dateOfBirth, String email, String phone) {
        super(name, surname, dateOfBirth, email, phone);
    }

    public BorrowerModel(PersonModel person) {
        super(person.getName(), person.getSurname(), person.getDateOfBirth(), person.getEmail(), person.getPhone());
    }

    public List<BookModel> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<BookModel> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }
}
