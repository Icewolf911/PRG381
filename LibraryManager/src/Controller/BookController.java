package Controller;

import Database.DBconnection;
import Model.AuthorModel;
import Model.BookModel;
import Model.PersonModel;

import java.util.ArrayList;

public class BookController {
    private BookModel bookModel;
    static DBconnection db = new DBconnection();

    public BookController(BookModel bookModel) {
        this.bookModel = bookModel;
        //database add book
    }

    public static ArrayList<BookModel> getBooks() {

        db.connect();
        db.createTables();
        ArrayList<BookModel> books = db.getBooks();

        return books;

    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }


}
