package az.orient.bankdemo.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum EnumTransactionType {
    INTERNAL(1),INLAND(2);
    public int value;
}
