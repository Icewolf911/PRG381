package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainMenuView extends JDialog {
    private JPanel contentPane;
    private JButton addAuthor_btn;
    private JButton addBook_btn;
    private JTable author_tbl;
    private JTable book_tbl;
    private JButton borrowBook_btn;
    private JLabel authors_lbl;
    private JLabel books_lbl;
    private JLabel title_lbl;

    public MainMenuView() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(addAuthor_btn);


    }



    public static void main(String[] args) {
        MainMenuView dialog = new MainMenuView();
        dialog.setPreferredSize(new Dimension(800,400));
        dialog.pack();
        dialog.setVisible(true);

        System.exit(0);
    }


}
