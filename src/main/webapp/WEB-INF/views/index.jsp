<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF8">
<title>첨부파일 확장자 체크 테스트페이지</title>
<!-- jQuery 스크립트 -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<!-- 확장자 체크관련 js파일-->
<script src="/index.js"></script>
<!-- 부트스트랩-->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.2/css/bootstrap.min.css">
</head>
<body>
<div class="container">
	<h1>◎파일 확장자 차단</h1>
	<span>파일확장자에 따라 특정 형식의 파일을 첨부하거나 전송하지 못하도록 제한</span>
	<form id="extensionForm" onsubmit="fn_custom(); return false;">
		<table>
				<tbody>
					<tr>
						<th style="width: 100px;">고정확장자</th>
						<td>
							<input type="checkbox" name="fileOpt" id="fixExtension1" value="Y" data-extension="bat"> bat 
							<input type="checkbox" name="fileOpt" id="fixExtension2" value="Y" data-extension="cmd"> cmd 
							<input type="checkbox" name="fileOpt" id="fixExtension3" value="Y" data-extension="com"> com 
							<input type="checkbox" name="fileOpt" id="fixExtension4" value="Y" data-extension="cpl"> cpl 
							<input type="checkbox" name="fileOpt" id="fixExtension5" value="Y" data-extension="exe"> exe 
							<input type="checkbox" name="fileOpt" id="fixExtension6" value="Y" data-extension="scr"> scr
							<input type="checkbox" name="fileOpt" id="fixExtension7" value="Y" data-extension="js"> js
						</td>
					</tr>
					<tr>
						<th style="width: 100px;">커스텀 확장자</th>
						<td>
							<input type="text" name="custom" id="custom" placeholder="확장자 입력" value="" maxlength="20"> <input type="button" class="btn btn-primary btn-xs" id="customBtn" onclick="fn_custom();" value="+추가">
						</td>
					</tr>
					<tr>
						<th style="width: 100px;"></th>
						<td>
							<div style="border: 1px solid black; border-radius: 15px;">
								<div style="margin: 3px 3px 0px 3px"><span id="customCount">${customCount}</span>/200</div>
								<div id="customListDiv" style="margin-top: 5px;">
									<span style="background-color: white; border: 1px solid black; border-radius: 25px;" >adasdas
										<button class="btn btn-danger btn-xs" style="background-color: white; border: none; border-radius: 30px; color:red;">X</button>
									</span>
								</div>
							</div>
						</td>
					</tr>
				</tbody>
		</table>
	</form>
	<div>
		<h2>파일확장자 테스트</h2>
		<form action="/file/upload" method="POST" enctype="multipart/form-data">
			<div >
				<input type="file" name="fileInput"><button class="btn btn-primary" style="margin-top: 20px;" type="submit">제출하기</button>
			</div>
		</form>
		<div>
			<h3>True값이면 사용가능한 전송 or 업로드 가능한 파일</h3>
			<p>
				${extensionCheck}
			</p>
		</div>
		
	</div>
</div>
</body>
</html>