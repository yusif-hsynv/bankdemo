package az.orient.bankdemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;

import java.util.Date;

@Data
@Entity
@Table(name = "transaction")
@DynamicInsert
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "dt_account_id")
    private Account dtAccount;
    private Double amount;
    private String crAccountIban;
    @ManyToOne
    @JoinColumn(name = "cr_account_id")
    private Account crAccount;
    private String currency;
    private Integer transactionType;
    @CreationTimestamp
    private Date payDate;
    @ColumnDefault(value = "1")
    @Column(insertable = true)
    private Integer active;
}
