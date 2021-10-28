package com.test.flow.file.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.test.flow.file.Entity.Extension;
import com.test.flow.file.type.ExtensionType;

@Repository //생략가능
public interface ExtensionRepository extends JpaRepository<Extension, Long>{

	List<Extension> findAllByFixExtension(ExtensionType fix);

	void deleteByExtension(String extension);

	Long countByFixExtension(ExtensionType custom);

	Extension findByExtension(String extension);

}
