package usmanali.investmentapp;

public class user_info {
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



    public String getFather_name() {
        return father_name;
    }

    public void setFather_name(String father_name) {
        this.father_name = father_name;
    }

    public String getCNIC() {
        return CNIC;
    }

    public void setCNIC(String CNIC) {
        this.CNIC = CNIC;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getReferer_email() {
        return referer_email;
    }

    public void setReferer_email(String referer_email) {
        this.referer_email = referer_email;
    }

    public String getOpening_date() {
        return opening_date;
    }

    public void setOpening_date(String opening_date) {
        this.opening_date = opening_date;
    }

    public float getInvestment() {
        return investment;
    }

    public void setInvestment(int investment) {
        this.investment = investment;
    }

    public float getEarning() {
        return earning;
    }

    public void setEarning(int earning) {
        this.earning = earning;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }

    public float getProfit_percentage() {
        return profit_percentage;
    }

    public void setProfit_percentage(int profit_percentage) {
        this.profit_percentage = profit_percentage;
    }

    String email;
    String investment_period;

    public String getInvestment_period() {
        return investment_period;
    }

    public void setInvestment_period(String investment_period) {
        this.investment_period = investment_period;
    }

    String father_name;
    String CNIC;
    String password;
    String referer_email;
    String opening_date;
    float investment;
    float earning;
    int id;
    String Name;
    String user_type;
    float profit_percentage;
}
