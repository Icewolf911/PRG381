package View;

import Controller.AuthorController;
import Database.DBconnection;
import Model.AuthorModel;
import Model.PersonModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class AuthorView {
    public JPanel Mainpanel;
    private JTextField txtName;
    private JTextField txtSurname;
    private JTextField txtDOB;
    private JTextField txtEmail;
    private JTextField txtphone;
    private JTable tblAuthors;
    private JButton createButton;
    private JButton deleteButton;
    private JButton ClearButton;
    private JButton editButton;
    private JButton searchButton;
    private JTextField Search_txt;
    private JButton homeButton;
    private DefaultTableModel tableModel;

    public AuthorView() {

        tableModel = new DefaultTableModel(new String[]{"First Name", "Last Name", "Email", "Phone"}, 0);
        tblAuthors.setModel(tableModel);
        DBconnection db = new DBconnection();


        populateAuthorTable(AuthorController.getAuthors());

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = txtName.getText();
                String surname = txtSurname.getText();
                String dob = txtDOB.getText();
                String email = txtEmail.getText();
                String phone = txtphone.getText();

                if (name.isEmpty() || surname.isEmpty() || dob.isEmpty() || email.isEmpty() || phone.isEmpty()){
                    JOptionPane.showConfirmDialog(Mainpanel,
                            "Enter all fields",
                            "Error",
                            JOptionPane.INFORMATION_MESSAGE);
                }else {


                    AuthorController.addAuthor(name, surname, dob, email, phone);
                    JOptionPane.showConfirmDialog(Mainpanel,//weni wat om hier te doen nie -> the 'this' needs to reference an actual component, I just referenced the Mainpanel component in place of 'this'
                            "Added to db",
                            "Confirm",
                            JOptionPane.INFORMATION_MESSAGE);

                    DefaultTableModel model = (DefaultTableModel) tblAuthors.getModel();
                    populateAuthorTable(AuthorController.getAuthors());
                }

                txtName.setText("");
                txtSurname.setText("");
                txtDOB.setText("");
                txtEmail.setText("");
                txtphone.setText("");
            }


        });
        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtName.setText("");
                txtSurname.setText("");
                txtDOB.setText("");
                txtEmail.setText("");
                txtphone.setText("");
            }
        });


        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tblAuthors.getSelectedRow();
                if (selectedRow == -1) {
                    JOptionPane.showMessageDialog(Mainpanel,
                            "Please select a author to delete.");
                    return;
                }

                // Remove the selected row from the table
                DefaultTableModel model = (DefaultTableModel) tblAuthors.getModel();
                int id = (int) tblAuthors.getValueAt(selectedRow, 5);
                AuthorController.deleteAuthor(id);
                populateAuthorTable(AuthorController.getAuthors());
                txtName.setText("");
                txtSurname.setText("");
                txtDOB.setText("");
                txtEmail.setText("");
                txtphone.setText("");
            }
        });
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                    int selectedRow = tblAuthors.getSelectedRow();
                    if (selectedRow != -1) {
                        String name = txtName.getText();
                        String surname = txtSurname.getText();
                        String dob = txtDOB.getText();
                        String email = txtEmail.getText();
                        String phone = txtphone.getText();
                        int id = (int) tblAuthors.getValueAt(selectedRow, 5);
                        AuthorController.editAuthor(id, name, surname, dob, email, phone);
                        populateAuthorTable(AuthorController.getAuthors());
                    }
                txtName.setText("");
                txtSurname.setText("");
                txtDOB.setText("");
                txtEmail.setText("");
                txtphone.setText("");
            }

        });

        tblAuthors.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                int selectedRow = tblAuthors.getSelectedRow();
                txtName.setText(tblAuthors.getValueAt(selectedRow, 0).toString());
                txtSurname.setText(tblAuthors.getValueAt(selectedRow, 1).toString());
                txtDOB.setText(tblAuthors.getValueAt(selectedRow, 2).toString());
                txtEmail.setText(tblAuthors.getValueAt(selectedRow, 3).toString());
                txtphone.setText(tblAuthors.getValueAt(selectedRow, 4).toString());

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ArrayList<AuthorModel> authors = AuthorController.getAuthors();
                ArrayList<AuthorModel> result = new ArrayList<>();
                for (AuthorModel author: authors) {
                    String name = author.getName().toLowerCase();
                    String surname = author.getSurname().toLowerCase();
                    String email = author.getEmail().toLowerCase();
                    String nameSearch = Search_txt.getText().toLowerCase();
                    String surnameSearch = Search_txt.getText().toLowerCase();
                    String emailSearch = Search_txt.getText().toLowerCase();
                    if (name.contains(nameSearch)||surname.contains(surnameSearch)||email.contains(emailSearch)){
                        result.add(author);
                    }
                }
                populateAuthorTable(result);

            }
        });
        homeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


            }
        });
    }


//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> {
//            JFrame frame = new JFrame("Author View");
//            frame.setContentPane(new AuthorView().Mainpanel);
//            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            frame.pack();
//            frame.setVisible(true);
//        });
//    }
    private void populateAuthorTable(ArrayList<AuthorModel> authors) {

        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"First Name", "Last Name","DateofBirth", "Email", "Phone","ID"}, 0);

        // Set the model to the table
        tblAuthors.setModel(model);

        // Populate the table with authors
        for (AuthorModel author : authors) {
            String firstName = author.getName();
            String lastName = author.getSurname();
            String dob = author.getDateOfBirth();
            String email = author.getEmail();
            String phone = author.getPhone();
            int id = author.getId();

            // Add the author's first name and last name as a row in the table
            model.addRow(new Object[]{firstName, lastName, dob, email, phone, id});
        }
    }

}
