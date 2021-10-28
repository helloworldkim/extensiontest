package com.test.flow.file;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.test.flow.file.service.FileService;

public class FileController {
	
	@Autowired
	FileService fileService;
	
	final private static String extensionPattern="^[a-zA-Z]*$"; 
	
	@RequestMapping("/")
	public String fileMain(HttpServletRequest request, HttpServletResponse response, Model model) throws SQLException {
		int customCount =0;
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customCode","extension1"); //임시 코드값 부여
		customCount= fileService.selectCountOfCustomExtensionByCustomCode(map);
		model.addAttribute("customCount",customCount);
		
		return "index";
	}
	@GetMapping("/api/fix/fixExtensions")
	@ResponseBody
	public Map<String,String> selectFixExtensions(HttpServletRequest request, HttpServletResponse response, Model model) throws SQLException {

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("customCode","extension1"); //임시 코드값 부여
		LinkedHashMap<String,String> fixExtensions = fileService.selectFixExtensionFlags(map);
		return fixExtensions;
	}
	@PostMapping("/api/fix/extension")
	@ResponseBody
	public Map<String, Object> updateFixextension(@RequestBody Map<String, Object> payload) throws SQLException{
		Map<String,Object> result = new HashMap<>();
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		
		/*빈값 or null체크*/
		String extension = payload.get("fixExtension").toString();
		if(extension==null || extension.equals("")) {
			result.put("status", "500");
			result.put("msg", "내용이 없습니다.");
			return result;
		}
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("customCode","extension1"); //임시 코드값 부여
		map =fileService.selectFileConfig(); 
		/*고정 확장자인지 체크*/
		String extensions[] = map.get("fixExtensions").toString().split(",");
		boolean check = FixExtensionChecker(extension, extensions);
		if(check!=true) {
		 result.put("status", "401");
		 result.put("msg", "고정확장자값이 아닙니다.");
		 return result;
		}
		/*고정값 업로드 수행*/
		String column ="";
		for(int i=0; i<extensions.length; i++) {
			StringBuffer sb = new StringBuffer();
			if(extensions[i].equals(extension)) {
				sb.append("fixExtension");
				sb.append(i+1);
			}
			column = sb.toString(); //fixextension1 ~ fixextension7
			if(!column.equals("")) {
				payload.put("fixExtension", column);
				payload.put("customCode","extension1"); //임시 코드값 부여
				fileService.updateFixExtensionByCustomCode(payload);
				result.put("status", "200");
				result.put("msg", "정상입니다.");
			}
		}
		return result;
	}
	
	@GetMapping("/api/custom/customList")
	@ResponseBody
	public Map<String, Object> selectCustomextensionList() throws SQLException{
		Map<String,Object> result = new HashMap<>();
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		
		Map<String,Object> map = new HashMap<>();
		map.put("customCode","extension1"); //임시 코드값 부여
		
		/*커스텀 확장자 리스트 조회*/
		List<Map<String,Object>> list = new ArrayList<>();
		list = fileService.selectCustomExtensionList(map);
		if(list!=null) {
			result.put("status", "200");
			result.put("msg", "정상입니다.");
		}
		/*커스텀 확장자 리스트 카운트 조회*/
		int customCount = fileService.selectCountOfCustomExtensionByCustomCode(map);
		result.put("list", list);
		result.put("customCount", customCount);
		return result;
	}
	
	@PostMapping("/api/custom")
	@ResponseBody
	public Map<String,String> fileInputCustomextension(@RequestBody Map<String, Object> payload,HttpServletRequest request, HttpServletResponse response) throws SQLException {
		Map<String,String> result = new HashMap<String, String>();
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		
		/*빈값 or null체크*/
		String extension = payload.get("custom").toString();
		if(extension==null || extension.equals("")) {
			result.put("status", "500");
			result.put("msg", "내용이 없습니다.");
			return result;
		}
		/*백단 유효성 체크 확장자 조건 영어만들어간다. (.,특수문자 다 거른다)*/
		if(!extension.matches(extensionPattern)) {
			result.put("status", "401");
			result.put("msg", "영어만 입력가능합니다");
			return result;
		}
		if((extension.length()>20)) {
			result.put("status", "401");
			result.put("msg", "20자 이상 입력하실수 없습니다.");
			return result;
		}

		Map<String,Object> map = new HashMap<String, Object>();
		map =fileService.selectFileConfig(); 
			  
		/*고정 확장자인지 체크*/
		String extensions[] = map.get("fixExtensions").toString().split(",");
		boolean check = FixExtensionChecker(extension, extensions);
		if(check==true) {
		 result.put("status", "401");
		 result.put("msg", "고정확장자에서 체크해주세요");
		 return result;
		}
		int cnt =0;
		/*이미 등록된 확장자인지 체크*/
		payload.put("customCode","extension1"); //임시 코드값 부여
		payload.put("custom", extension);
		cnt = fileService.checkCustomExtension(payload);
		if(cnt!=0) {
		 result.put("status", "401");
		 result.put("msg", "이미 등록된 확장자입니다");
		 return result;
		}
		/*200개 최대 등록체크*/
		cnt=0;
		cnt = fileService.selectCountOfCustomExtensionByCustomCode(payload);
		if(cnt>=200) {
		 result.put("status", "401");
		 result.put("msg", "최대 200개 까지 추가 가능합니다");
		 return result;
		}
		/*insert로직 수행*/
		cnt=0;
		cnt =fileService.insertCustomExtension(payload);
		if(cnt!=0) {
			result.put("status", "200");
			result.put("msg", "추가되었습니다.");
		}
		return result;
	}
	

	@PostMapping("/api/custom/delete")
	@ResponseBody
	public Map<String,String> fileDeleteCustomextension(@RequestBody Map<String, Object> payload,HttpServletRequest request, HttpServletResponse response) throws SQLException {
		Map<String,String> result = new HashMap<String, String>();
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		
		String idx = payload.get("idx").toString();
		if(idx==null || idx.equals("")) {
			result.put("status", "401");
			result.put("msg", "삭제중 오류가 발생했습니다.");
			return result;
		}
		
		int cnt =0;
		//삭제수행
		cnt = fileService.deleteCustomExtension(payload);
		if(cnt!=0) {
			result.put("status", "200");
			result.put("msg", "삭제되었습니다.");
		}
		
		return result;
	}
	
	@PostMapping("/file/upload")
	public String fileUploadCheck(MultipartHttpServletRequest multiRequest,Model model) throws SQLException {
		Map<String, MultipartFile> files = multiRequest.getFileMap();
		//System.out.println(files.get("fileInput").getName());
		String fileName = files.get("fileInput").getOriginalFilename();
		int extensionIndex = fileName.lastIndexOf(".");//마지막.이 찍힌곳을 기준으로 확장자를 가져온다.
		String extension = fileName.substring(extensionIndex+1); //.을 제외한 값을 가져온다
		
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("customCode","extension1"); //임시 코드값 부여
		map =fileService.selectFileConfig(); 
		String extensions[] = map.get("fixExtensions").toString().split(",");
		/*고정확장자인지 체크*/
		boolean check = FixExtensionChecker(extension,extensions);
		/*고정값 체크 Y,N값 체크 수행*/
		if(check) {
			String column ="";
			for(int i=0; i<extensions.length; i++) {
				StringBuffer sb = new StringBuffer();
				if(extensions[i].equals(extension)) {
					sb.append("fixExtension");
					sb.append(i+1);
				}
				column = sb.toString(); //fixextension1 ~ fixextension7
				if(!column.equals("")) {
					map.put("customCode","extension1"); //임시 코드값 부여
					LinkedHashMap<String,String> fixExtensions = fileService.selectFixExtensionFlags(map);
					String useYn = fixExtensions.get(column);
					if(useYn.equals("Y")) {
						check=false;
						model.addAttribute("extensionCheck",check);
						return "index";
					}
				}
			}
		}
		map = new HashMap<String, Object>();
		map.put("customCode","extension1"); //임시 코드값 부여
		map.put("custom",extension); //임시 코드값 부여
		int cnt=0;
		//2번은 커스텀 확장자인지
		cnt = fileService.checkCustomExtension(map);
		if(cnt!=0) {
			check=false;
			model.addAttribute("extensionCheck",check);
			return "index";
		}else {
			check=true;
			model.addAttribute("extensionCheck",check);
		}
		//그외 실제로 파일업로드 로직 수행
		return "index";
	}
	/**
	 * 고정확장자 리스트를 조회하여 
	 * 현재 확장자와 고정확장자 리스트에 존재하는 확장자가 맞는지 그 결과값을
	 * boolean값으로 돌려준다.
	 * @param extension
	 * @param extensions
	 * @return 고정확장자면 true, 아니면 false값을 return한다
	 */
	public boolean FixExtensionChecker(String extension,String[] extensions){
		boolean check = false;
		for(String ex: extensions) {
			 if(ex.equals(extension)){
			  check=true;
			 }
		}
		return check;
	}

}
