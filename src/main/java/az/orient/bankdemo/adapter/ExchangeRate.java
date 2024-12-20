package az.orient.bankdemo.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRate {
    private String result;
    @JsonProperty(value = "conversion_rates")
    private ConversionRates conversionRates;
}
