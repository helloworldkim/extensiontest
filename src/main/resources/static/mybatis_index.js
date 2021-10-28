/*고정 확장자 체크*/
	$(function(){
		$("input[name=fileOpt]").change(function(){
			let id = this.id;
			let checkbox = $("#"+id);
			let check = checkbox.is(':checked');
			/*체크박스 체크여부 확인하고 체크했을때는 Y값(고정확장자 사용) update 아닐땐N값(사용안함)) 업데이트를 수행*/
			if(check){
				console.log("체크 된 경우");
				let extension = checkbox.data('extension');
				let useYn = checkbox.val();
				fn_fixExtensionUseYnToggle(extension,useYn);
			}else{
				console.log("체크안됬을경우");
				let extension = checkbox.data('extension');
				let useYn = 'N';
				fn_fixExtensionUseYnToggle(extension,useYn);
			}
		});
	});
	/*페이지 로딩시 고정확장자 체크박스 체크여부*/
	$(function(){
		$.ajax({
				type: "GET",
				url:"/api/fix/fixExtensions",
				contentType : 'application/json; charset=UTF-8',
				dataType:"json",
				data: "",
				cache: false,
				success:function(data){
					let fixExtensions = data;
					/* Obj to array*/
					let fixExtensionArray = Object.keys(fixExtensions).map(function (key) { 
						return [String(key), fixExtensions[key]]; 
					}); 
					/*체크박스 Y N 값에따라 체크여부 변경처리*/
					fn_fixExtensionChecked(fixExtensionArray);
				},
				fail: function(e, text){
					console.log(e,text);
				}
			});
	});
	/*페이지 로드시 커스텀 확장자 리스트 호출*/
	$(function(){
		fn_customList();
	});
	/*배열의 Y N값에따라 해당하는 id의 checked 여부를 변경시킨다.*/
	function fn_fixExtensionChecked(array){
		
		for(let i=0; i<array.length; i++){
			let inputId = array[i][0];
			let useYn = array[i][1];
			if(useYn=='Y'){
				console.log($("#"+inputId));
				let id = "#"+inputId;
				$(id).prop("checked",true);
			}else{
				let id = "#"+inputId;
				$(id).prop("checked",false);
			}
		}

	}
	/*고정 확장자 제한 사용여부 update 함수*/
	function fn_fixExtensionUseYnToggle(fixExtension, useYn){
		let data ={
			"fixExtension": fixExtension,
			"useYn" : useYn
		};
		$.ajax({
				type: "POST",
				url:"/api/fix/extension",
				contentType : 'application/json; charset=UTF-8',
				dataType:"json",
				data: JSON.stringify(data),
				cache: false,
				success:function(data){
					console.log(data);
				},
				fail: function(e, text){
					console.log(e,text);
				}
			});
	}
	/*커스텀 확장자 리스트 호출*/
	function fn_customList(){
		$.ajax({
				type: "GET",
				url:"/api/custom/customList",
				contentType : 'application/json; charset=UTF-8',
				dataType:"json",
				data: "",
				cache: false,
				success:function(data){
					let list = data.list;
					let count = data.customCount;
					fn_makeCustomList(list);
					fn_changeCustomCount(count);
				},
				fail: function(e, text){
					console.log(e,text);
				}
			});
	}
	/*커스텀 확장자 리스트 갯수 그려내는 함수*/
	function fn_changeCustomCount(count){
		let countDiv=$("#customCount");
		countDiv.text(count);
	}
	/*커스텀 확장자 리스트 그려내는 함수 수행*/
	function fn_makeCustomList(list){
		let htmlTag ="";
		let target = $("#customListDiv");
		for(let i=0; i<list.length; i++){
			const extension = list[i].customExtension;
			const idx = list[i].idx;
			htmlTag +='<span style="background-color: white; border: 1px solid black; border-radius: 25px; padding:2px; margin-left:10px">';
			htmlTag	+= extension;
			htmlTag	+= '<button type="button" class="btn btn-danger btn-xs" style="background-color: white; border: none; border-radius: 30px; color:red; margin-bottom:2px;" onClick="fn_deleteCustom('
			htmlTag += idx;
			htmlTag += ')">X</button></span>';
		};
		target.html(htmlTag);
	}
	/*커스텀 확장자 삭제*/
	function fn_deleteCustom(idx){
		let data ={"idx":idx};
		$.ajax({
				type: "POST",
				url:"/api/custom/delete",
				contentType : 'application/json; charset=UTF-8',
				dataType:"json",
				data: JSON.stringify(data),
				cache: false,
				success:function(data){
					if(data.status!="200"){
						alert(data.msg);
						return;
					}
					if(data.status=="200"){
						alert(data.msg);
						//리스트 새로호출
						fn_customList();
						return;
					}
				},
				fail: function(e, text){
					console.log(e,text);
				}
			});


	}
		/*커스텀 확장자 추가*/
		function fn_custom() {
			let frm = $("#extensionForm");
			let extension = $("#custom").val();
			let data = {
				"custom" : extension
			};

			$.ajax({
				type: "POST",
				url:"/api/custom",
				contentType : 'application/json; charset=UTF-8',
				dataType:"json",
				data: JSON.stringify(data),
				cache: false,
				success:function(data){
					if(data.status!="200"){
						alert(data.msg);
						return;
					}
					if(data.status=="200"){
						alert(data.msg);
						$("#custom").val("");
						//리스트 새로호출
						fn_customList();
						return;
					}
					
				},
				fail: function(e, text){
					console.log(e,text);
				}
			});
			return false;
		}