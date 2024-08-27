package Database;

import Model.PersonModel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DBconnection {

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
    private static final String JDBC_URL = "jdbc:derby:LibraryDB;create=true";

    private Connection con;

    public void connect() {
        try {
            Class.forName(DRIVER);
            con = DriverManager.getConnection(JDBC_URL);
            System.out.println("Connected to database");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void createTables() {
        String createPeopleTable = "CREATE TABLE People (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "name VARCHAR(255) NOT NULL, " +
                "surname VARCHAR(255) NOT NULL, " +
                "dateOfBirth DATE, " +
                "email VARCHAR(255), " +
                "phone VARCHAR(20), " +
                "isAuthor SMALLINT DEFAULT 0, " + // Using SMALLINT to represent BOOLEAN
                "isBorrower SMALLINT DEFAULT 0" + // Using SMALLINT to represent BOOLEAN
                ")";

        String createBooksTable = "CREATE TABLE Books (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
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
                ")";

        String createBorrowedBooksTable = "CREATE TABLE BorrowedBooks (" +
                "id INT PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1), " +
                "book_id INT, " +
                "borrower_id INT, " +
                "borrowDate DATE, " +
                "returnDate DATE, " +
                "FOREIGN KEY (book_id) REFERENCES Books(id), " +
                "FOREIGN KEY (borrower_id) REFERENCES People(id)" +
                ")";

        try (Statement stmt = con.createStatement()) {
            // Check if tables exist before creating them
            DatabaseMetaData dbm = con.getMetaData();

            ResultSet tables = dbm.getTables(null, null, "PEOPLE", null);
            if (!tables.next()) {
                stmt.execute(createPeopleTable);
            }

            tables = dbm.getTables(null, null, "BOOKS", null);
            if (!tables.next()) {
                stmt.execute(createBooksTable);
            }

            tables = dbm.getTables(null, null, "BORROWEDBOOKS", null);
            if (!tables.next()) {
                stmt.execute(createBorrowedBooksTable);
            }

            System.out.println("Tables created or already exist");
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


    public PersonModel getPersonById(int id) {
        String sql = "SELECT * FROM People WHERE id = ?";
        PersonModel person = null;

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String dateOfBirth = rs.getString("dateOfBirth");
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                // Create a PersonModel object with the retrieved data
                person = new PersonModel(name, surname, dateOfBirth, email, phone);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return person;
    }

    public ArrayList<PersonModel> getPersons() {
        String sql = "SELECT * FROM People";
        ArrayList<PersonModel> people = new ArrayList<>();

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String dateOfBirth = rs.getString("dateOfBirth"); // Assuming dateOfBirth is stored as a string
                String email = rs.getString("email");
                String phone = rs.getString("phone");

                // Assuming PersonModel has a constructor that takes all these fields
                PersonModel person = new PersonModel(name, surname, dateOfBirth, email, phone);
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // You might want to return an empty list instead, depending on your error-handling strategy
        }

        return people;
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
