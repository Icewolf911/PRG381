package Controller;
import Model.BookModel;

public class BookController {
    private BookModel bookModel;

    public BookController(BookModel bookModel) {
        this.bookModel = bookModel;
        //database add book
    }

    public BookModel getBookModel() {
        return bookModel;
    }

    public void setBookModel(BookModel bookModel) {
        this.bookModel = bookModel;
    }
}
