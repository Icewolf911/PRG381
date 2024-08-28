package Model;

import java.util.ArrayList;
import java.util.List;

public class AuthorModel extends PersonModel{
    ArrayList<BookModel> writtenBooks;
    public AuthorModel(String name, String surname, String dateOfBirth, String email, String phone) {
        super(name, surname, dateOfBirth, email, phone);
    }

    public AuthorModel() {
        super();
    }
    public AuthorModel(PersonModel person) {
        super(person.getName(), person.getSurname(), person.getDateOfBirth(), person.getEmail(), person.getPhone());

    }

    public ArrayList<BookModel> getWrittenBooks() {
        return writtenBooks;
    }

    public void setWrittenBooks(ArrayList<BookModel> writtenBooks) {
        this.writtenBooks = writtenBooks;
    }
}
