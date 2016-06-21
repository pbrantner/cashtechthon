package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Classification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClassificationDao extends JpaRepository<Classification, Long> {
    Page<Classification> findByCustomerId(Long customerId, Pageable pageable);
}
