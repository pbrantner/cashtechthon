package at.ac.tuwien.cashtechthon.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import at.ac.tuwien.cashtechthon.domain.File;

public interface IFileDao extends JpaRepository<File, Long> {

}
