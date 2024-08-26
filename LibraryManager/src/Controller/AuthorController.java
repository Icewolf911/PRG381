package Controller;
import Model.AuthorModel;

public class AuthorController {
    private AuthorModel authorModel;

    public AuthorController(AuthorModel authorModel) {
        this.authorModel = authorModel;
        //database add author
    }

    public AuthorModel getAuthorModel() {
        return authorModel;
    }

    public void setAuthorModel(AuthorModel authorModel) {
        this.authorModel = authorModel;
    }

}
