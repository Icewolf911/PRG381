package Controller;
import Database.DBconnection;
import Model.AuthorModel;
import Model.BookModel;
import Model.PersonModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorController {
    private AuthorModel authorModel;
    static DBconnection db = new DBconnection();


    public AuthorController() {
        this.authorModel = authorModel;
        //database add author
        db.connect();

    }

    public static ArrayList<AuthorModel> getAuthors() {
        db.connect();
        ArrayList<AuthorModel> authors = new ArrayList<>();
        ArrayList<PersonModel> people = db.getPersons();
        for (PersonModel person : people) {
            if (person.isAuthor()) {
                AuthorModel author = new AuthorModel(person);
                author.setId(person.getId());
                author.setAuthor(true);
                author.setWrittenBooks(getBooksByAuthorId(person.getId()));
                authors.add(author);
            }
        }
        return authors;
    }

    private static ArrayList<BookModel> getBooksByAuthorId(int id) {
        db.connect();
        ArrayList<BookModel> books = db.getBooks();
        ArrayList<BookModel> authorBooks = new ArrayList<>();
        for (BookModel book : books) {
            if (book.getAuthor().getId() == id) {
                authorBooks.add(book);
            }
        }
        return authorBooks;
    }

    public static void addAuthor(String name, String surname, String dob, String email, String phone) {
        db.connect();
        db.insertPerson(name, surname, dob, email, phone, true, false);
    }

    public static void deleteAuthor(int id) {
        db.connect();
        db.deletePerson(id);
    }

    public static AuthorModel getAuthorModel(int id) {
        return db.getAuthorById(id);

    }

    public static AuthorModel getAuthorById(int i) {
        return db.getAuthorById(i);
    }

    public void setAuthorModel(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }

    public static void editAuthor(int id, String name, String surname, String dob, String email, String phone) {
        db.connect();
        db.updatePerson(id, name, surname, dob, email, phone);
    }



}
