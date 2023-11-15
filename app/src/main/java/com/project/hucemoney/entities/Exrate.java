package com.project.hucemoney.entities;

import org.simpleframework.xml.Attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Exrate {
    @Attribute(name = "CurrencyCode")
    private String currencyCode;

    @Attribute(name = "CurrencyName")
    private String currencyName;

    @Attribute(name = "Buy")
    private String buyRate;

    @Attribute(name = "Transfer")
    private String transferRate;

    @Attribute(name = "Sell")
    private String sellRate;

    public String getCurrencyName() {
        return currencyName.trim();
    }

    public double getBuyRateAsDouble() {
        return "-".equals(buyRate) ? 0f : Double.parseDouble(buyRate.replace(",", ""));
    }

    public double getTransferRateAsDouble() {
        return "-".equals(transferRate) ? 0f : Double.parseDouble(transferRate.replace(",", ""));
    }

    public double getSellRateAsDouble() {
        return "-".equals(sellRate) ? 0f : Double.parseDouble(sellRate.replace(",", ""));
    }
}
