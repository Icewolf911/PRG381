package View;

import Controller.BookController;
import Database.DBconnection;
import Model.AuthorModel;
import Model.BookModel;
import Model.PersonModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import Controller.AuthorController;
import app.MainClass;
import org.apache.derby.impl.tools.ij.Main;

public class MainMenuView extends JDialog {
    public JPanel contentPane;
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
        db.clearDatabase();
        db.populateTablesWithDummyData();
        db.populateTablesWithDummyData();
        populateAuthorTable();
        populateBooksTable();


        addAuthor_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
        addBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        borrowBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }
        });
    }

    public void goToAuthorView() {
        JFrame mainFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        mainFrame.getContentPane().removeAll();
        mainFrame.getContentPane().add(new AuthorView().Mainpanel);
        mainFrame.revalidate();
        mainFrame.repaint();
    }

    private void populateAuthorTable() {
        ArrayList<AuthorModel> authors = AuthorController.getAuthors();
        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"ID","First Name", "Last Name"}, 0);

        // Set the model to the table
        author_tbl.setModel(model);

        // Populate the table with authors
        for (AuthorModel author : authors) {
            String firstName = author.getName();
            String lastName = author.getSurname();
            int id = author.getId();

            // Add the author's first name and last name as a row in the table
            model.addRow(new Object[]{id,firstName, lastName});
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

//    public static void main(String[] args) {
//        MainMenuView dialog = new MainMenuView();
//        dialog.setPreferredSize(new Dimension(800,400));
//        dialog.pack();
//        dialog.setVisible(true);
//        System.exit(0);
//    }


}
