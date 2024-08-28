package Controller;
import Database.DBconnection;
import Model.AuthorModel;
import Model.PersonModel;

import java.sql.*;
import java.util.ArrayList;

public class AuthorController {
    private AuthorModel authorModel;
    static DBconnection db = new DBconnection();


    public AuthorController() {
        this.authorModel = authorModel;
        //database add author
        db.connect();

    }

    public static ArrayList<PersonModel> getAuthors() {
        db.connect();
        ArrayList<PersonModel> authors = new ArrayList<>();
        ArrayList<PersonModel> people = db.getPersons();
        for (PersonModel person : people) {
            if (person.isAuthor()) {
                authors.add(person);
            }
        }
        return authors;
    }

    public static void addAuthor(String name, String surname, String dob, String email, String phone) {
        db.connect();
        db.insertPerson(name, surname, dob, email, phone, true, false);
    }

    public AuthorModel getAuthorModel() {
        return authorModel;
    }

    public void setAuthorModel(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }

    private static final String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    private static final String JDBC_URL = "jdbc:derby:DB;create=true";

    Connection con;

    public void createAuthorsTable() throws SQLException {
        try {
            String query = "Create Table Authors(Name varchar(20), Surname varchar(20), DateofBirth varchar(20), E-mail varchar(20), Phone varchar(10))";
            this.con.createStatement().execute(query);
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

//    public void add(String name, String surname, String dob, String email, String phone){
//        try {
//            String query = "INSERT INTO Authors VALUES ('"+name+"','"+surname+"', '"+dob+"', '"+email+"', '"+phone+"')";
//            this.con.createStatement().execute(query);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            System.out.println("Data not added");
//        }
//    }

    public ArrayList<String[]> view(){
        ArrayList<String[]> dataList = new ArrayList<>();

        try {
            String query = "SELECT * FROM Authors";
            ResultSet table = this.con.createStatement().executeQuery(query);

            while (table.next()){
                String name = table.getString("Name");
                String surname = table.getString("Surname");
                String dob = table.getString("DateofBirth");
                String email = table.getString("Email");
                String phone = table.getString("Phone");

                String[] row = {name, surname, dob, email, phone};
                dataList.add(row);

            }

            } catch (SQLException e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public void connect() throws ClassNotFoundException{
        try {
            Class.forName(DRIVER);
            this.con = DriverManager.getConnection(JDBC_URL);
            if (this.con != null) {
                System.out.println("Connected to db");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
