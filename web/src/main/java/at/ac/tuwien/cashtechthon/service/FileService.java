package at.ac.tuwien.cashtechthon.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import at.ac.tuwien.cashtechthon.dao.IFileDao;
import at.ac.tuwien.cashtechthon.domain.File;
import at.ac.tuwien.cashtechthon.dtos.FileId;

@Service
@Transactional
public class FileService implements IFileService {

	private IFileDao fileDao;
	
	@Autowired
	public FileService(IFileDao fileDao) {
		this.fileDao = fileDao;
	}
	
	@Override
	public FileId storeFile(byte[] content) {
		File fileToStore = new File();
		fileToStore.setContent(content);
		FileId fileId = new FileId();
		fileId.setFileId(fileDao.save(fileToStore).getId());
		return fileId;
	}
}
