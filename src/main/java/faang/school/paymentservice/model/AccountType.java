package faang.school.paymentservice.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AccountType {
    DEBIT("8888"),
    SAVINGS("9999");

    private final String value;
}
