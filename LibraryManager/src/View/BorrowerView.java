package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BorrowerView extends JFrame {
    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTable tableBorrowers;
    private DefaultTableModel tableModel;

    public BorrowerView() {
        // Initialize the table model and set it for the table
        tableModel = new DefaultTableModel(new String[]{"First Name", "Last Name", "Email", "Phone"}, 0);
        tableBorrowers.setModel(tableModel);

        // Action listeners for buttons
        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addBorrower();
            }
        });

        btnUpdate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateBorrower();
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteBorrower();
            }
        });
    }

    private void addBorrower() {
        String firstName = txtFirstName.getText();
        String lastName = txtLastName.getText();
        String email = txtEmail.getText();
        String phone = txtPhone.getText();

        // Add data to the table (just for display purposes)
        tableModel.addRow(new Object[]{firstName, lastName, email, phone});

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
        tableModel.setValueAt(txtFirstName.getText(), selectedRow, 0);
        tableModel.setValueAt(txtLastName.getText(), selectedRow, 1);
        tableModel.setValueAt(txtEmail.getText(), selectedRow, 2);
        tableModel.setValueAt(txtPhone.getText(), selectedRow, 3);

        clearFields();
    }

    private void deleteBorrower() {
        int selectedRow = tableBorrowers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to delete.");
            return;
        }

        // Remove the selected row from the table
        tableModel.removeRow(selectedRow);
    }

    private void clearFields() {
        txtFirstName.setText("");
        txtLastName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Borrower View");
            frame.setContentPane(new BorrowerView().tableBorrowers);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setVisible(true);
            
        });
    }
}
