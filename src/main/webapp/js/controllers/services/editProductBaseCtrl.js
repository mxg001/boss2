/**
 * 业务产品基础信息修改
 */
angular.module('inspinia',['angularFileUpload']).controller('editProductBaseCtrl',function($scope, $http, $state, $stateParams,FileUploader) {
	//数据源
	$scope.bpImgHide = false;
	$scope.addProductHide = false;	//修改页面，增加两个按钮，方便清除原来的二维码或者宣传图片
	$scope.type = [{text:'个人',value:'1'},{text:'个体商户',value:'2'},{text:'企业商户',value:'3'}];
	$scope.bool = [{text:'否',value:'0'},{text:'是',value:'1'}];
	$scope.isLimitHard = [ {text : '不限',value : '0'}, {text : '指定硬件产品',value : '1'} ];
	$scope.products = [];
	$scope.otherProducts = [{text:"无",value:null}];
	$scope.otherOemProducts = [{text:"无",value:null}];
	var uploaderImgFlag = true;
	var removeBpImgFlag = false;
	$scope.isUsed = false;
	$http.get('businessProductDefine/getProductBase/'+$stateParams.id).success(function(msg){
		for (var i = 0; i < msg.otherProducts.length; i++) {
			$scope.otherProducts.push({
				text : msg.otherProducts[i].bpName,
				value : msg.otherProducts[i].bpId
			});
		}
		$scope.baseInfo = msg.product;	//业务产品的基本信息
		$scope.hards = msg.hards;
		$scope.allHards = msg.allHards;
		$scope.allItem = msg.allItem;
		$scope.items = msg.bpIdItem;
	/*	for(var i=0; i<$scope.allItems.length; i++){
			if($scope.SELECTED_ITEMS != null){
				for(var j=0; j<$scope.SELECTED_ITEMS.length; j++){
					if($scope.allItems[i].itemId==$scope.SELECTED_ITEMS[j].itemId){
						$scope.allItems[i].itemContent = true;
						$scope.allItems[i].disabled = true;
						break;
					} else {
						$scope.allItems[i].itemContent = false;
					}
				}
			}
		}*/
		if(msg.product.bpImg == null){
			$scope.bpImgHide = true;
		}
	});
	
	
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
	/*$scope.itemsGrid = { // 配置表格
			data : 'allItems',
			paginationPageSize : 20, // 分页数量
			paginationPageSizes : [ 10, 20, 50, 100 ], // 切换每页记录数
			useExternalPagination : true, // 分页数量
			enableHorizontalScrollbar : 0, // 去掉滚动条
			enableVerticalScrollbar : 1, // 去掉滚动条
			columnDefs : [ // 表格数据
			  {field: 'itemContent',displayName: '',width: '30',cellTemplate:
			     '<input type="checkbox" ng-disabled="row.entity.disabled" ng-model="row.entity.itemContent" ng-checked="row.entity.itemContent"/>'},
			             
			,{
				field : 'itemName',
				width:470,
				displayName : '进件要求项名称'
			}],
			onRegisterApi : function(gridApi) {
				$scope.gridApiItem = gridApi;
			},
//			isRowSelectable: function(row){ // 选中行 
//				if($scope.SELECTED_ITEMS != null){
//					for(var i=0;i<$scope.SELECTED_ITEMS.length;i++){
//		        	  if(row.entity.itemId==$scope.SELECTED_ITEMS[i].itemId){
////		        		  row.grid.api.selection.selectRow(row.entity); 
//		        		  row.entity.disabled = true;
//		        		  row.entity.aaa = true;
//		        	  }
//			        }
//				}
//	        }
		};*/
		
	//硬件产品表格
	$scope.hardsGrid = { // 配置表格
			data : 'allHards',
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
	//添加完新的宣传图片后，隐藏原来的，并将能否提交上传置为false（上传成功后才能提交）
	uploaderImg.onAfterAddingFile = function(fileItem) {
		$scope.bpImgHide = true;//隐藏图片
		uploaderImgFlag = false;
		removeBpImgFlag = false;
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
	$scope.removeImg = function(){
		$scope.bpImgHide = true;
		removeBpImgFlag = true;
	}
	$scope.subDisable = false;
	$scope.submit = function() {
		$scope.subDisable = true;
		var timestamp2 = Date.parse(new Date($scope.baseInfo.saleStarttime));
		var timestamp3 = Date.parse(new Date($scope.baseInfo.saleEndtime));
    	if(timestamp2>timestamp3){
    		$scope.notice("可销售的起始时间不能大于结束时间"); 
    		return;
    	}
		$scope.subDisable = true;
		//1.二维码和宣传图片都没有
		if(uploaderImgFlag){
			$scope.submitData();
		}
		//2.宣传图片有
		if(!uploaderImgFlag){
			//有上传宣传图片等待上传
			uploaderImg.uploadAll();// 上传宣传图片
			uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.baseInfo.bpImg = response.url;
					$scope.submitData();
				}
	        }
		}
	}
	$scope.submitData = function(){
		// 获取选中的数据
		if(removeBpImgFlag){
			delete $scope.baseInfo.bpImg;
		}
		//如果没有勾选依赖硬件，不能再关联“自动开通业务产品”
		if($scope.baseInfo.relyHardware == 0){
			delete $scope.baseInfo.linkProduct;
		}
		
		/*for(var i=0;i<$scope.allItems.length;i++){
     	  if(row.entity.itemId==$scope.SELECTED_ITEMS[i].itemId){
      		  row.grid.api.selection.selectRow(row.entity); 
      		  row.entity.disabled = true;
      		  row.entity.aaa = true;
      	  }
	    }*/
		/*$scope.itemSelectList = [];
		var i = 0;
		angular.forEach($scope.allItems, function(itemInfo,index){
			if(itemInfo.itemContent){
				if(itemInfo.disabled != true){
					$scope.itemSelectList[i++] = (itemInfo);
				}
				
			}
		});*/
		
		var data = {
			"baseInfo" : $scope.baseInfo,
			"items" : $scope.gridApiItem.selection.getSelectedRows(),
			"hards" : $scope.gridApiHard.selection.getSelectedRows()
		};
		
		$http.post('businessProductDefine/saveProductBase',angular.toJson(data)).success(function(msg) {
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