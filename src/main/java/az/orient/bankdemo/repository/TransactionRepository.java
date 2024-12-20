package az.orient.bankdemo.repository;

import az.orient.bankdemo.entity.Account;
import az.orient.bankdemo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByDtAccountAndActive(Account dtAccount,Integer active);
}
