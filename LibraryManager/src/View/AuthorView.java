package View;

import Controller.AuthorController;
import Database.DBconnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
    private JButton button1;
    private JTextField textField1;
    private JButton button2;
    private DefaultTableModel tableModel;

    public AuthorView() {

        tableModel = new DefaultTableModel(new String[]{"First Name", "Last Name", "Email", "Phone"}, 0);
        tblAuthors.setModel(tableModel);
        DBconnection db = new DBconnection();

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
<<<<<<< Updated upstream
                    AuthorController.addAuthor(name, surname, dob, email, phone);
                    JOptionPane.showConfirmDialog(Mainpanel,//weni wat om hier te doen nie -> the 'this' needs to reference an actual component, I just referenced the Mainpanel component in place of 'this'
=======
                    db.add(name, surname, dob, email, phone);
                    JOptionPane.showConfirmDialog(Mainpanel,
>>>>>>> Stashed changes
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
            public static AuthorController db = new AuthorController();

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
                            "Please select a borrower to delete.");
                    return;
                }

                // Remove the selected row from the table
                tableModel.removeRow(selectedRow);
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
}
