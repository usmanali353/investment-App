package usmanali.investmentapp;

public class withdraw_notifications {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWithdraw_amount() {
        return withdraw_amount;
    }

    public void setWithdraw_amount(int withdraw_amount) {
        this.withdraw_amount = withdraw_amount;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getNotification_text() {
        return notification_text;
    }

    public void setNotification_text(String notification_text) {
        this.notification_text = notification_text;
    }

    public String getNotification_date() {
        return notification_date;
    }

    public void setNotification_date(String notification_date) {
        this.notification_date = notification_date;
    }

    public String getApproved() {
        return approved;
    }

    public void setApproved(String approved) {
        this.approved = approved;
    }

    int id,withdraw_amount;
    String customer_email,notification_text,notification_date,approved;
}
