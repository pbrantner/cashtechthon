package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.dtos.GroupedClassification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IClassificationDao extends JpaRepository<Classification, Long> {
    Page<Classification> findByCustomer(Customer customer, Pageable pageable);

    @Query(value = "select new at.ac.tuwien.cashtechthon.dtos.GroupedClassification(cl.classification, count(cl.classification)) " +
            "from Classification cl where cl.customer.id = :customerId group by cl.classification")
    List<GroupedClassification> findClassificationsByCustomer(@Param("customerId") Long customerId);

    @Query(value = "select new at.ac.tuwien.cashtechthon.dtos.GroupedClassification(cl.classification, count(cl.classification)) " +
            "from Classification cl " +
            "where (select max(cls.amount) from Classification cls where cls.classification = 'Income') BETWEEN :incomeFrom AND :incomeTo " +
            "group by cl.classification")
    List<GroupedClassification> findClassificationsByAgeAndIncome(@Param("incomeFrom") BigDecimal incomeFrom, @Param("incomeTo") BigDecimal incomeTo);
}
