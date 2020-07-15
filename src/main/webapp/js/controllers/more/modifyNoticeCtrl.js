/**
 * 修改公告
 * 注释的代码为后期需要配合前端实现的模块
 * 1.代理商搜索下拉框
 * 2.根据该下拉框动态显示业务产品表
 */
angular.module('inspinia',['angularFileUpload']).controller("modifyNoticeCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
	$scope.agentBusiness = [{text:"全部",value:'0'},{text:"指定一级代理商下（包括下级发展）的商户",value:'1'}];
	$scope.team = [{text:'全部',value:'0'},{text:'直营',value:'1'},{text:'非直营',value:'2'}];
	$scope.isAll = [{text:'所有代理商',value:'1'},{text:'所有一级代理商',value:'2'}];
	$scope.baseInfo = {agentBusiness:'0',team:'0',isAll:'1'};
	$scope.agent = [];			//所有的一级代理商
	$scope.customerData = [];	//所有的业务产品
	$scope.fileFlagHide = false;	//“附件”，修改公告时，显示“点击下载”
	$scope.imgFlagHide = false;
	var uploadFileFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
	var uploadImgFlag = true;	
	var removeFileFlag = false;	//是否删除附件，默认为false
	var removeImgFlag = false; 		//是否删除图片
	var titleImgFlagHide = false;
    $scope.oemListResult=false;
    $scope.oemTypes=[];//缓存所有指定组织
    $scope.oemListes=[];//超级盟主品牌列表

	$http.get('notice/selectInfoById/'+$stateParams.id).success(function(msg){
		$scope.noticeInfo = msg.notice;
		$scope.products = msg.products;	//页面上应该选中状态的业务产品
		$scope.customerData = msg.allProduct;
        $scope.oemTypes=msg.oemTypes;
        $scope.oemListes=msg.oemListes;

		$scope.agent.push({
			value : '0',
			text : '全部'
		});
		for(var i=0; i<msg.allAgent.length; i++){
			$scope.agent.push({value:msg.allAgent[i].agentNo,text:msg.allAgent[i].agentName});
		}
		//如果附件为空，则隐藏
		if(msg.notice.attachment == null){
			$scope.fileFlagHide = true;
		}
		//如果消息图片为空，则隐藏
		if(msg.notice.messageImg == null){
			$scope.imgFlagHide = true;
		}
		if(msg.notice.titleImg == null){
			$scope.titleImgFlagHide = true;
		}
		if(msg.notice.agentNo == '0'){
			$scope.baseInfo.agentBusiness = '0';
		}
		if(msg.notice.agentNo != '0'){
			$scope.baseInfo.agentBusiness = '1';
		}
		if(msg.notice.receiveType == '1'){
			$scope.baseInfo.isAll = '1';
		}
		if(msg.notice.receiveType == '2'){
			$scope.baseInfo.isAll = '2';
		}
        if(msg.notice.oemType.indexOf("11")!=-1){
            $scope.oemListResult=true;
		}
	});
	//业务产品与代理商之间的切换
	$scope.getAgent = function(){
        if($scope.baseInfo.agentBusiness == '0'){
        	$scope.noticeInfo.agentNo = '0';
        	$http.get('agentInfo/selectProductById/' + $scope.noticeInfo.agentNo).success(function(data){
    			$scope.customerData = data;
    	    }).error(function(){
    	    });
        }
	};
	
//  根据代理商ID,返回其包含的所有业务产品	notice.agentNo 对应 agent_info里面的agent_no
	$scope.getBpList = function(){
		$http.get('agentInfo/selectProductById/' + $scope.noticeInfo.agentNo).success(function(data){
			$scope.customerData = [];
			$scope.customerData = data;
	    }).error(function(){
	    });
	}
	
	
	// 上传附件,定义控制器路径
	var uploaderFile = $scope.uploaderFile = new FileUploader({
		url : 'upload/fileUpload.do',
		queueLimit: 1,   //文件个数 
		removeAfterUpload: true,  //上传后删除文件
	    headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	// 过滤长度，只能上传一个
	uploaderFile.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploaderFile.filters.push({name : 'imageFilter',fn : function(
		item /* {File|FileLikeObject} */,options) {
			var name = '|'+ item.name.slice(item.name.lastIndexOf('.') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|doc|docx|pdf|ppt|xls|xlsx|'.indexOf(name) !== -1;
		}
	});
	uploaderFile.onAfterAddingFile = function(fileItem) {
		uploadFileFlag = false;
		$scope.fileFlagHide = true;
	}
	uploaderFile.removeFromQueue = function(value){
		uploadFileFlag = true;
		removeFileFlag = false;
		if($scope.noticeInfo.attachment != null){
			$scope.fileFlagHide = false;
		}
		
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}

	//删除附件的点击事件，removeFileFlag为true表示数据提交之前清除附件的数据
	//并隐藏附件
	$scope.removeAnnex = function(){
		removeFileFlag = true;
		$scope.fileFlagHide = true;
	}
	//删除图片
	$scope.removeImg = function(){
		removeImgFlag = true;
		$scope.imgFlagHide = true;
	}
	//删除图片标题
	$scope.removeTitleImg = function(){
		delete $scope.noticeInfo.titleImg;
		$scope.titleImgFlagHide = true;
	}

	//上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数 
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
			if(item.size>500*1024){
				return false;
			}
            return this.queue.length < 1;
        }
    });
    //过滤格式
     uploaderImg.filters.push({
         name: 'imageFilter',
         fn: function(item /*{File|FileLikeObject}*/, options) {
             var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
             return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
         }
     });
     uploaderImg.onAfterAddingFile = function(fileItem) {
    	 uploadImgFlag = false;
    	 $scope.imgFlagHide = true;
 	}
 	uploaderImg.removeFromQueue = function(value){
 		uploadImgFlag = true;
 		removeImgFlag = false;
 		if($scope.noticeInfo.messageImg != null){
			$scope.imgFlagHide = false;
		}
 		var index = this.getIndexOfItem(value);
         var item = this.queue[index];
         if (item.isUploading) item.cancel();
         this.queue.splice(index, 1);
         item._destroy();
         this.progress = this._getTotalProgress();
 	}

	/***
	 * 上传图片标题-start
	 */
	var uploaderTitleImgFlag = "false";
	var uploaderTitleImg = $scope.uploaderTitleImg = new FileUploader({
		url: 'upload/fileUpload.do',
		queueLimit: 1,   //文件个数
		removeAfterUpload: true,  //上传后删除文件
		headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	//过滤长度，只能上传一个
	uploaderTitleImg.filters.push({
		name: 'imageFilter',
		fn: function(item, options) {
			if(item.size>500*1024){
				return false;
			}
			return this.queue.length < 1;
		}
	});
	//过滤格式
	uploaderTitleImg.filters.push({
		name: 'imageFilter',
		fn: function(item /*{File|FileLikeObject}*/, options) {
			var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});
	uploaderTitleImg.onAfterAddingFile = function(fileItem) {
		uploaderTitleImgFlag = "uploading";
	};

	uploaderTitleImg.removeFromQueue = function(value){
		uploaderTitleImgFlag = "true";
		var index = this.getIndexOfItem(value);
		var item = this.queue[index];
		if (item.isUploading) item.cancel();
		this.queue.splice(index, 1);
		item._destroy();
		this.progress = this._getTotalProgress();
	};
	/***
	 * 上传图片标题-end
	 */

	//业务产品表格
	$scope.productsGrid = { // 配置表格
		data : 'customerData',
		enableHorizontalScrollbar : 0, // 去掉滚动条
		enableVerticalScrollbar : 1, // 去掉滚动条
		columnDefs : [ // 表格数据
		{
			field : 'teamName',
			displayName : '所属组织'
		},{
			field : 'bpName',
			displayName : '业务产品名称'
		},{
			field : 'remark',
			displayName : '业务产品说明'
		}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiProduct = gridApi;
		},
		isRowSelectable: function(row){ // 选中行 
			if($scope.products != null){
				for(var i=0;i<$scope.products.length;i++){
					if(row.entity.bpId==$scope.products[i].bpId){
						row.grid.api.selection.selectRow(row.entity); 
					}
				}
			}
		}
	};

    //业务产品表格
    $scope.productsGrid2 = { // 配置表格
        data : 'customerData',
        enableHorizontalScrollbar : 0, // 去掉滚动条
        enableVerticalScrollbar : 1, // 去掉滚动条
        columnDefs : [ // 表格数据
            {
                field : 'teamName',
                displayName : '所属组织'
            },{
                field : 'bpName',
                displayName : '业务产品名称'
            },{
                field : 'remark',
                displayName : '业务产品说明'
            }],
        onRegisterApi : function(gridApi) {
            $scope.gridApiProduct2 = gridApi;
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.products != null){
                for(var i=0;i<$scope.products.length;i++){
                    if(row.entity.bpId==$scope.products[i].bpId){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };
	
	 $scope.submit = function(){
		 $scope.submitting = true;
		 if(removeFileFlag){	//如果清除附件，设置附件地址为null
	    		delete $scope.noticeInfo.attachment;
	    	}
	    	if(removeImgFlag){
	    		delete $scope.noticeInfo.messageImg;
	    	}
		 //1.没有需要上传的
		 if(uploadFileFlag && uploadImgFlag){
			 $scope.submitData();
		 }
		 //2.只有消息图片需要上传
		 else if(uploadFileFlag && !uploadImgFlag){
			 uploaderImg.uploadAll();// 上传宣传图片
			 uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.noticeInfo.messageImg = response.url;
					$scope.submitData();
				}
	        }
		 }
		 //3.只有附件需要上传
		 else if(!uploadFileFlag && uploadImgFlag){
			 uploaderFile.uploadAll();
			 uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { 
					$scope.noticeInfo.attachment = response.url;
					$scope.submitData();
				}
	        }
		 }
		 //4.消息图片和附件都需要上传
		 else if(!uploadFileFlag && !uploadImgFlag){
			 uploaderFile.uploadAll();// 上传宣传图片
				uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
					if (response.url != null) { // 回调参数url
						$scope.noticeInfo.messageImg = response.url;
						uploaderImg.uploadAll();
						 uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
							if (response.url != null) { 
								$scope.noticeInfo.attachment = response.url;
								$scope.submitData();
							}
				        }
					}
		        }
		 }
	 }

    $scope.oemTypeCheck= function(){
        $scope.oemListResult=false;
        $("input:checkbox[name='oemtypecheck']:checked").each(function() { // 遍历多选框
            if($(this).val()==11){
                $scope.oemListResult=true;
            }
        });
    }
    $scope.oemListCheckAll = function(oemListCheckFlag){
        if(oemListCheckFlag){
            angular.forEach($scope.oemListes,function(data){
                data.checkedState = 1;
            })
        } else {
            angular.forEach($scope.oemListes,function(data){
                data.checkedState = 0;
            })
        }
    }

    $scope.submitData = function(){
		if(uploaderTitleImgFlag==="uploading"){
			console.log("开始上传图片标题");
			uploaderTitleImg.uploadAll();//开始上传图片标题
			uploaderTitleImg.onSuccessItem = function(fileItem, response, status, headers) {
				console.info('onSuccessItem', fileItem, response, status, headers);
				$scope.noticeInfo.titleImg = response.url;
				uploaderTitleImgFlag = "true";
				console.log("图片标题上传完成");
				$scope.submitData();
			};
			return;
		}
        $scope.submitting = true;
    	$scope.noticeInfo.content= $('.note-editable').html();
        var obj;
        var data;
        if($scope.noticeInfo.sysType=="1"){
            obj = $scope.gridApiProduct;
            data = {"notice":$scope.noticeInfo,"baseInfo":$scope.baseInfo,"products":obj.selection.getSelectedRows()};
        }else{
            var str="";
            $("input:checkbox[name='oemtypecheck']:checked").each(function() { // 遍历多选框
                console.log($(this).val());  // 每一个被选中项的值
                if(""==str){
                    str=str+$(this).val();
                }else{
                    str=str+","+$(this).val();
                }
            });
            if(""==str){
                $scope.notice("指定组织至少选择一个");
                $scope.submitting = false;
                return;
            }
            $scope.noticeInfo.oemType=str;
            str=""
            $("input:checkbox[name='oemlistcheck']:checked").each(function() { // 遍历多选框
                console.log($(this).val());  // 每一个被选中项的值
                if(""==str){
                    str=str+$(this).val();
                }else{
                    str=str+","+$(this).val();
                }
            });
            $scope.noticeInfo.oemList=str;
            data = {"notice":$scope.noticeInfo,"baseInfo":$scope.baseInfo};

        }

        if($scope.noticeInfo.showStatus=="1"){
            if(!($scope.noticeInfo.validBeginTime&& $scope.noticeInfo.validEndTime)){
                $scope.notice("有效时间不能为空");
                $scope.submitting = false;
                return;
            }
            if($scope.noticeInfo.validBeginTime> $scope.noticeInfo.validEndTime){
                $scope.notice("开始时间不能大于结束时间");
                $scope.submitting = false;
                return;
            }
        }
        console.log("修改通告");
        console.log(angular.toJson(data));
    	$http.post("notice/saveNotice.do",angular.toJson(data))
		.success(function(msg){
			$scope.notice(msg.msg);
			if(msg.status){
				$state.transitionTo('sys.queryNotice',null,{reload:true});
			}
			$scope.submitting = false;
		}).error(function(){
			$scope.submitting = false;
		});
    }
});

