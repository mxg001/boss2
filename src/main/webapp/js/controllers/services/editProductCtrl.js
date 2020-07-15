/**
 * 修改业务产品
 */
angular.module('inspinia',['angularFileUpload']).controller('editProductCtrl',function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.twoCodeHide = false;
	$scope.bpImgHide = false;
	$scope.addProductHide = false;	//修改页面，增加两个按钮，方便清除原来的二维码或者宣传图片
	$scope.type = [{text:'个人',value:'1'},{text:'个体商户',value:'2'},{text:'企业商户',value:'3'}];
	$scope.bool = [{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.isLimitHard = [ {text : '不限',value : '0'}, {text : '指定硬件产品',value : '1'} ];
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.team = [];
	$scope.products = [];
	$scope.otherProducts = [{text:"无",value:null}];
	$scope.otherOemProducts = [{text:"无",value:null}];
	$scope.hards = [];
	var uploaderTwoFlag = true;
	var uploaderImgFlag = true;
	var removeTwoFlag = false;
	var removeBpImgFlag = false;
	$scope.isUsed = false;
	$http.get('businessProductDefine/editProductCtrl/'+$stateParams.id).success(function(msg){
		for(var i=0;i<msg.linkInfo.allTeam.length;i++){
			$scope.team.push({text:msg.linkInfo.allTeam[i].teamName,value:msg.linkInfo.allTeam[i].teamId+""});
		}
		for (var i = 0; i < msg.linkInfo.otherProducts.length; i++) {
			$scope.otherProducts.push({
				text : msg.linkInfo.otherProducts[i].bpName,
				value : msg.linkInfo.otherProducts[i].bpId
			});
		}
		for (var i = 0; i < msg.linkInfo.otherOemProducts.length; i++) {
			$scope.otherOemProducts.push({
				text : msg.linkInfo.otherOemProducts[i].bpName,
				value : msg.linkInfo.otherOemProducts[i].bpId
			});
		}
		$scope.allService = msg.linkInfo.allService;	//所有的服务
		$scope.allItem = msg.linkInfo.allItem;	//所有的进件项
		$scope.allHard = msg.linkInfo.allHard;	//所有的硬件产品
		$scope.services = msg.services;	//相关的服务
		$scope.items = msg.items;	//相关的进件项
		$scope.hards = msg.hards;	//相关的硬件产品
		$scope.baseInfo = msg.product;	//业务产品的基本信息
		if(msg.product.twoCode == null){
			$scope.twoCodeHide = true;
		}
		if(msg.product.bpImg == null){
			$scope.bpImgHide = true;
		}
		$scope.changeTeam();
	});
	
	$scope.couldAgent = function(){
		if($scope.baseInfo.proxy=='0'){
			$scope.baseInfo.teamId=100010;
		}
	}
	
	//获取同组织的其他业务产品
	$scope.changeTeam = function(){
		$http({
			url:"businessProductDefine/getTeamOtherBp?teamId=" + $scope.baseInfo.teamId + "&bpId=" + $scope.baseInfo.bpId,
			method:"POST"
		}).success(function(msg){
			if(msg){
				$scope.products = msg;
			}
		})
	}
	
	$scope.servicesGrid = {
		data : 'allService',
		useExternalPagination : true, // 开启拓展名
		enableHorizontalScrollbar : 0, // 去掉水平滚动条
		enableVerticalScrollbar : 1, // grid垂直滚动条是否显示, 0-不显示
		columnDefs : [ {
			field : 'serviceName',
			displayName : '服务名称',
			width:260,
		},
		{
			field : 'serviceType',
			displayName : '服务类型',
			width:260,
			cellFilter: "formatDropping:"+angular.toJson($scope.serviceTypes)
		},
		{
			field : 'tFlag',
			displayName : 'T0T1标志',
			width:260,
			cellFilter: "formatDropping:"+angular.toJson($scope.tFlags)
		}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiService = gridApi;
		},
		isRowSelectable: function(row){ // 选中行 
			if($scope.services != null){
				for(var i=0;i<$scope.services.length;i++){
					 if(row.entity.serviceId==$scope.services[i].serviceId){
						 row.grid.api.selection.selectRow(row.entity);
					 }
				}
			}
        },
	};
	

	$scope.itemsGrid = { // 配置表格
		data : 'allItem',
		paginationPageSize : 20, // 分页数量
		paginationPageSizes : [ 10, 20, 50, 100 ], // 切换每页记录数
		useExternalPagination : true, // 分页数量
		enableHorizontalScrollbar : 0, // 去掉滚动条
		enableVerticalScrollbar : 1, // 去掉滚动条
		columnDefs : [ // 表格数据
		{
			field : 'itemName',
			displayName : '进件要求项名称',
			width:470,
			enableSorting : false
		}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiItem = gridApi;
		},
		isRowSelectable: function(row){ // 选中行 
			if($scope.items != null){
				for(var i=0;i<$scope.items.length;i++){
	        	  if(row.entity.itemId==$scope.items[i].itemId){
	        		  row.grid.api.selection.selectRow(row.entity); 
	        	  }
		        }
			}
        }
	};
	//硬件产品表格
	$scope.hardsGrid = { // 配置表格
			data : 'allHard',
			enableHorizontalScrollbar : 0, // 去掉滚动条
			enableVerticalScrollbar : 1, // 去掉滚动条
			columnDefs : [ // 表格数据
			{
				field : 'typeName',
				displayName : '种类名称'
			},{
				field : 'versionNu',
				displayName : '版本号'
			}],
			onRegisterApi : function(gridApi) {
				$scope.gridApiHard = gridApi;
			},
			isRowSelectable: function(row){ // 选中行 
				if($scope.hards != null){
					for(var i=0;i<$scope.hards.length;i++){
		         	  if(row.entity.hpId==$scope.hards[i].hpId){
		         		  row.grid.api.selection.selectRow(row.entity); 
		         	  }
			        }
				}
	         }
		};
	// 上传二维码,定义控制器路径
	var uploaderTwo = $scope.uploaderTwo = new FileUploader({
		url : 'upload/fileUpload.do',
		removeAfterUpload: true,  //上传后删除文件
	    headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	// 过滤长度，只能上传一个
	uploaderTwo.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploaderTwo.filters.push({
		name : 'imageFilter',
		fn : function(item /* {File|FileLikeObject} */,options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});
	
	// 上传宣传图片,定义控制器路径
	var uploaderImg = $scope.uploaderImg = new FileUploader({
		url : 'upload/fileUpload.do',
		removeAfterUpload: true,  //上传后删除文件
	    headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	// 过滤长度，只能上传一个
	uploaderImg.filters.push({
		name : 'imageFilter',
		fn : function(item, options) {
			return this.queue.length < 1;
		}
	});
	// 过滤格式
	uploaderImg.filters.push({
		name : 'imageFilter',
		fn : function(item /* {File|FileLikeObject} */,options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});
	//添加完新的二维码后，隐藏原来的，并将能否提交上传置为false（上传成功后才能提交）
	uploaderTwo.onAfterAddingFile = function(fileItem) {
		$scope.twoCodeHide = true;//隐藏图片
		uploaderTwoFlag = false;
		removeTwoFlag = false;
	}
	//添加完新的宣传图片后，隐藏原来的，并将能否提交上传置为false（上传成功后才能提交）
	uploaderImg.onAfterAddingFile = function(fileItem) {
		$scope.bpImgHide = true;//隐藏图片
		uploaderImgFlag = false;
		removeBpImgFlag = false;
	}
	uploaderTwo.removeFromQueue = function(value){
		if($scope.baseInfo.twoCode != null){
			$scope.twoCodeHide = false;
		}
		uploaderTwoFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	uploaderImg.removeFromQueue = function(value){
		if($scope.baseInfo.bpImg != null){
			$scope.bpImgHide = false;
		}
		uploaderImgFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	$scope.removeTwoCode = function(){
		$scope.twoCodeHide = true;
		removeTwoFlag = true;
	}
	$scope.removeImg = function(){
		$scope.bpImgHide = true;
		removeBpImgFlag = true;
	}
	$scope.subDisable = false;
	$scope.submit = function() {
		var timestamp2 = Date.parse(new Date($scope.baseInfo.saleStarttime));
		var timestamp3 = Date.parse(new Date($scope.baseInfo.saleEndtime));
    	if(timestamp2>timestamp3){
    		$scope.notice("可销售的起始时间不能大于结束时间"); 
    		return;
    	}
    	var serviceData = $scope.gridApiService.selection.getSelectedRows();
		var num = 0;
		var flag = true;
		var flag2 = true;
		//同一个业务产品，不能选择两个服务类型为POS机的！
		//同一个业务产品，不能选择两个服务类型一样的！
		if(serviceData == null || serviceData.length<2){
			flag = false;
		}
		angular.forEach(serviceData,function(sdata,index){
				if(serviceData.length >1 && flag ){
					for(var i=serviceData.length-1;i>index;i--){
						if(sdata.serviceType==serviceData[i].serviceType &&
								serviceData[i].serviceType!=10001 &&
								(sdata.tFlag==serviceData[i].tFlag || sdata.tFlag ==0 || serviceData[i].tFlag == 0)){
							flag2 = false;
							flag = false;
						}
					}
				}
		});
		if(!flag2){
			$scope.notice("同一个业务产品中，服务类型和T0T1标志，组合起来必须唯一");
			return;
		}
		$scope.subDisable = true;
		//1.二维码和宣传图片都没有
		if(uploaderTwoFlag && uploaderImgFlag){
			$scope.submitData();
		}
		//2.二维码有，宣传图片没有
		if(!uploaderTwoFlag && uploaderImgFlag){
			//有二维码等待上传
	        uploaderTwo.uploadAll();// 上传二维码
	        uploaderTwo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.twoCode = response.url;
					$scope.submitData();
				}
	        }
		}
		//3.二维码没有，宣传图片有
		if(uploaderTwoFlag && !uploaderImgFlag){
			//有上传宣传图片等待上传
			uploaderImg.uploadAll();// 上传宣传图片
			uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.bpImg = response.url;
					$scope.submitData();
				}
	        }
		}
		//4.二维码和宣传图片都有
		if(!uploaderTwoFlag && !uploaderImgFlag){
			 uploaderTwo.uploadAll();// 上传二维码
	        uploaderTwo.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.twoCode = response.url;
					 uploaderImg.uploadAll();// 上传宣传图片
					uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
						if (response.url != null) { // 回调参数url
							$scope.baseInfo.bpImg = response.url;
							$scope.submitData();
						}
					}
				}
	        }
		}
	}
	$scope.submitData = function(){
		// 获取选中的数据
		if(removeTwoFlag){
			delete $scope.baseInfo.twoCode;
		}
		if(removeBpImgFlag){
			delete $scope.baseInfo.bpImg;
		}
		if($scope.baseInfo.teamId == "100010"){
			delete $scope.baseInfo.ownBpId;
		}
		//如果没有勾选依赖硬件，不能再关联“自动开通业务产品”
		if($scope.baseInfo.relyHardware == 0){
			delete $scope.baseInfo.linkProduct;
		}
		var data = {
			"baseInfo" : $scope.baseInfo,
			"services" : $scope.gridApiService.selection.getSelectedRows(),
			"items" : $scope.gridApiItem.selection.getSelectedRows(),
			"hards" : $scope.gridApiHard.selection.getSelectedRows()
		};
		
		$http.post('businessProductDefine/saveProduct',angular.toJson(data)).success(function(msg) {
			$scope.subDisable = false;
			if (msg.status) {
				$scope.notice(msg.msg);
				$state.transitionTo('service.queryProduct',null,{reload:true});
			} else {
				$scope.notice(msg.msg);
			}
		}).error(function() {
			$scope.subDisable = false;
			$scope.notice("系统错误");
		});
	}
});