package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import java.sql.*;

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
    private JPanel LeftBuffer;
    private JPanel BottomBuffer;
    private JPanel Mainpanel;
    private JButton Update_btn;
    private JButton Delete_btn;
    private JButton Search_btn;


    public BookView(){
        //DefaultTableModel tableModel = (DefaultTableModel) ExistingBooks_tbl.getModel();
        DBConnection dbConnection = new DBConnection();
        dbConnection.connect();

        AddBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String title = Title_txt.getText();
                String genre = Genre_txt.getText();
                String author = Author_txt.getText();
                String publisher = Publisher_txt.getText();
                String publishDate = PublishDate_txt.getText();
                String language = Language_txt.getText();
                int numCopies = Integer.parseInt(NumCopies_txt.getText());
                int availableCopies = Integer.parseInt(AvailableCopies_txt.getText());
                int borrowedCopies = Integer.parseInt(BorrowedCopies_txt.getText());

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date publishDateAsDate = null;
                try {
                    publishDateAsDate = dateFormat.parse(publishDate);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                if (title.isEmpty() || genre.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty() || language.isEmpty() || NumCopies_txt.getText().isEmpty() || AvailableCopies_txt.getText().isEmpty() || BorrowedCopies_txt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "One or more fields are invalid.", "Invalid Fields", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbConnection.insertBook(title, genre, publisher, publishDateAsDate, language, numCopies, availableCopies, borrowedCopies, getAuthorId(author));

                    DefaultTableModel model = (DefaultTableModel) ExistingBooks_tbl.getModel();
                    model.addRow(new Object[] {title, genre, author, publisher, publishDateAsDate, language, numCopies, availableCopies, borrowedCopies});
                    Title_txt.setText("");
                    Genre_txt.setText("");
                    Author_txt.setText("");
                    Publisher_txt.setText("");
                    PublishDate_txt.setText("");
                    Language_txt.setText("");
                    AvailableCopies_txt.setText("");
                    BorrowedCopies_txt.setText("");
                    Title_txt.requestFocus();
                }

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
                String idText = Title_txt.getText(); //Using book title instead of ID

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

                ResultSet resultSet = dbConnection.getBookById(bookId);
                try {
                    if (resultSet != null && resultSet.next()) {
                        String title = resultSet.getString("title");
                        String genre = resultSet.getString("genre");
                        String publisher = resultSet.getString("publisher");
                        String publicationDate = resultSet.getString("publicationDate");
                        String language = resultSet.getString("language");
                        int numCopies = resultSet.getInt("numberOfCopies");
                        int availableCopies = resultSet.getInt("numberOfAvailableCopies");
                        int borrowedCopies = resultSet.getInt("numberOfBorrowedCopies");
                        int authorId = resultSet.getInt("author_id");

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
                        model.addRow(new Object[]{title, genre, publisher, publicationDate, language, numCopies, availableCopies, borrowedCopies});

                    } else {
                        JOptionPane.showMessageDialog(Mainpanel, "No book found with the given ID.", "No Results", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(Mainpanel, "Error retrieving book details.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        Update_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = Title_txt.getText(); //Used title instead of ID

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
                String author = Author_txt.getText();
                String publisher = Publisher_txt.getText();
                String publicationDate = PublishDate_txt.getText();
                String language = Language_txt.getText();
                int numCopies = Integer.parseInt(NumCopies_txt.getText());
                int availableCopies = Integer.parseInt(AvailableCopies_txt.getText());
                int borrowedCopies = Integer.parseInt(BorrowedCopies_txt.getText());
                int authorId = getAuthorId(author);

                if (title.isEmpty() || genre.isEmpty() || author.isEmpty() || publisher.isEmpty() || publicationDate.isEmpty() || language.isEmpty() || NumCopies_txt.getText().isEmpty() || AvailableCopies_txt.getText().isEmpty() || BorrowedCopies_txt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "One or more fields are invalid.", "Invalid Fields", JOptionPane.ERROR_MESSAGE);
                } else {
                    dbConnection.updateBook(bookId, title, genre, publisher, publicationDate, language, numCopies, availableCopies, borrowedCopies, authorId);
                    JOptionPane.showMessageDialog(Mainpanel, "Book updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearInputFields();
                }
            }
        });

        Delete_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String idText = Title_txt.getText(); //Used Title instead of ID

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
            }
        });

    }

    private int getAuthorId(String authorName) {
        // Logic to fetch author ID based on authorName from the database
        // This is to be used for CRUD operations
        return 1;
    }

    private void clearInputFields() {
        Title_txt.setText("");
        Genre_txt.setText("");
        Author_txt.setText("");
        Publisher_txt.setText("");
        PublishDate_txt.setText("");
        Language_txt.setText("");
        NumCopies_txt.setText("");
        AvailableCopies_txt.setText("");
        BorrowedCopies_txt.setText("");
        Title_txt.requestFocus();
    }


}
