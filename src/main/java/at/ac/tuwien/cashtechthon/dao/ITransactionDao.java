package at.ac.tuwien.cashtechthon.dao;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.cashtechthon.domain.Transaction;

public interface ITransactionDao extends JpaRepository<Transaction, Long> {

    List<Transaction> findById(String Id);
    List<Transaction> findByCustomerIdAndTransactionDateBetween(Long customerId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByCustomerIdInAndTransactionDateBetween(Collection<Long> customerIds, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByTransactionDateBetween(LocalDateTime start, LocalDateTime end);
    Page<Transaction> findDistinctCompanyByCustomerId(Long customerId, Pageable pageable);
}
