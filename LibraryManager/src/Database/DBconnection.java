package Database;

import Model.AuthorModel;
import Model.BookModel;
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

    public void insertBook(String title, String genre, String publisher, java.util.Date publicationDate, String language, int numberOfCopies, int numberOfAvailableCopies, int numberOfBorrowedCopies) {
        java.sql.Date date = new java.sql.Date(publicationDate.getTime());
        String sql = "INSERT INTO Books (title, genre, publisher, publicationDate, language, numberOfCopies, numberOfAvailableCopies, numberOfBorrowedCopies) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, title);
            pstmt.setString(2, genre);
            pstmt.setString(3, publisher);
            pstmt.setDate(4, date); // Assuming publicationDate is in "YYYY-MM-DD" format
            pstmt.setString(5, language);
            pstmt.setInt(6, numberOfCopies);
            pstmt.setInt(7, numberOfAvailableCopies);
            pstmt.setInt(8, numberOfBorrowedCopies);
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
                person.setAuthor(rs.getBoolean("isAuthor"));
                person.setBorrower(rs.getBoolean("isBorrower"));
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
                // Add other fields as necessary

                return author;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }




    public List<BookModel> getBorrowedBooksByBorrowerId(int borrowerId) {
        List<BookModel> borrowedBooks = new ArrayList<>();
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

                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public void populateTablesWithDummyData() {
        String insertPeople = "INSERT INTO People (name, surname, dateOfBirth, email, phone, isAuthor, isBorrower) VALUES (?, ?, ?, ?, ?, ?, ?)";
        String insertBooks = "INSERT INTO Books (title, genre, publisher, publicationDate, language, numberOfCopies, numberOfAvailableCopies, numberOfBorrowedCopies, author_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String insertBorrowedBooks = "INSERT INTO BorrowedBooks (book_id, borrower_id, borrowDate, returnDate) VALUES (?, ?, ?, ?)";

        try (PreparedStatement pstmtPeople = con.prepareStatement(insertPeople);
             PreparedStatement pstmtBooks = con.prepareStatement(insertBooks);
             PreparedStatement pstmtBorrowedBooks = con.prepareStatement(insertBorrowedBooks)) {

            // Insert dummy records into People
            pstmtPeople.setString(1, "John");
            pstmtPeople.setString(2, "Doe");
            pstmtPeople.setDate(3, Date.valueOf("1980-01-01"));
            pstmtPeople.setString(4, "john.doe@example.com");
            pstmtPeople.setString(5, "123-456-7890");
            pstmtPeople.setBoolean(6, true);  // Example author
            pstmtPeople.setBoolean(7, false);
            pstmtPeople.executeUpdate();

            pstmtPeople.setString(1, "Jane");
            pstmtPeople.setString(2, "Smith");
            pstmtPeople.setDate(3, Date.valueOf("1990-02-02"));
            pstmtPeople.setString(4, "jane.smith@example.com");
            pstmtPeople.setString(5, "098-765-4321");
            pstmtPeople.setBoolean(6, false);
            pstmtPeople.setBoolean(7, true);  // Example borrower
            pstmtPeople.executeUpdate();

            // Assume we know the IDs of the inserted people (this is simplified)
            int authorId = 1;  // Example author ID
            int borrowerId = 2;  // Example borrower ID

            // Insert dummy records into Books
            pstmtBooks.setString(1, "The Great Adventure");
            pstmtBooks.setString(2, "Fiction");
            pstmtBooks.setString(3, "Fictional Publisher");
            pstmtBooks.setDate(4, Date.valueOf("2024-01-01"));
            pstmtBooks.setString(5, "English");
            pstmtBooks.setInt(6, 10);
            pstmtBooks.setInt(7, 8);
            pstmtBooks.setInt(8, 2);
            pstmtBooks.setInt(9, authorId);
            pstmtBooks.executeUpdate();

            pstmtBooks.setString(1, "Learning Java");
            pstmtBooks.setString(2, "Educational");
            pstmtBooks.setString(3, "Tech Publisher");
            pstmtBooks.setDate(4, Date.valueOf("2023-01-01"));
            pstmtBooks.setString(5, "English");
            pstmtBooks.setInt(6, 5);
            pstmtBooks.setInt(7, 5);
            pstmtBooks.setInt(8, 0);
            pstmtBooks.setInt(9, authorId);
            pstmtBooks.executeUpdate();

            // Assume we know the IDs of the inserted books (this is simplified)
            int bookId1 = 1;  // Example book ID
            int bookId2 = 2;  // Example book ID

            // Insert dummy records into BorrowedBooks
            pstmtBorrowedBooks.setInt(1, bookId1);
            pstmtBorrowedBooks.setInt(2, borrowerId);
            pstmtBorrowedBooks.setDate(3, Date.valueOf("2024-08-01"));
            pstmtBorrowedBooks.setDate(4, Date.valueOf("2024-08-15"));
            pstmtBorrowedBooks.executeUpdate();

            System.out.println("Tables populated with dummy data");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
