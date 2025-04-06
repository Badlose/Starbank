package ru.starbank.bank.dto;

import java.util.List;

public class ListStatisticDTO {

    private List<StatisticDTO> stats;

    public ListStatisticDTO() {
    }

    public List<StatisticDTO> getStats() {
        return stats;
    }

    public void setStats(List<StatisticDTO> stats) {
        this.stats = stats;
    }

}
