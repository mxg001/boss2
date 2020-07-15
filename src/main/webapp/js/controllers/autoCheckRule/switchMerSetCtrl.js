angular.module('inspinia').controller('switchMerSetCtrl',function(i18nService,$scope,$http,$state,$stateParams,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	

	$scope.functionNumber = $stateParams.functionNumber;
	$scope.info={merchantNo:"",merchantName:"",teamId:"",functionNumber:$scope.functionNumber};
	$scope.clear=function(){
		$scope.info={merchantNo:"",merchantName:"",teamId:"",functionNumber:$scope.functionNumber};
	}



    $scope.teamTypeStr=null;
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType=[];
        $scope.teamType.push({text:'全部',value:""});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
        $scope.teamTypeStr=angular.toJson($scope.teamType);
    });
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//根据agentNo查询代理商
	$scope.selectByParam=function(){
		
		$http.post('merFunctionManager/selectByParam.do',"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(result){
			//响应成功]
			$scope.switchSetTable.data = result.data.result;
			$scope.switchSetTable.totalItems = result.data.totalCount;
		});
	}

	$scope.exportConfig = function(){
		SweetAlert.swal({
				title: "确认导出？",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true
			},
			function (isConfirm) {
				if (isConfirm) {
					$scope.info.random = Math.random();
					var data = angular.toJson($scope.info);
					var download = "merFunctionManager/exportConfig?info="+ encodeURI(data);
					document.getElementById("download").src = download;
				}
			});
	}

	var rowList={};
	var num=0;
	
	$scope.selectByParam();
	$scope.switchSetTable = {
//			data: 'switchSetData',
			paginationPageSize: 10,
			paginationPageSizes: [10, 20, 50, 100],
			useExternalPagination: true,		  	//开启拓展名
			enableHorizontalScrollbar: 1,        //横向滚动条
			enableVerticalScrollbar : 1,  		//纵向滚动条
			columnDefs: [
	            {field: 'merchantNo', displayName: '商户编号'},
	            {field: 'merchantName', displayName: '商户名称'},
	            {field: 'teamName', displayName: '业务组织'},
	           {field: 'id', displayName: '操作',cellTemplate:
	            	'<a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.deleteMer\')" ng-click="grid.appScope.deleteById(row.entity.id)">删除</a></div>'
		        }
	        ], 
	        onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.id]=rows[i].entity;
			            	   num++;
			               }
			            }else{
			            	rowList={};
			            	num=0;
			            }
			         })
			         //单选
			         $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
			            if(row.isSelected){
			               $scope.testRow = row.entity;
			               rowList[row.entity.id]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.id];
			            	num--;
			            	if(num<0){
			            		num=0;
			            	}
			            }
			         })
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectByParam();
		          });
		      }
	
		};
	 $scope.del=function(){
		 if(num<2){
			 $scope.notice("删除最少选中两条");
			 return;
		 }
		 var list=[];
		 var mm=0;
		 for(index in rowList){
			 list[mm]=rowList[index];
			 mm++;
		 }
		 SweetAlert.swal({
	            title: "确认删除？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true 
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post("merFunctionManager/deleteInfo",angular.toJson(list))
		   	   		 	.success(function(result){
			   	   			 if(result.status){
			   	    			$scope.notice(result.msg);
			   	    			$state.transitionTo('func.switchMerSet',{"functionNumber":$scope.functionNumber,"merList":$scope.merList},{reload:true});
			   	   			 }else{
			   	   				 $scope.notice(result.msg);
			   	   			 }
			   	   			 rowList={};
			   	   			 num=0;
		   	   		 	});
		            }else{
		            	 //rowList={};
		   	   			 //num=0;
		            }
	        });
	 }
	 //删除单个
	 $scope.deleteById=function(id){
	        SweetAlert.swal({
	            title: "确认删除？",
//	            text: "",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post('merFunctionManager/deleteById?id='+id)
		   	   		 	.success(function(datas){
		            		$scope.notice(datas.msg);
		    				$scope.selectByParam();
		    			}).error(function(){
		    				$socpe.notice("删除失败");
		    			})
		            }
	        });
	    };
});