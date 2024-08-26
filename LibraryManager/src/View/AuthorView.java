package View;

import Controller.AuthorController;

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
    private JButton displayAllButton;
    private JButton editButton;

    public AuthorView() {

        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                String name = txtName.getText();
                String surname = txtSurname.getText();
                String dob = txtDOB.getText();
                String email = txtEmail.getText();
                String phone = txtphone.getText();

                if (name.isEmpty() || surname.isEmpty() || dob.isEmpty() || email.isEmpty() || phone.isEmpty()){
                    JOptionPane.showConfirmDialog(this,//weni wat om hier te doen nie
                            "Enter all fields",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }else {
                    db.add(name, surname, dob, email, phone);
                    JOptionPane.showConfirmDialog(this,//weni wat om hier te doen nie
                            "Added to db",
                            "Confirm",
                            JOptionPane.ERROR_MESSAGE);

                    DefaultTableModel model = (DefaultTableModel) tblAuthors.getModel();
                    model.addRow(new Object[]{name, surname, dob, email, phone});
                }
            }
            public static AuthorController db = new AuthorController();

        });
        displayAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel model = (DefaultTableModel) tblAuthors.getModel();

                for (String[] row : db.view()){
                    model.addRow(row);
                }
            }
            public static AuthorController db = new AuthorController();

        });



    }
}
