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

    public static void deleteAuthor(int id) {
        db.connect();
        db.deletePerson(id);
    }

    public static AuthorModel getAuthorModel(int id) {
        return db.getAuthorById(id);

    }

    public void setAuthorModel(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }



}
