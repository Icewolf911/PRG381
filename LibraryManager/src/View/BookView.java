package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;

import Controller.AuthorController;
import Controller.BookController;
import Model.*;
import Database.*;


public class BookView extends Component {
    private JPanel Header;
    private JPanel Body;
    private JPanel BD_Panel;
    private JPanel BD_Header;
    private JTextField Title_txt;
    private JTextField Genre_txt;
    private JTextField Author_txt;
    private JTextField Publisher_txt;
    private JTextField PublishDate_txt;
    private JTextField Language_txt;
    private JTextField NumCopies_txt;
    private JTextField AvailableCopies_txt;
    private JTextField BorrowedCopies_txt;
    private JButton Clear_btn;
    private JButton AddBook_btn;
    private JPanel EB_Panel;
    private JPanel EB_Header;
    private JTable ExistingBooks_tbl;
    private JPanel BottomBuffer;
    private JPanel Mainpanel;
    private JButton Update_btn;
    private JButton Delete_btn;
    private JButton Search_btn;
    private JComboBox Author_cmbx;
    private JTextField BookID_txt;
    private JButton btnHome;


    public BookView(){
        //DefaultTableModel tableModel = (DefaultTableModel) ExistingBooks_tbl.getModel();
        DBconnection dbConnection = new DBconnection();
        dbConnection.connect();
        ArrayList<AuthorModel> authors = AuthorController.getAuthors();
        for (PersonModel author : authors) {
//            Author_cmbx.addItem(author.getName() + " " + author.getSurname());
            Author_cmbx.addItem(author.getId()+" "+author.getName() + " " + author.getSurname());
        }
        populateBooksTable();


        AddBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = Title_txt.getText();
                String genre = Genre_txt.getText();

                AuthorModel author =
                        AuthorController.getAuthorById(Integer.parseInt(Author_cmbx.getSelectedItem().toString().split(
                                " ")[0]));
                String publisher = Publisher_txt.getText();
                String publishDate = PublishDate_txt.getText();
                String language = Language_txt.getText();
                int numCopies = Integer.parseInt(NumCopies_txt.getText());
                int availableCopies = Integer.parseInt(AvailableCopies_txt.getText());
                int borrowedCopies = Integer.parseInt(BorrowedCopies_txt.getText());


                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date publishDateAsDate = null;
                try {
                    publishDateAsDate = dateFormat.parse(publishDate);
                    publishDate=publishDateAsDate.toString();
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                if (BookID_txt.getText().isEmpty() || title.isEmpty() || genre.isEmpty() || publisher.isEmpty() || publishDate.isEmpty() || language.isEmpty() || NumCopies_txt.getText().isEmpty() || AvailableCopies_txt.getText().isEmpty() || BorrowedCopies_txt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "One or more fields are invalid.", "Invalid Fields", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbConnection.insertBook(title, genre, publisher, publishDateAsDate, language, numCopies,
                            availableCopies, borrowedCopies, author.getId());
                    System.out.println("Book Added");
                    DefaultTableModel model = (DefaultTableModel) ExistingBooks_tbl.getModel();
                    model.addRow(new Object[] {title, genre, author, publisher, publishDateAsDate, language, numCopies, availableCopies, borrowedCopies});
                    clearInputFields();
                }
                populateBooksTable();

            }

        });

        Clear_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                clearInputFields();
            }
        });

        Search_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = BookID_txt.getText(); //Using book title instead of ID

                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "Please enter a book ID.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int bookId;
                try {
                    bookId = Integer.parseInt(idText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Mainpanel, "Invalid ID format. Please enter a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                BookModel book =  dbConnection.getBookById(bookId);
                try {
                    if (book != null) {
                        String title = book.getTitle();
                        String genre = book.getGenre();
                        String publisher = book.getPublisher();
                        String publicationDate = book.getPublicationDate();
                        String language = book.getLanguage();
                        int numCopies = book.getNumberOfCopies();
                        int availableCopies = book.getNumberOfAvailableCopies();
                        int borrowedCopies = book.getNumberOfBorrowedCopies();
                        int authorId = book.getAuthor().getId();
                        int bookID = Integer.parseInt(BookID_txt.getText());

                        Title_txt.setText(title);
                        Genre_txt.setText(genre);
                        Publisher_txt.setText(publisher);
                        PublishDate_txt.setText(publicationDate);
                        Language_txt.setText(language);
                        NumCopies_txt.setText(String.valueOf(numCopies));
                        AvailableCopies_txt.setText(String.valueOf(availableCopies));
                        BorrowedCopies_txt.setText(String.valueOf(borrowedCopies));

                        DefaultTableModel model = (DefaultTableModel) ExistingBooks_tbl.getModel();
                        model.setRowCount(0);
                        model.addRow(new Object[]{title, genre, publisher, publicationDate, language, numCopies, availableCopies, borrowedCopies, authorId, bookID});

                    } else {
                        JOptionPane.showMessageDialog(Mainpanel, "No book found with the given ID.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Mainpanel, "Error retrieving book details.", "Error", JOptionPane.ERROR_MESSAGE);
                }

                populateBooksTable();
            }
        });

        Update_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = BookID_txt.getText(); //Used title instead of ID

                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "Please enter a book ID to update.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int bookId;
                try {
                    bookId = Integer.parseInt(idText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Mainpanel, "Invalid ID format. Please enter a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                String title = Title_txt.getText();
                String genre = Genre_txt.getText();
                AuthorModel author = AuthorController.getAuthorById(Integer.parseInt(Author_cmbx.getSelectedItem().toString().split(
                        " ")[0]));
                String publisher = Publisher_txt.getText();
                String publicationDate = PublishDate_txt.getText();
                String language = Language_txt.getText();
                int numCopies = Integer.parseInt(NumCopies_txt.getText());
                int availableCopies = Integer.parseInt(AvailableCopies_txt.getText());
                int borrowedCopies = Integer.parseInt(BorrowedCopies_txt.getText());
                int bookID = Integer.parseInt(BookID_txt.getText());

                int authorId = author.getId();

                if (title.isEmpty() || genre.isEmpty() || author==null || publisher.isEmpty() || publicationDate.isEmpty() || language.isEmpty() || NumCopies_txt.getText().isEmpty() || AvailableCopies_txt.getText().isEmpty() || BorrowedCopies_txt.getText().isEmpty() || BookID_txt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "One or more fields are invalid.", "Invalid Fields", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbConnection.updateBook(bookId, title, genre, publisher, publicationDate, language, numCopies, availableCopies, borrowedCopies, authorId);
                    JOptionPane.showMessageDialog(Mainpanel, "Book updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearInputFields();
                }
                populateBooksTable();
            }
        });

        Delete_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = BookID_txt.getText(); //Used Title instead of ID

                if (idText.isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "Please enter a book ID to delete.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int bookId;
                try {
                    bookId = Integer.parseInt(idText);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(Mainpanel, "Invalid ID format. Please enter a numeric ID.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                dbConnection.deleteBook(bookId);
                JOptionPane.showMessageDialog(Mainpanel, "Book deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearInputFields();

                populateBooksTable();
            }


        });

        ExistingBooks_tbl.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);
                if (e.getClickCount() == 1) { // single click

                    int selectedRow = ExistingBooks_tbl.getSelectedRow();

                    if (selectedRow != -1) {
                        Title_txt.setText((String) ExistingBooks_tbl.getValueAt(selectedRow, 0));
                        Genre_txt.setText((String) ExistingBooks_tbl.getValueAt(selectedRow, 1));
                        Publisher_txt.setText((String) ExistingBooks_tbl.getValueAt(selectedRow, 3));
                        PublishDate_txt.setText((String) ExistingBooks_tbl.getValueAt(selectedRow, 4));
                        Language_txt.setText((String) ExistingBooks_tbl.getValueAt(selectedRow, 5));
                        NumCopies_txt.setText( ExistingBooks_tbl.getValueAt(selectedRow, 6).toString());
                        AvailableCopies_txt.setText(ExistingBooks_tbl.getValueAt(selectedRow, 7).toString());
                        BorrowedCopies_txt.setText(ExistingBooks_tbl.getValueAt(selectedRow, 8).toString());
                        BookID_txt.setText(ExistingBooks_tbl.getValueAt(selectedRow, 9).toString());
                        int authorId =
                                dbConnection.getBookById((int) ExistingBooks_tbl.getValueAt(selectedRow, 9)).getAuthor().getId() ;

                        String authorFullNameAndID =
                                authorId +" "+ AuthorController.getAuthorById(authorId).getName() + " " +
                                        AuthorController.getAuthorById(authorId).getSurname();
                        System.out.println(authorFullNameAndID);
                        for (int i = 0; i < Author_cmbx.getItemCount(); i++) {
                            if (Author_cmbx.getItemAt(i).equals(authorFullNameAndID)) {
                                Author_cmbx.setSelectedIndex(i);
                                break;
                            }
                        }
                    }


                }

            }
        });
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    private void clearInputFields() {
        Title_txt.setText("");
        Genre_txt.setText("");
        Author_cmbx.setSelectedItem(null);
        Publisher_txt.setText("");
        PublishDate_txt.setText("");
        Language_txt.setText("");
        NumCopies_txt.setText("");
        AvailableCopies_txt.setText("");
        BorrowedCopies_txt.setText("");
        BookID_txt.setText("");
        Title_txt.requestFocus();
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Book View");
//            frame.setContentPane(new BookView().Mainpanel);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }

    private void populateBooksTable() {
        ArrayList<BookModel> books = BookController.getBooks();
        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title","Genre", "Author","Publisher", "Publication Date",
                "Language","Number of Copies","Available Copies","Borrowed Copies","Book ID"}, 0);

        // Set the model to the table
        ExistingBooks_tbl.setModel(model);

        // Populate the table with books
        for (BookModel book : books) {
            String title = book.getTitle();
            String genre = book.getGenre();

            String publicationDate = book.getPublicationDate();
            String language = book.getLanguage();
            int numCopies = book.getNumberOfCopies();
            int availableCopies = book.getNumberOfAvailableCopies();
            int borrowedCopies = book.getNumberOfBorrowedCopies();
            int bookID = book.getId();
            String publisher = book.getPublisher();
            String author = book.getAuthor().getId()+" "+book.getAuthor().getName() + " " + book.getAuthor().getSurname();

            // Add the book's first name and last name as a row in the table
            model.addRow(new Object[]{title, genre, author,publisher, publicationDate, language, numCopies, availableCopies, borrowedCopies, bookID});
        }
    }

}
