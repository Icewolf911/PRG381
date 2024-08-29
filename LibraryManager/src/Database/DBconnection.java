package Database;

import Model.AuthorModel;
import Model.BookModel;
import Model.PersonModel;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
                "id INT PRIMARY KEY , " +
                "name VARCHAR(255) NOT NULL, " +
                "surname VARCHAR(255) NOT NULL, " +
                "dateOfBirth DATE, " +
                "email VARCHAR(255), " +
                "phone VARCHAR(20), " +
                "isAuthor SMALLINT DEFAULT 0, " + // Using SMALLINT to represent BOOLEAN
                "isBorrower SMALLINT DEFAULT 0" + // Using SMALLINT to represent BOOLEAN
                ")";

        String createBooksTable = "CREATE TABLE Books (" +
                "id INT PRIMARY KEY , " +
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
                "id INT PRIMARY KEY , " +
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

    public void clearDatabase() {
        try (Statement stmt = con.createStatement()) {
            // Disable foreign key constraints
            stmt.execute("SET CONSTRAINTS ALL DEFERRED");

            // Delete data from tables
            stmt.execute("DELETE FROM BorrowedBooks");
            stmt.execute("DELETE FROM Books");
            stmt.execute("DELETE FROM People");

            // Drop and recreate tables to reset identity columns
            stmt.execute("DROP TABLE BorrowedBooks");
            stmt.execute("DROP TABLE Books");
            stmt.execute("DROP TABLE People");

            createTables(); // Call the method to recreate the tables

            System.out.println("Database cleared");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertPerson(String name, String surname, String dateOfBirth, String email, String phone, boolean isAuthor, boolean isBorrower) {

        String sql = "INSERT INTO People (id,name, surname, dateOfBirth, email, phone, isAuthor, isBorrower) VALUES " +
                "(?,?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, getLastInsertedPersonId() + 1); // Get the last inserted ID and increment by 1
            pstmt.setString(2, name);
            pstmt.setString(3, surname);
            pstmt.setDate(4, Date.valueOf(dateOfBirth)); // Assuming dateOfBirth is in "YYYY-MM-DD" format
            pstmt.setString(5, email);
            pstmt.setString(6, phone);
            pstmt.setBoolean(7, isAuthor);
            pstmt.setBoolean(8, isBorrower);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastInsertedPersonId() {
        String sql = "SELECT MAX(id) FROM People";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the maximum id value
                } else {
                    return -1; // Return -1 if no rows are found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
        }
    }

    public void insertBook(String title, String genre, String publisher, java.util.Date publicationDate,
                           String language, int numberOfCopies, int numberOfAvailableCopies, int numberOfBorrowedCopies, int authorId) {
        java.sql.Date date = new java.sql.Date(publicationDate.getTime());
        String sql = "INSERT INTO Books (title, genre, publisher, publicationDate, language, numberOfCopies, " +
                "numberOfAvailableCopies, numberOfBorrowedCopies,author_id,id) VALUES ( ?, ?, ?, ?, ?, ?,?,?,?,?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, publisher);
            pstmt.setDate(4, date); // Assuming publicationDate is in "YYYY-MM-DD" format
            pstmt.setString(5, language);
            pstmt.setInt(6, numberOfCopies);
            pstmt.setInt(7, numberOfAvailableCopies);
            pstmt.setInt(8, numberOfBorrowedCopies);
            pstmt.setInt(9, authorId);
            pstmt.setInt(10, getLastInsertedBookId() + 4); // Get the last inserted ID and increment by 1
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getLastInsertedBookId() {
        String sql = "SELECT MAX(id) FROM Books";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the maximum id value
                } else {
                    return -1; // Return -1 if no rows are found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
        }
    }


    public void insertBorrowedBook(int bookId, int borrowerId, String borrowDate, String returnDate) {
        String sql = "INSERT INTO BorrowedBooks (book_id, borrower_id, borrowDate, returnDate,id) VALUES (?, ?, ?, ?," +
                "?)";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date borrowDateJ = null;
        java.util.Date returnDateJ = null;

        try {
            borrowDateJ = dateFormat.parse(borrowDate);
            returnDateJ = dateFormat.parse(returnDate);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
        java.sql.Date borrowDateS = new java.sql.Date(borrowDateJ.getTime());
        java.sql.Date returnDateS = new java.sql.Date(returnDateJ.getTime());
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, bookId);
            pstmt.setInt(2, borrowerId);
            pstmt.setDate(3, borrowDateS); // Assuming borrowDate is in "YYYY-MM-DD" format
            pstmt.setDate(4, returnDateS); // Assuming returnDate is in "YYYY-MM-DD" format
            pstmt.setInt(5, getLastInsertedBorrowedBookId() + 1); // Get the last inserted ID and increment by 1
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public int getLastInsertedBorrowedBookId() {
        String sql = "SELECT MAX(id) FROM BorrowedBooks";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Return the maximum id value
                } else {
                    return -1; // Return -1 if no rows are found
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Return -1 if an error occurs
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
                int id = rs.getInt("id");

                // Assuming PersonModel has a constructor that takes all these fields
                PersonModel person = new PersonModel(name, surname, dateOfBirth, email, phone);
                person.setAuthor(rs.getBoolean("isAuthor"));
                person.setBorrower(rs.getBoolean("isBorrower"));
                person.setId(id);
                people.add(person);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return null; // You might want to return an empty list instead, depending on your error-handling strategy
        }

        return people;
    }

    public AuthorModel getAuthorById(int id) {
        String sql = "SELECT * FROM People WHERE id = ? AND isAuthor = 1"; // Assuming isAuthor is stored as 1 for true
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                AuthorModel author = new AuthorModel();
                author.setName(rs.getString("name"));
                author.setSurname(rs.getString("surname"));
                author.setDateOfBirth(rs.getDate("dateOfBirth").toString()); // Assuming you want a String format
                author.setEmail(rs.getString("email"));
                author.setPhone(rs.getString("phone"));
                author.setId(rs.getInt("id"));
                // Add other fields as necessary

                return author;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<BookModel> getBorrowedBooksByBorrowerId(int borrowerId) {
        ArrayList<BookModel> borrowedBooks = new ArrayList<>();
        String sql = "SELECT * FROM BorrowedBooks WHERE borrower_id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, borrowerId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int bookId = rs.getInt("book_id");
                BookModel book = getBookById(bookId); // Fetch the book details using its ID
                if (book != null) {
                    borrowedBooks.add(book);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return borrowedBooks;
    }


    public void updatePerson(int id, String name, String surname, String dateOfBirth, String email, String phone) {
        String sql = "UPDATE People SET name = ?, surname = ?, dateOfBirth = ?, email = ?, phone = ? WHERE id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.setString(2, surname);
            pstmt.setDate(3, Date.valueOf(dateOfBirth));
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setInt(6, id);
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
        // Delete books associated with the person
        String deleteBooksSql = "DELETE FROM Books WHERE author_id = ?";
        try (PreparedStatement deleteBooksStmt = con.prepareStatement(deleteBooksSql)) {
            deleteBooksStmt.setInt(1, id);
            deleteBooksStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Delete the person
        String deletePersonSql = "DELETE FROM People WHERE id = ?";
        try (PreparedStatement deletePersonStmt = con.prepareStatement(deletePersonSql)) {
            deletePersonStmt.setInt(1, id);
            deletePersonStmt.executeUpdate();
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


    public void deleteBorrowedBook(int book_id ,int borrower_id) {
        String sql = "DELETE FROM BorrowedBooks WHERE book_id = ? AND borrower_id = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, book_id);
            pstmt.setInt(2, borrower_id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public BookModel getBookById(int id) {
        BookModel book = null;
        String sql = "SELECT * FROM Books WHERE id = ?";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                book = new BookModel();
                book.setTitle(rs.getString("title"));
                book.setGenre(rs.getString("genre"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getDate("publicationDate").toString()); // Assuming you want a String format
                book.setLanguage(rs.getString("language"));
                book.setNumberOfCopies(rs.getInt("numberOfCopies"));
                book.setNumberOfAvailableCopies(rs.getInt("numberOfAvailableCopies"));
                book.setNumberOfBorrowedCopies(rs.getInt("numberOfBorrowedCopies"));

                int authorId = rs.getInt("author_id");
                AuthorModel author = getAuthorById(authorId); // Fetch the author using the author ID
                book.setAuthor(author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return book;
    }

    public ArrayList<BookModel> getBooks() {
        ArrayList<BookModel> books = new ArrayList<>();
        String sql = "SELECT * FROM Books";

        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BookModel book = new BookModel();
                book.setTitle(rs.getString("title"));
                book.setGenre(rs.getString("genre"));
                book.setPublisher(rs.getString("publisher"));
                book.setPublicationDate(rs.getDate("publicationDate").toString()); // Assuming you want a String format
                book.setLanguage(rs.getString("language"));
                book.setNumberOfCopies(rs.getInt("numberOfCopies"));
                book.setNumberOfAvailableCopies(rs.getInt("numberOfAvailableCopies"));
                book.setNumberOfBorrowedCopies(rs.getInt("numberOfBorrowedCopies"));

                int authorId = rs.getInt("author_id");

                AuthorModel author = getAuthorById(authorId); // Assuming you have this method
                book.setAuthor(author);

                book.setId(rs.getInt("id"));

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void populateTablesWithDummyData() {
        // Create a few people
        insertPerson("John", "Doe", "1990-01-01", "john.doe@example.com", "1234567890", true, false);
        insertPerson("Jane", "Doe", "1992-02-02", "jane.doe@example.com", "0987654321", false, true);
        insertPerson("Bob", "Smith", "1980-03-03", "bob.smith@example.com", "5551234567", true, true);

        // Create a few books
        int authorId1 = getLastInsertedPersonId();
        insertBook("Book 1", "Fiction", "Publisher 1", new java.util.Date(), "English", 10, 10, 0, authorId1);
        insertBook("Book 2", "Non-Fiction", "Publisher 2", new java.util.Date(), "Spanish", 20, 20, 0, authorId1);

        int authorId2 = getLastInsertedPersonId();
        insertBook("Book 3", "Romance", "Publisher 3", new java.util.Date(), "French", 15, 15, 0, authorId2);

        // Create a few borrowed books
        int bookId1 = getLastInsertedBookId();
        int borrowerId1 = getLastInsertedPersonId();
        insertBorrowedBook(bookId1, borrowerId1, "2022-01-01", "2022-01-31");

        int bookId2 = getLastInsertedBookId();
        int borrowerId2 = getLastInsertedPersonId();
        insertBorrowedBook(bookId2, borrowerId2, "2022-02-01", "2022-02-28");
    }


}
