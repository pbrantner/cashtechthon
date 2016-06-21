package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.dtos.GroupedClassification;
import at.ac.tuwien.shared.dtos.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface IClassificationDao extends JpaRepository<Classification, Long> {
    Page<Classification> findByCustomer(Customer customer, Pageable pageable);

    @Query(value = "SELECT new at.ac.tuwien.cashtechthon.dtos.GroupedClassification(cl.classification, SUM(cl.amount)) " +
                   "FROM Classification cl WHERE cl.customer.id = :customerId GROUP BY cl.classification")
    List<GroupedClassification> findClassificationsByCustomer(@Param("customerId") Long customerId);

    /**
     * Query for comparison
     *
     * @param incomeFrom (optional)
     * @param incomeTo (optional)
     * @param gender (optional)
     * @param location (optional)
     * @param customerId the customer id that is currently viewed (hence, has to be excluded)
     * @return
     */
    @Query(value = "SELECT new at.ac.tuwien.cashtechthon.dtos.GroupedClassification(cl.classification, SUM(cl.amount)) " +
            "FROM Classification cl " +
            "JOIN cl.customer cu " +
            "WHERE (:incomeFrom IS NULL OR cu.id IN (SELECT clsc.id FROM Classification cls JOIN cls.customer clsc WHERE cls.classification = 'Income' GROUP BY clsc.id HAVING MAX(cls.amount) >= :incomeFrom)) " +
            "AND (:incomeTo IS NULL OR cu.id IN (SELECT clsc.id FROM Classification cls JOIN cls.customer clsc WHERE cls.classification = 'Income' GROUP BY clsc.id HAVING MAX(cls.amount) <= :incomeTo)) " +
            "AND (:gender IS NULL OR cu.gender = :gender) " +
            "AND (:location IS NULL OR cu.location = :location) " +
            "AND (:ageFrom IS NULL OR (YEAR(current_date) - YEAR(cu.dateOfBirth)) >= :ageFrom)" +
            "AND (:ageTill IS NULL OR (YEAR(current_date) - YEAR(cu.dateOfBirth)) <= :ageTill)" +
            "AND cu.id <> :customerId " +
            "GROUP BY cl.classification")
    List<GroupedClassification> findClassificationsByAgeAndIncome(@Param("incomeFrom") BigDecimal incomeFrom,
                                                                  @Param("incomeTo") BigDecimal incomeTo,
                                                                  @Param("gender") Gender gender,
                                                                  @Param("location") String location,
                                                                  @Param("customerId") Long customerId,
                                                                  @Param("ageFrom") Integer ageFrom,
                                                                  @Param("ageTill") Integer ageTill);
}
