import java.sql.*;

public class DBconnection {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:LibraryDB;create=true";

    private Connection con;

    public void connect() {
        try {
            Class.forName(DRIVER);
            Connection connection = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connected to database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String createPeopleTable = "CREATE TABLE IF NOT EXISTS People (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(255) NOT NULL, " +
                "surname VARCHAR(255) NOT NULL, " +
                "dateOfBirth DATE, " +
                "email VARCHAR(255), " +
                "phone VARCHAR(20), " +
                "isAuthor BOOLEAN DEFAULT FALSE, " +
                "isBorrower BOOLEAN DEFAULT FALSE" +
                ");";

        String createBooksTable = "CREATE TABLE IF NOT EXISTS Books (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "title VARCHAR(255) NOT NULL, " +
                "genre VARCHAR(255), " +
                "publisher VARCHAR(255), " +
                "publicationDate DATE, " +
                "language VARCHAR(50), " +
                "numberOfCopies INT NOT NULL, " +
                "numberOfAvailableCopies INT NOT NULL, " +
                "numberOfBorrowedCopies INT NOT NULL, " +
                "author_id INT, " +
                "FOREIGN KEY (author_id) REFERENCES People(id)" +
                ");";

        String createBorrowedBooksTable = "CREATE TABLE IF NOT EXISTS BorrowedBooks (" +
                "id INT AUTO_INCREMENT PRIMARY KEY, " +
                "book_id INT, " +
                "borrower_id INT, " +
                "borrowDate DATE, " +
                "returnDate DATE, " +
                "FOREIGN KEY (book_id) REFERENCES Books(id), " +
                "FOREIGN KEY (borrower_id) REFERENCES People(id)" +
                ");";

        try (Statement stmt = con.createStatement()) {
            stmt.execute(createPeopleTable);
            stmt.execute(createBooksTable);
            stmt.execute(createBorrowedBooksTable);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPerson(String name, String surname, String dateOfBirth, String email, String phone, boolean isAuthor, boolean isBorrower) {
        String sql = "INSERT INTO People (name, surname, dateOfBirth, email, phone, isAuthor, isBorrower) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setDate(3, Date.valueOf(dateOfBirth)); // Assuming dateOfBirth is in "YYYY-MM-DD" format
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setBoolean(6, isAuthor);
            pstmt.setBoolean(7, isBorrower);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertBook(String title, String genre, String publisher, String publicationDate, String language, int numberOfCopies, int numberOfAvailableCopies, int numberOfBorrowedCopies, int authorId) {
        String sql = "INSERT INTO Books (title, genre, publisher, publicationDate, language, numberOfCopies, numberOfAvailableCopies, numberOfBorrowedCopies, author_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, publisher);
            pstmt.setDate(4, Date.valueOf(publicationDate)); // Assuming publicationDate is in "YYYY-MM-DD" format
            pstmt.setString(5, language);
            pstmt.setInt(6, numberOfCopies);
            pstmt.setInt(7, numberOfAvailableCopies);
            pstmt.setInt(8, numberOfBorrowedCopies);
            pstmt.setInt(9, authorId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void insertBorrowedBook(int bookId, int borrowerId, String borrowDate, String returnDate) {
        String sql = "INSERT INTO BorrowedBooks (book_id, borrower_id, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, borrowerId);
            pstmt.setDate(3, Date.valueOf(borrowDate)); // Assuming borrowDate is in "YYYY-MM-DD" format
            pstmt.setDate(4, Date.valueOf(returnDate)); // Assuming returnDate is in "YYYY-MM-DD" format
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public ResultSet getPersonById(int id) {
        String sql = "SELECT * FROM People WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public ResultSet getBookById(int id) {
        String sql = "SELECT * FROM Books WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public ResultSet getBorrowedBooksByBorrowerId(int borrowerId) {
        String sql = "SELECT * FROM BorrowedBooks WHERE borrower_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, borrowerId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void updatePerson(int id, String name, String surname, String dateOfBirth, String email, String phone, boolean isAuthor, boolean isBorrower) {
        String sql = "UPDATE People SET name = ?, surname = ?, dateOfBirth = ?, email = ?, phone = ?, isAuthor = ?, isBorrower = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setDate(3, Date.valueOf(dateOfBirth));
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setBoolean(6, isAuthor);
            pstmt.setBoolean(7, isBorrower);
            pstmt.setInt(8, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBook(int id, String title, String genre, String publisher, String publicationDate, String language, int numberOfCopies, int numberOfAvailableCopies, int numberOfBorrowedCopies, int authorId) {
        String sql = "UPDATE Books SET title = ?, genre = ?, publisher = ?, publicationDate = ?, language = ?, numberOfCopies = ?, numberOfAvailableCopies = ?, numberOfBorrowedCopies = ?, author_id = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, publisher);
            pstmt.setDate(4, Date.valueOf(publicationDate));
            pstmt.setString(5, language);
            pstmt.setInt(6, numberOfCopies);
            pstmt.setInt(7, numberOfAvailableCopies);
            pstmt.setInt(8, numberOfBorrowedCopies);
            pstmt.setInt(9, authorId);
            pstmt.setInt(10, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deletePerson(int id) {
        String sql = "DELETE FROM People WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteBook(int id) {
        String sql = "DELETE FROM Books WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public void deleteBorrowedBook(int id) {
        String sql = "DELETE FROM BorrowedBooks WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    



}
