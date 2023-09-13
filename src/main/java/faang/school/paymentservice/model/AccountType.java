package faang.school.paymentservice.model;

public enum AccountType {
    DEBIT("debit"),
    SAVINGS("savings");

    private final String value;

    AccountType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
