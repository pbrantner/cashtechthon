package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.Threshold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IThresholdDao extends JpaRepository<Threshold, Long> {

}
