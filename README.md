# extensiontest
확장자 체크 로직 구현 관련 프로젝트

## 기본 요건사항

	1-1 고정확장자는 차단을 자주하는 확장자 리스트이며, default는 unCheck되어져있습니다.
	1-2 고정 확장자를 check or uncheck를 할 경우 db에 저장됩니다. 새로고침 시 유지되어야합니다.
	(아래쪽 커스텀 확장자에는 해당값이 들어가면 안된다)
	2-1 확장자 최대 입력길이는 20자리 (완)
	2-2 추가버튼 클릭시 DB 저장되며, 아래쪽 영역에 표현됩니다.(완)
	3-1 커스텀 확장자는 최대 200개까지 추가가 가능 (체크해봐야함 200개 제한은 걸어둠)
	3-2 확장자 옆 X를 클릭시 db에서 삭제한다. (삭제완)
	위 요건외에 어떤점을 고려했는지 적어주세요
	Ex) 커스텀 확장자 중복 체크 
	커스텀확장자 sh를 추가한 후 다시 sh를 추가했을 때 고려하여 개발 과 같은형태.

## 그외 고려한 사항  
	1. 확장자에 영어만 가능하도록 정규표현식으로 제한.
	2. 확장자 내용이 없는경우 추가안되도록 제한.
	3. 고정확장자인경우 커스텀확장자에 입력안되도록 제한
	4. 이미 등록된 확장자의 경우 재 추가 안되도록 제한
	5. 등록 200개 갯수제한
	6. 추가버튼+엔터키로도 커스텀 확장자 추가 등록 가능하도록 form태그로 구현. 사용자 편의성을 위해 SPA유지하도록 submit false로 제한.
확장자 테스트 되는지 확인부 추가.

## 프로젝트 구성
SPRING BOOT  
JAVA 1.8  
Mysql 8.0.23  
Tomcat 9(내장톰캣)  
====


# 테이블관계도
생각한 테이블 ERD  

![테이블관계도수정본](https://user-images.githubusercontent.com/68931285/135407557-e2b127d9-9668-46bb-9f56-47e3c266551c.png)

## 사용된 테이블 DDL  
기타 호환성을 위해 utf4mb4로 설정.

CREATE TABLE fileconfig (
	idx BIGINT auto_increment NOT null primary key,
	customCode varchar(100) not NULL COMMENT '커스텀 확장자를 조회할 수 있는 code값',
	fixExtension1 varchar(1) DEFAULT 'N' NULL COMMENT 'bat확장자 체크 0허용1제한',
	fixExtension2 varchar(1) DEFAULT 'N' NULL COMMENT 'cmd확장자 체크 0허용1제한',
	fixExtension3 varchar(3) DEFAULT 'N' NULL COMMENT 'com확장자 체크 0허용1제한',
	fixExtension4 varchar(1) DEFAULT 'N' NULL COMMENT 'cpi확장자 체크 0허용1제한',
	fixExtension5 varchar(1) DEFAULT 'N' NULL COMMENT 'exe확장자 체크 0허용1제한',
	fixExtension6 varchar(1) DEFAULT 'N' NULL COMMENT 'scr확장자 체크 0허용1제한',
	fixExtension7 varchar(1) DEFAULT 'N' NULL COMMENT 'js확장자 체크 0허용1제한',
	fixExtensions varchar(100) DEFAULT NULL COMMENT '고정확장자 리스트'
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

CREATE TABLE extension (
	idx BIGINT auto_increment NOT null primary key,
	customCode varchar(100) not null,
	customExtension varchar(100) not null
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8mb4
COLLATE=utf8mb4_general_ci;

## 기타주의사항
메뉴코드부분이 자동화 구현이 안되어있어서 최초 사용시 코드값을 넣는 형태의 insert를 필요로함.

INSERT INTO fileconfig
(idx, customCode, fixExtension1, fixExtension2, fixExtension3, fixExtension4, fixExtension5, fixExtension6, fixExtension7, fixExtensions)
VALUES(1, 'extension1', 'N', 'N', 'N', 'N', 'N', 'N', 'N', 'bat,cmd,com,cpl,exe,scr,js');
