package az.orient.bankdemo.adapter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversionRates {
    @JsonProperty(value = "AZN")
    private Double AZN;
    @JsonProperty(value = "EUR")
    private Double EUR;
    @JsonProperty(value = "USD")
    private Double USD;
}
