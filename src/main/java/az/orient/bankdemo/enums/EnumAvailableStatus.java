package az.orient.bankdemo.enums;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public enum EnumAvailableStatus {
    ACTIVE(1),DEACTIVE(0), DELETED(2);
    public int value;
}
