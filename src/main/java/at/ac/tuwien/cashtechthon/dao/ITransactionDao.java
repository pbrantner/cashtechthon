package at.ac.tuwien.cashtechthon.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.cashtechthon.domain.Transaction;

public interface ITransactionDao extends JpaRepository<Transaction, Long> {

}
