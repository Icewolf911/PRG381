package View;

import Controller.BookController;
import Model.BookModel;
import Model.BorrowerModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import Controller.BorrowerController;

public class BorrowerView extends JFrame {
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTable tableBorrowers;
    private JPanel MainPannel;
    private JTextField txtDOB;
    private JButton addBook_btn;
    private JButton removeBook_btn;
    private JComboBox comboBox1;
    private JTable Bookstbl;
    private JTextField txtCheckOutDate;
    private JTextField txtCheckInDate;
    private JLabel checkOutDate;
    private JButton btnHome;
    private DefaultTableModel tableModel;

    public BorrowerView() {

        // Initialize the table model and set it for the table
        tableModel = new DefaultTableModel(new String[]{"First Name", "Last Name","DOB", "Email", "Phone","id"}, 0);
        tableBorrowers.setModel(tableModel);
        populateBorrowersTable();
        populateComboBox();

        // Action listeners for buttons
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBorrower();
                populateBorrowersTable();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBorrower();
                populateBorrowersTable();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBorrower();
                populateBorrowersTable();
            }
        });
        tableBorrowers.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (e.getClickCount() == 1) { // single click

                    int selectedRow = tableBorrowers.getSelectedRow();

                    if (selectedRow != -1) {
                        txtFirstName.setText((String) tableBorrowers.getValueAt(selectedRow, 0));
                        txtLastName.setText((String) tableBorrowers.getValueAt(selectedRow, 1));
                        txtDOB.setText((String) tableBorrowers.getValueAt(selectedRow, 2));
                        txtEmail.setText((String) tableBorrowers.getValueAt(selectedRow, 3));
                        txtPhone.setText((String) tableBorrowers.getValueAt(selectedRow, 4));
                    }

                    int id = (int) tableBorrowers.getValueAt(selectedRow, 5);

                    populateBooksTable(BorrowerController.getBorrowedBooks(id));

                }
            }
        });
        addBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBorrowers.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a borrower to add a book to.");
                    return;
                }

                int borrowerId = (int) tableBorrowers.getValueAt(selectedRow, 5);
                int bookId = Integer.parseInt(comboBox1.getSelectedItem().toString().split(" ")[0]);

                BorrowerController.addBookToBorrower(borrowerId, bookId, txtCheckOutDate.getText(), txtCheckInDate.getText());
                populateBooksTable(BorrowerController.getBorrowedBooks(borrowerId));
            }
        });
        removeBook_btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tableBorrowers.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(null, "Please select a borrower to add a book to.");
                    return;
                }

                int borrowerId = (int) tableBorrowers.getValueAt(selectedRow, 5);
                int bookId = Integer.parseInt(comboBox1.getSelectedItem().toString().split(" ")[0]);

                BorrowerController.removeBookToBorrower(borrowerId, bookId);
                populateBooksTable(BorrowerController.getBorrowedBooks(borrowerId));
            }
        });
        btnHome.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
    }


    private void addBorrower() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();
        String dob = txtDOB.getText();

        // Add data to the table (just for display purposes)
        BorrowerController.addBorrower(firstName, lastName,dob, email, phone);

        // Clear the input fields after adding
        clearFields();
    }

    private void updateBorrower() {
        int selectedRow = tableBorrowers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to update.");
            return;
        }

        // Update the selected row with new data
        BorrowerController.updateBorrower((int) tableBorrowers.getValueAt(selectedRow, 5), txtFirstName.getText()
                , txtLastName.getText(),txtDOB.getText(), txtEmail.getText(), txtPhone.getText());

        clearFields();
    }

    private void deleteBorrower() {
        int selectedRow = tableBorrowers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to delete.");
            return;
        }

        // Remove the selected row from the table
        BorrowerController.deleteBorrower((int) tableBorrowers.getValueAt(selectedRow, 5));
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Borrower View");
//            frame.setContentPane(new BorrowerView().MainPannel);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.pack();
//            frame.setVisible(true);
//
//        });
//    }
//

    private void populateBooksTable(ArrayList<BookModel> books){
        DefaultTableModel model = new DefaultTableModel(new String[]{"Title","Genre", "Author","Publisher", "Publication Date",
                "Language","Number of Copies","Available Copies","Borrowed Copies","Book ID"}, 0);

        // Set the model to the table
        Bookstbl.setModel(model);

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

    private void populateBorrowersTable() {
        // Get the list of borrowers from the controller
        ArrayList<BorrowerModel> borrowers = BorrowerController.getBorrowers();

        // Clear the table
        tableModel.setRowCount(0);

        // Populate the table with borrowers
        for (BorrowerModel borrower : borrowers) {
            String firstName = borrower.getName();
            String lastName = borrower.getSurname();
            String email = borrower.getEmail();
            String phone = borrower.getPhone();
            int id = borrower.getId();
            String dob = borrower.getDateOfBirth();

            // Add the borrower's details as a row in the table
            tableModel.addRow(new Object[]{firstName, lastName,dob, email, phone,id});
        }
    }

    private void populateComboBox(){
        ArrayList<BookModel> books = BookController.getBooks();
        for (BookModel book : books) {
            if (book.getNumberOfAvailableCopies() > 0) {
                comboBox1.addItem(book.getId() + " " + book.getTitle());
            }
        }

    }
}
