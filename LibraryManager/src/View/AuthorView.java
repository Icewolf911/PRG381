package View;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AuthorView {
    private JPanel panel1;
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
                    JOptionPane.showConfirmDialog(this,
                            "Enter all fields",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }else {
                    
                }
            }
        });
    }
}
