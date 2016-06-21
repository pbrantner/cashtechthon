package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Classification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClassificationDao extends JpaRepository<Classification, Long> {

}
