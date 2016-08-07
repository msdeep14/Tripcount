package app.msdeep14.tripcount;


/**
 * Created by mandeep singh on 7/25/2016.
 */
public class DataProvider {
    private int amount;
    private  String name;
    private  int id;


    public DataProvider(String name, int id, int amount) {
        this.name = name;
        this.id = id;
        this.amount = amount;
    }

    public DataProvider(int amount, String name) {
        this.amount = amount;
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
