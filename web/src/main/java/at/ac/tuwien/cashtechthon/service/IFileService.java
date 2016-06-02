package at.ac.tuwien.cashtechthon.service;

import at.ac.tuwien.cashtechthon.dtos.FileId;


public interface IFileService {
	FileId storeFile(byte[] content);
}
