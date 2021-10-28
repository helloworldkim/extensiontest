package com.test.flow.file;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.flow.file.Entity.Extension;
import com.test.flow.file.repo.ExtensionRepository;
import com.test.flow.file.type.ExtensionType;

@Controller
public class JPAController {
	
	@Value("${extension.fixList}")
	private String fixExtensionArr;
	
	private static final Logger log = LoggerFactory.getLogger(JPAController.class);

	@Autowired
	ExtensionRepository repo;
	
	@RequestMapping("/")
	public String fileMain(HttpServletRequest request, HttpServletResponse response, Model model) throws SQLException {
		Long customCount =0L;
		customCount= repo.countByFixExtension(ExtensionType.CUSTOM);
		model.addAttribute("customCount",customCount);
		
		return "jpa_index";
	}
	
	@GetMapping("/api/fix/fixExtensions")
	@ResponseBody
	public List<Extension> selectFixExtensions(HttpServletRequest request, HttpServletResponse response, Model model) throws SQLException {
		List<Extension> resultList = repo.findAllByFixExtension(ExtensionType.FIX); //플레그값이 존재하는 컬럼들을 다 불러준다
		return resultList;
	}
	
	@PostMapping("/api/fix/extension")
	@ResponseBody
	public void updateFixExtension(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Extension extension) {
		extension.setFixExtension(ExtensionType.FIX);
		repo.save(extension);
		log.info("고정확장자 INSERT완료");
		
	}
	@DeleteMapping("/api/fix/extension")
	@ResponseBody
	@Transactional //더티체킹을 위해 트랜젝션 관리를 하고있음.
	public void deleteFixExtension(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Extension extension) {
		String ex = extension.getExtension();
		repo.deleteByExtension(ex);
		log.info("고정확장자 Delete완료");
	}
	
	
	@PostMapping("/api/custom")
	@ResponseBody
	public Map<String,Object> InsertCustomExtension(HttpServletRequest request,
			HttpServletResponse response,@RequestBody Extension extension) {
		log.info("InsertCustomExtension 호출");
		Map<String,Object> result = new HashMap<>();
		String extensionArr[] = fixExtensionArr.split(",");
		for(String ex: extensionArr) {
			if(ex.equals(extension.getExtension())) {
				result.put("status", "500");
				result.put("msg", "고정확장자에서 체크해주세요");
				return result;
			}
		}
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		log.info("InsertCustomExtension 호출");
		Extension findByExtension = repo.findByExtension(extension.getExtension());
		if(findByExtension==null) {
			extension.setFixExtension(ExtensionType.CUSTOM);
			repo.save(extension);
			log.info("커스텀확장자 INSERT완료");
			result.put("status", "200");
			result.put("msg","성공");
		}
		return result;
	}
	@GetMapping("/api/custom")
	@ResponseBody
	public Map<String, Object> listCustomExtension(HttpServletRequest request, HttpServletResponse response){
		log.info("listCustomExtension 호출");
		Map<String,Object> result = new HashMap<>();
		List<Extension> resultsList = repo.findAllByFixExtension(ExtensionType.CUSTOM);
		Long resultCount = repo.countByFixExtension(ExtensionType.CUSTOM);
		result.put("list", resultsList);
		result.put("customCount", resultCount);
		return result;
	}
	@DeleteMapping("/api/custom/{id}")
	@ResponseBody
	public Map<String,Object> deleteCustomExtension(HttpServletRequest request,
			HttpServletResponse response,@PathVariable Long id){
		log.info("deleteCustomExtension 호출");
		Map<String,Object> result = new HashMap<>();
		result.put("status", "500");
		result.put("msg", "오류가 발생했습니다.");
		Optional<Extension> findById = repo.findById(id);
		if(!findById.isEmpty()) {
			repo.deleteById(id);
			result.put("status", "200");
			result.put("msg","성공");
			log.info("커스텀확장자 DELETE완료");
		}
		return result;
	}
	
}
