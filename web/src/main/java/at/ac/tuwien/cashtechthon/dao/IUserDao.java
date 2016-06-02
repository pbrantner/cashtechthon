package at.ac.tuwien.cashtechthon.dao;

import at.ac.tuwien.cashtechthon.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUserDao extends JpaRepository<User, Long> {
}
