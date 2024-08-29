package Controller;
import Database.DBconnection;
import Model.AuthorModel;
import Model.BookModel;
import Model.BorrowerModel;
import Model.PersonModel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class BorrowerController {
    private BorrowerModel borrowerModel;
    static DBconnection db = new DBconnection();

    public BorrowerController(BorrowerModel borrowerModel) {
        this.borrowerModel = borrowerModel;
        //database add borrower
    }

    public static ArrayList<BorrowerModel> getBorrowers() {
        db.connect();
        ArrayList<BorrowerModel> borrowers = new ArrayList<>();
        ArrayList<PersonModel> people = db.getPersons();
        for (PersonModel person : people) {
            if (person.isBorrower()) {
                BorrowerModel borrower = new BorrowerModel(person);
                borrower.setId(person.getId());
                borrower.setBorrower(true);
                borrowers.add(borrower);
            }
        }
        return borrowers;
    }

    public static void addBorrower(String firstName, String lastName, String dob, String email, String phone) {
        db.connect();
        db.insertPerson(firstName, lastName,dob, email, phone, false, true);
    }

    public static ArrayList<BookModel> getBorrowedBooks(int borrowerId) {
        db.connect();
        ArrayList<BookModel> books = db.getBorrowedBooksByBorrowerId(borrowerId);
        return books;
    }

    public static void updateBorrower(int id, String firstName, String lastName, String dob, String email, String phone) {
        db.connect();
        db.updatePerson(id, firstName, lastName, dob, email, phone);
    }

    public static void deleteBorrower(int valueAt) {
        db.connect();
        db.deletePerson(valueAt);
    }

    public static void addBookToBorrower(int borrowerId, int bookId, String borrowedDate, String returnDate) {
        db.connect();
        db.insertBorrowedBook(bookId,borrowerId, borrowedDate, returnDate);
    }

    public static void removeBookToBorrower(int borrowerId, int bookId) {
        db.connect();
        db.deleteBorrowedBook(bookId, borrowerId);
    }


    public BorrowerModel getBorrowerModel() {
        return borrowerModel;
    }

    public void setBorrowerModel(BorrowerModel borrowerModel) {
        this.borrowerModel = borrowerModel;
    }
}
