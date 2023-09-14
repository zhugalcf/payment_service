package faang.school.paymentservice.model;

public enum AccountType {
    DEBIT("8888"),
    SAVINGS("9999");

    private final String value;

    AccountType(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
