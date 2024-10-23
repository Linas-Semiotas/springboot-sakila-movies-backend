package lt.ca.javau10.sakila.models.dto;

public class BalanceDto {
    
    private Double amount;

    public BalanceDto() {}

    public BalanceDto(Double amount) {
        this.amount = amount;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
