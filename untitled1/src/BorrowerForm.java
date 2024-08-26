package com.library.management.ui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Borrower extends JFrame {

    private JTextField txtFirstName;
    private JTextField txtLastName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JButton btnAdd;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTable tableBorrowers;
    private JPanel rootPanel;
    private DefaultTableModel tableModel;

    public Borrower() {

        tableModel = new DefaultTableModel(new String[]{"First Name", "Last Name", "Email", "Phone"}, 0);
        tableBorrowers.setModel(tableModel);


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


        tableModel.addRow(new Object[]{firstName, lastName, email, phone});

        clearFields();
    }

    private void updateBorrower() {
        int selectedRow = tableBorrowers.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a borrower to update.");
            return;
        }


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
            Borrower form = new Borrower();
            form.setContentPane(form.rootPanel);
            form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            form.pack();
            form.setVisible(true);
        });
    }
}
