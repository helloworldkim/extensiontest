package com.test.flow.file.service.impl;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.flow.file.service.FileService;

@Service("fileService")
public class FileServiceImpl implements FileService {
	
	@Autowired
	FileMapper fileMapper;

	@Override
	public Map<String, Object> selectFileConfig() throws SQLException {
		return fileMapper.selectFileConfig();
	}

	@Override
	public int insertCustomExtension(Map<String, Object> payload) throws SQLException {
		return fileMapper.insertCustomExtension(payload);
	}

	@Override
	public int selectCountOfCustomExtensionByCustomCode(Map<String, Object> map) throws SQLException {
		return fileMapper.selectCountOfCustomExtensionByCustomCode(map);
	}

	@Override
	public List<Map<String, Object>> selectCustomExtensionList(Map<String,Object>map) throws SQLException {
		return fileMapper.selectCustomExtensionList(map);
	}

	@Override
	public int deleteCustomExtension(Map<String, Object> payload) throws SQLException {
		return fileMapper.deleteCustomExtension(payload);
	}

	@Override
	public int checkCustomExtension(Map<String, Object> payload) throws SQLException {
		return fileMapper.checkCustomExtension(payload);
	}

	@Override
	public int updateFixExtensionByCustomCode(Map<String, Object> payload) throws SQLException {
		return fileMapper.updateFixExtensionByCustomCode(payload);
	}

	@Override
	public LinkedHashMap<String, String> selectFixExtensionFlags(Map<String, Object> map) throws SQLException {
		return fileMapper.selectFixExtensionFlags(map);
	}

}
