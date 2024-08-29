package Model;

public class BookModel {
    private String title;
    private String genre;
    private String publisher;
    private String publicationDate;
    private String language;
    private int numberOfCopies;
    private int numberOfAvailableCopies;
    private int numberOfBorrowedCopies;
    private AuthorModel author;
    private int id;

    public BookModel(String title, String isbn, String genre, String publisher, String publicationDate, String language, int numberOfPages, int numberOfCopies, int numberOfAvailableCopies, int numberOfBorrowedCopies, int numberOfReservedCopies, AuthorModel author) {
        this.title = title;
        this.genre = genre;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.language = language;
        this.numberOfCopies = numberOfCopies;
        this.numberOfAvailableCopies = numberOfAvailableCopies;
        this.numberOfBorrowedCopies = numberOfBorrowedCopies;
        this.author = author;
    }

    public BookModel(String title, String genre, String publisher, String publicationDate, String language, int numberOfCopies, AuthorModel author) {
        this.title = title;
        this.genre = genre;
        this.publisher = publisher;
        this.publicationDate = publicationDate;
        this.language = language;
        this.numberOfCopies = numberOfCopies;
        this.author = author;
    }

    public BookModel() {

    }

    public String getTitle() {
        return title;
    }


    public String getGenre() {
        return genre;
    }

    public String getPublisher() {
        return publisher;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public String getLanguage() {
        return language;
    }

    public int getNumberOfCopies() {
        return numberOfCopies;
    }

    public int getNumberOfAvailableCopies() {
        return numberOfAvailableCopies;
    }

    public int getNumberOfBorrowedCopies() {
        return numberOfBorrowedCopies;
    }


    public AuthorModel getAuthor() {
        return author;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setNumberOfCopies(int numberOfCopies) {
        this.numberOfCopies = numberOfCopies;
    }

    public void setNumberOfAvailableCopies(int numberOfAvailableCopies) {
        this.numberOfAvailableCopies = numberOfAvailableCopies;
    }

    public void setNumberOfBorrowedCopies(int numberOfBorrowedCopies) {
        this.numberOfBorrowedCopies = numberOfBorrowedCopies;
    }

    public void setAuthor(AuthorModel author) {
        this.author = author;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
}
