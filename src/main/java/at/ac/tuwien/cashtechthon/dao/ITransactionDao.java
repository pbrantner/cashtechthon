package at.ac.tuwien.cashtechthon.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.cashtechthon.domain.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface ITransactionDao extends JpaRepository<Transaction, Long> {
    List<Transaction> findById(String Id);
    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
}
