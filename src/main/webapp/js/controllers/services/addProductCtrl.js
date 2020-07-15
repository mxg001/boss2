/**
 * 增加业务产品
 */
angular.module('inspinia', [ 'angularFileUpload' ]).controller('addProductCtrl',function($scope, $http, $state,
		$stateParams, $timeout,i18nService, FileUploader) {
	i18nService.setCurrentLang('zh-cn');
	// 数据源
	$scope.twoCodeHide = true;
	$scope.bpImgHide = true;
	$scope.bool = [ {text : '否',value : '0'}, {text : '是',value : '1'} ];
	$scope.type = [ {text : '个人',value : '1'}, {text : '个体商户',value : '2'}, {text : '企业商户',value : '3'} ];
	$scope.isLimitHard = [ {text : '不限',value : '0'}, {text : '指定硬件产品',value : '1'} ];
	$scope.tFlags=[{text:"不涉及",value:0},{text:"只允许T0",value:1},{text:"只允许T1",value:2},{text:"允许T0和T1",value:3}];
	$scope.baseInfo = {
		teamId:200010,
		bpType : '1',
		notCheck : '0',
		proxy : '1',
		limitHard:'1',
		relyHardware:0,
		saleStarttime:null,
		saleEndtime:null,
		allowWebItem:1,
		allowIndividualApply:1
	};
	$scope.products = [];
	$scope.otherProducts = [{text:"无",value:null}];
	$scope.otherOemProducts = [{text:"无",value:null}];
	$scope.team = [];
	$scope.onServices = [];
	$http.get('businessProductDefine/addProductCtrl').success(function(msg) {
		$scope.services = msg.allService;
		$scope.allHard = msg.allHard;
		$scope.allItems = msg.allItem;
		$scope.items = msg.items;
		for (var i = 0; i < msg.allTeam.length; i++) {
			$scope.team.push({
				text : msg.allTeam[i].teamName,
				value : msg.allTeam[i].teamId
			});
		}
		for (var i = 0; i < msg.otherProducts.length; i++) {
			$scope.otherProducts.push({
				text : msg.otherProducts[i].bpName,
				value : msg.otherProducts[i].bpId
			});
		}
		for (var i = 0; i < msg.otherOemProducts.length; i++) {
			$scope.otherOemProducts.push({
				text : msg.otherOemProducts[i].bpName,
				value : msg.otherOemProducts[i].bpId
			});
		}
	});
	
	
	$scope.couldAgent = function(){
		if($scope.baseInfo.proxy=='0'){
			$scope.baseInfo.teamId=100010;
		}
	}
	
	//获取同组织的其他业务产品
	$scope.changeTeam = function(){
		$http({
			url:"businessProductDefine/getTeamOtherBp?teamId=" + $scope.baseInfo.teamId,
			method:"POST"
		}).success(function(msg){
			if(msg){
				$scope.products = msg;
			}
		})
//		if($scope.baseInfo.teamId==100010){
//			$scope.products = $scope.otherProducts;
//		} else {
//			$scope.products = $scope.otherOemProducts;
//		}
	}

	$scope.servicesGrid = {
		data : 'services',
		useExternalPagination : true, // 开启拓展名
		enableHorizontalScrollbar : 0, // 去掉水平滚动条
		enableVerticalScrollbar : 1, // grid垂直滚动条是否显示, 0-不显示
		columnDefs : [ {
			field : 'serviceName',
			displayName : '服务名称',
			width:260,
			enableSorting : true
		},
		{
			field : 'serviceType',
			displayName : '服务类型',
			width:260,
			enableSorting : true,
			cellFilter: "formatDropping:"+angular.toJson($scope.serviceTypes)
		},
		{
			field : 'tFlag',
			displayName : 'T0T1标志',
			width:260,
			enableSorting : true,
			cellFilter: "formatDropping:"+angular.toJson($scope.tFlags)
		}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiService = gridApi;
		}
	};

	$scope.itemsGrid = { // 配置表格
		data : 'allItems',
		paginationPageSize : 20, // 分页数量
		paginationPageSizes : [ 10, 20, 50, 100 ], // 切换每页记录数
		useExternalPagination : true, // 分页数量
		enableHorizontalScrollbar : 0, // 去掉滚动条
		enableVerticalScrollbar : 1, // 去掉滚动条
		columnDefs : [ // 表格数据
		{
			field : 'itemName',
			width:470,
			displayName : '进件要求项名称'
		}],
		onRegisterApi : function(gridApi) {
			$scope.gridApiItem = gridApi;
		},
		isRowSelectable: function(row){ // 选中行 
			if($scope.SELECTED_ITEMS != null){
				for(var i=0;i<$scope.SELECTED_ITEMS.length;i++){
	        	  if(row.entity.itemId==$scope.SELECTED_ITEMS[i].value){
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
	uploaderTwo.filters.push({name : 'imageFilter',fn : function(
		item /* {File|FileLikeObject} */,options) {
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
	uploaderImg.filters.push({name : 'imageFilter',fn : function(
		item /* {File|FileLikeObject} */,options) {
			var type = '|'+ item.type.slice(item.type.lastIndexOf('/') + 1)+ '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});
	var uploaderTwoFlag = true;
	var uploaderImgFlag = true;
	uploaderTwo.onAfterAddingFile = function(fileItem) {
		uploaderTwoFlag = false;
	}
	uploaderImg.onAfterAddingFile = function(fileItem) {
		uploaderImgFlag = false;
	}
	uploaderTwo.removeFromQueue = function(value){
		uploaderTwoFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	uploaderImg.removeFromQueue = function(value){
		uploaderImgFlag = true;
		var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
	}
	$scope.subDisable = false;
	$scope.submit = function() {
    	if($scope.baseInfo.saleStarttime>$scope.baseInfo.saleEndtime){
    		$scope.notice("可销售的起始时间不能大于结束时间"); 
    		return;
    	}
    	var serviceData = $scope.gridApiService.selection.getSelectedRows();
		var num = 0;
		var flag = true;
		var flag2 = true;
		//同一个业务产品，不能选择两个服务类型为POS机的！
		//同一个业务产品，不能选择两个服务类型一样的(关联提现服务除外)！
		//可以有两个或以上
		if(serviceData == null || serviceData.length<2){
			flag = false;
		}
		//循环选中的所有服务，两两比较，一旦不符合，就return
		angular.forEach(serviceData,function(sdata,index){
				if(serviceData.length >1 && flag ){
					for(var i=serviceData.length-1;i>index;i--){
						if(sdata.serviceType==serviceData[i].serviceType && 
//								serviceData[i].serviceType!=10001 &&
								(sdata.tFlag==serviceData[i].tFlag || sdata.tFlag ==0 || serviceData[i].tFlag == 0)){
							$scope.notice("同一个业务产品中，服务类型和T0T1标志，组合起来必须唯一");
							flag = false;
							flag2 = false;
							return;
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
			"hards" : $scope.gridApiHard.selection.getSelectedRows(),
		};
		$http.post('businessProductDefine/saveProduct',angular.toJson(data)).success(function(msg) {
			$scope.subDisable = false;
			if (msg.status) {
				$scope.notice(msg.msg);
				$state.transitionTo('service.addProduct',null,{reload:true});
			} else {
				$scope.subDisable = false;
				$scope.notice(msg.msg);
			}
		}).error(function() {
			$scope.subDisable = false;
			$scope.notice("系统错误");
			
		});
	}

});