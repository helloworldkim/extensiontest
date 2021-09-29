package com.test.flow.file.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.test.flow.file.service.FixExtensionVO;

@Mapper
public interface FileMapper {

	int updateCustomExtensionByIdx(Map<String, Object> payload);

	Map<String, Object> selectFileConfig();

	int insertCustomExtension(Map<String, Object> payload);

	int selectCountOfCustomExtensionByCustomCode(Map<String, Object> map);

	List<Map<String, Object>> selectCustomExtensionList(Map<String, Object> map);

	int deleteCustomExtension(Map<String, Object> payload);

	int checkCustomExtension(Map<String, Object> payload);

	int updateFixExtensionByCustomCode(Map<String, Object> payload);

	LinkedHashMap<String, String> selectFixExtensionFlags(Map<String, Object> map);

}
