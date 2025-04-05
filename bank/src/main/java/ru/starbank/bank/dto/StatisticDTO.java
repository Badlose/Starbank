package ru.starbank.bank.dto;

public class StatisticDTO {

    private Long rule_id;

    private int count;

    public StatisticDTO() {
    }

    public Long getRule_id() {
        return rule_id;
    }

    public void setRule_id(Long rule_id) {
        this.rule_id = rule_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
