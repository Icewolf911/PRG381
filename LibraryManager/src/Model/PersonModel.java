package Model;

public class PersonModel {
    private String name;
    private String surname;
    private String dateOfBirth;
    private String email;
    private String phone;
    private int id;
    private boolean isAuthor;
    private boolean isBorrower;

    public PersonModel(String name, String surname, String dateOfBirth, String email, String phone) {
        this.name = name;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.phone = phone;
    }

    public PersonModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isAuthor() {
        return isAuthor;
    }

    public void setAuthor(boolean author) {
        isAuthor = author;
    }

    public boolean isBorrower() {
        return isBorrower;
    }

    public void setBorrower(boolean borrower) {
        isBorrower = borrower;
    }
}
