package com.test.flow.file.service;

import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface FileService {

	Map<String,Object> selectFileConfig() throws SQLException;

	int insertCustomExtension(Map<String, Object> payload) throws SQLException;

	int selectCountOfCustomExtensionByCustomCode(Map<String, Object> map) throws SQLException;

	List<Map<String, Object>> selectCustomExtensionList(Map<String, Object> map) throws SQLException;

	int deleteCustomExtension(Map<String, Object> payload) throws SQLException;

	int checkCustomExtension(Map<String, Object> payload) throws SQLException;

	int updateFixExtensionByCustomCode(Map<String, Object> payload) throws SQLException;

	LinkedHashMap<String, String> selectFixExtensionFlags(Map<String, Object> map) throws SQLException;

}
