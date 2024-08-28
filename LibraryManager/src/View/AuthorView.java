package View;

import Controller.AuthorController;
import Database.DBconnection;
import Model.AuthorModel;
import Model.PersonModel;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.ArrayList;

public class AuthorView {
    private JPanel Mainpanel;
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
                            JOptionPane.ERROR_MESSAGE);
                }else {


                    AuthorController.addAuthor(name, surname, dob, email, phone);
                    JOptionPane.showConfirmDialog(Mainpanel,//weni wat om hier te doen nie -> the 'this' needs to reference an actual component, I just referenced the Mainpanel component in place of 'this'
                            "Added to db",
                            "Confirm",
                            JOptionPane.ERROR_MESSAGE);

                    DefaultTableModel model = (DefaultTableModel) tblAuthors.getModel();
                    model.addRow(new Object[]{name, surname, dob, email, phone});
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
                model.removeRow(selectedRow);
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

                        tableModel.setValueAt(name, selectedRow, 0);
                        tableModel.setValueAt(surname, selectedRow, 1);
                        tableModel.setValueAt(dob, selectedRow, 2);
                        tableModel.setValueAt(email, selectedRow, 3);
                        tableModel.setValueAt(phone, selectedRow, 4);
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
                if (e.getClickCount() == 1) { // single click

                    int selectedRow = tblAuthors.getSelectedRow();
                    if (selectedRow != -1) {
                        txtName.setText((String) tblAuthors.getValueAt(selectedRow, 0));
                        txtSurname.setText((String) tblAuthors.getValueAt(selectedRow, 0));
                        txtDOB.setText((String) tblAuthors.getValueAt(selectedRow, 0));
                        txtEmail.setText((String) tblAuthors.getValueAt(selectedRow, 0));
                        txtphone.setText((String) tblAuthors.getValueAt(selectedRow, 0));
                    }

                }
            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                ArrayList<PersonModel> authors = AuthorController.getAuthors();
                ArrayList<PersonModel> result = new ArrayList<>();
                for (PersonModel author: authors) {
                    if (author.getName().contains(Search_txt.getText())||author.getSurname().contains(Search_txt.getText())||author.getEmail().contains(Search_txt.getText())){
                        result.add(author);
                    }
                }
                populateAuthorTable(result);

            }
        });
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Author View");
            frame.setContentPane(new AuthorView().Mainpanel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
        });
    }
    private void populateAuthorTable(ArrayList<PersonModel> authors) {

        // Create a table model with two columns: "First Name" and "Last Name"
        DefaultTableModel model = new DefaultTableModel(new String[]{"First Name", "Last Name","DateofBirth", "Email", "Phone","ID"}, 0);

        // Set the model to the table
        tblAuthors.setModel(model);

        // Populate the table with authors
        for (PersonModel author : authors) {
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
