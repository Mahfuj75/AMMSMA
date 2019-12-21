package asa.org.bd.ammsma.extra;

/**
 * Created by Mahfuj75 on 5/30/2017.
 */

public class TransactionHistory {
    private String date;
    private String amount;
    private String type;
    private String process;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }
}
