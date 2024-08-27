package View;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

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


    public BookView(){
        DefaultTableModel tableModel = (DefaultTableModel) ExistingBooks_tbl.getModel();

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

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                Date publishDateAsDate = null;
                try {
                    publishDateAsDate = dateFormat.parse(publishDate);
                } catch (ParseException ex) {
                    throw new RuntimeException(ex);
                }

                if (title.isEmpty() || genre.isEmpty() || author.isEmpty() || publisher.isEmpty() || publishDate.isEmpty() || language.isEmpty() || NumCopies_txt.getText().isEmpty() || AvailableCopies_txt.getText().isEmpty() || BorrowedCopies_txt.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(Mainpanel, "One or more fields are invalid.", "Invalid Fields", JOptionPane.ERROR_MESSAGE);
                } else {
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
        });

    }




}
