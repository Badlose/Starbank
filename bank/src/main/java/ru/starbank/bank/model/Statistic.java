package ru.starbank.bank.model;

import java.util.UUID;

public class Statistic {
    private UUID rule_id;
    private int count = 0;

    public Statistic(UUID rule_id, int count) {
        this.rule_id = rule_id;
        this.count = count;
    }

    public UUID getRule_id() {
        return rule_id;
    }

    public void setRule_id(UUID rule_id) {
        this.rule_id = rule_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }


}
