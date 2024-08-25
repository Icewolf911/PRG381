package Controller;
import Model.BorrowerModel;

public class BorrowerController {
    private BorrowerModel borrowerModel;

    public BorrowerController(BorrowerModel borrowerModel) {
        this.borrowerModel = borrowerModel;
        //database add borrower
    }

    public BorrowerModel getBorrowerModel() {
        return borrowerModel;
    }

    public void setBorrowerModel(BorrowerModel borrowerModel) {
        this.borrowerModel = borrowerModel;
    }
}
