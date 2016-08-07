package app.msdeep14.tripcount;

/**
 * Created by hp15-p017tu on 29-07-2016.
 */
public class balancedata {
    public String name;
    public String money;

    public balancedata(String name, String money) {
        this.name = name;
        this.money = money;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}