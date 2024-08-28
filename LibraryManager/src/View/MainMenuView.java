package View;

import Controller.BookController;
import Database.DBconnection;
import Model.BookModel;
import Model.PersonModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import Controller.AuthorController;

public class MainMenuView extends JDialog {
    private JPanel contentPane;
    private JButton addAuthor_btn;
    private JButton addBook_btn;
    private JTable author_tbl;
    private JTable book_tbl;
    private JButton borrowBook_btn;
    private JLabel authors_lbl;
    private JLabel books_lbl;
    private JLabel title_lbl;

    public MainMenuView() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addAuthor_btn);

        DBconnection db = new DBconnection();
        db.connect();
        db.createTables();
        populateAuthorTable();
        populateBooksTable();



    }

    private void populateAuthorTable() {
        ArrayList<PersonModel> authors = AuthorController.getAuthors();
        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"First Name", "Last Name"}, 0);

        // Set the model to the table
        author_tbl.setModel(model);

        // Populate the table with authors
        for (PersonModel author : authors) {
            String firstName = author.getName();
            String lastName = author.getSurname();

            // Add the author's first name and last name as a row in the table
            model.addRow(new Object[]{firstName, lastName});
        }
    }

    private void populateBooksTable() {
        ArrayList<BookModel> books = BookController.getBooks();
        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title", "Author", "Publication Date"}, 0);

        // Set the model to the table
        book_tbl.setModel(model);

        // Populate the table with books
        for (BookModel book : books) {
            String title = book.getTitle();
            String author = book.getAuthor().getName() + " " + book.getAuthor().getSurname();
            String publicationDate = book.getPublicationDate();

            // Add the book's first name and last name as a row in the table
            model.addRow(new Object[]{title, author, publicationDate});
        }
    }

    public static void main(String[] args) {
        MainMenuView dialog = new MainMenuView();
        dialog.setPreferredSize(new Dimension(800,400));
        dialog.pack();

        dialog.setVisible(true);


        System.exit(0);
    }


}
