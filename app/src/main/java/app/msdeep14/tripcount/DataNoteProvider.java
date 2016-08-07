package app.msdeep14.tripcount;

/**
 * Created by mandeep singh on 7/26/2016.
 */
public class DataNoteProvider {
    private int amount;

    public DataNoteProvider(int amount, String note, int id) {
        this.amount = amount;
        this.note = note;
        this.id = id;
    }

    private  String note;
    private  int id;

    public int getAmount() {
        return amount;
    }

    public DataNoteProvider(int id,int amount,String note){
        this.setAmount(amount);
        this.setNote(note);

    }

    public DataNoteProvider(int amount,String note){
        this.setAmount(amount);
        this.setNote(note);
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}