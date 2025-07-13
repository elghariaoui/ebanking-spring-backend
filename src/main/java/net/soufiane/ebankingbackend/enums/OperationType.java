package net.soufiane.ebankingbackend.enums;

public enum OperationType {
    DEBIT, CREDIT;

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
