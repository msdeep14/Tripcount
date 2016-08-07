package app.msdeep14.tripcount;

/**
 * Created by hp15-p017tu on 25-07-2016.
 */
public class item {

    private  int id;
    private  String name;
    private String amount;
    private  String note;

    public item(int id, String name, String amount, String note) {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.note = note;
    }

    public item(String note, String amount, String name){
        this.name = name;
        this.amount = amount;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getName() {
        return name;
    }

    public String getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }

    public String getNote() {
        return note;
    }
}

