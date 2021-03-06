package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Classification;
import at.ac.tuwien.cashtechthon.domain.Customer;
import at.ac.tuwien.cashtechthon.dtos.GroupedClassification;
import at.ac.tuwien.cashtechthon.dtos.MonthClassification;
import at.ac.tuwien.shared.dtos.Gender;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.criteria.CriteriaQuery;
import java.math.BigDecimal;
import java.util.List;

public interface IClassificationDao extends JpaRepository<Classification, Long> {
    //FIXME remove classifications which contain 'Threshold' in classification
    Page<Classification> findByCustomer(Customer customer, Pageable pageable);

    @Query(value = "SELECT new at.ac.tuwien.cashtechthon.dtos.GroupedClassification(cl.classification, SUM(cl.amount)) " +
                   "FROM Classification cl " +
                   "WHERE cl.customer.id = :customerId " +
                   "AND cl.classification NOT LIKE '%Threshold%' " +
                   "GROUP BY cl.classification")
    List<GroupedClassification> findClassificationsByCustomer(@Param("customerId") Long customerId);

    @Query(value = "SELECT new at.ac.tuwien.cashtechthon.dtos.MonthClassification(YEAR(cl.classificationDate), MONTH(cl.classificationDate), DAY(cl.classificationDate), SUM(cl.amount)) " +
            "FROM Classification cl " +
            "WHERE cl.customer.id = :customerId " +
            "AND cl.classification = :classification " +
            "GROUP BY YEAR(cl.classificationDate), MONTH(cl.classificationDate), DAY(cl.classificationDate), classificationDate " +
            "ORDER BY cl.classificationDate")
    List<MonthClassification> findComparisonByClassificationAndCustomer(@Param("classification") String classification,
                                                                   @Param("customerId") Long customerId);

    @Query(value = "SELECT new at.ac.tuwien.cashtechthon.dtos.MonthClassification(YEAR(cl.classificationDate), MONTH(cl.classificationDate), DAY(cl.classificationDate), SUM(cl.amount)) " +
            "FROM Classification cl " +
            "JOIN cl.customer cu " +
            "WHERE (:incomeFrom IS NULL OR cu.id IN (SELECT clsc.id FROM Classification cls JOIN cls.customer clsc WHERE cls.classification = 'Income' GROUP BY clsc.id HAVING MAX(cls.amount) >= :incomeFrom)) " +
            "AND (:incomeTo IS NULL OR cu.id IN (SELECT clsc.id FROM Classification cls JOIN cls.customer clsc WHERE cls.classification = 'Income' GROUP BY clsc.id HAVING MAX(cls.amount) <= :incomeTo)) " +
            "AND (:gender IS NULL OR cu.gender = :gender) " +
            "AND (:location IS NULL OR cu.location = :location) " +
            "AND (:ageFrom IS NULL OR (YEAR(current_date) - YEAR(cu.dateOfBirth)) >= :ageFrom)" +
            "AND (:ageTill IS NULL OR (YEAR(current_date) - YEAR(cu.dateOfBirth)) <= :ageTill)" +
            "AND cu.id <> :customerId " +
            "AND cl.classification = :classification " +
            "AND cl.classification NOT LIKE '%Threshold%' " +
            "GROUP BY YEAR(cl.classificationDate), MONTH(cl.classificationDate), DAY(cl.classificationDate), cl.classificationDate " +
            "ORDER BY cl.classificationDate")
    List<MonthClassification> findComparisonByClassificationAndGroup(@Param("classification") String classification,
                                                                     @Param("customerId") Long customerId,
                                                                     @Param("incomeFrom") BigDecimal incomeFrom,
                                                                     @Param("incomeTo") BigDecimal incomeTo,
                                                                     @Param("gender") Gender gender,
                                                                     @Param("location") String location,
                                                                     @Param("ageFrom") Integer ageFrom,
                                                                     @Param("ageTill") Integer ageTill);

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
            "AND cl.classification NOT LIKE '%Threshold%' " +
            "GROUP BY cl.classification")
    List<GroupedClassification> findClassificationsByProperties(@Param("incomeFrom") BigDecimal incomeFrom,
                                                                  @Param("incomeTo") BigDecimal incomeTo,
                                                                  @Param("gender") Gender gender,
                                                                  @Param("location") String location,
                                                                  @Param("customerId") Long customerId,
                                                                  @Param("ageFrom") Integer ageFrom,
                                                                  @Param("ageTill") Integer ageTill);
}
