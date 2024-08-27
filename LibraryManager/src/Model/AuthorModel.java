package Model;

import java.util.List;

public class AuthorModel extends PersonModel{
    List<BookModel> writtenBooks;
    public AuthorModel(String name, String surname, String dateOfBirth, String email, String phone) {
        super(name, surname, dateOfBirth, email, phone);
    }

    public AuthorModel() {
        super();
    }

    public List<BookModel> getWrittenBooks() {
        return writtenBooks;
    }

    public void setWrittenBooks(List<BookModel> writtenBooks) {
        this.writtenBooks = writtenBooks;
    }
}
