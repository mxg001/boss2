angular.module('inspinia',['uiSwitch']).controller('switchSetCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	

	$scope.functionNumber = $stateParams.functionNumber;
	$scope.blacklist = $stateParams.blacklist;
	$scope.info={agentNo:"",agentName:"",containsLower:-1,teamId:"",functionNumber:"",blacklist:""};
	$scope.info.functionNumber = $scope.functionNumber;//添加查询条件
	$scope.info.blacklist =$scope.blacklist;//添加查询条件
	$scope.containsLowerJson=[{text:"全部",value:-1},{text:"包含",value:1},{text:"不包含",value:0}];
	$scope.clear=function(){
		$scope.info={agentNo:"",agentName:""};
	}

    $scope.teamTypeStr=null;
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType=[];
        $scope.teamType.push({text:'全部',value:""});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
        $scope.teamTypeStr=angular.toJson($scope.teamType);
        $scope.switchSetTable.columnDefs.splice($scope.switchSetTable.columnDefs.length-2,0,{field: 'teamId', displayName: '属性',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.teamTypeStr});
        if($scope.blacklist==''){
        	$scope.switchSetTable.columnDefs[3].visible = false;
        }else{
        	$scope.switchSetTable.columnDefs[3].visible = true;
        }
    });
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//根据agentNo查询代理商
	$scope.selectByParam=function(){
		
		$http.post('agentFunctionManager/selectByParam.do',"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(page){
			//响应成功]
			$scope.switchSetTable.data = page.result;
			$scope.switchSetTable.totalItems = page.totalCount;
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
					console.log("***************");
					$scope.info.random = Math.random();
					var data = angular.toJson($scope.info);
					console.log(data);
					var download = "agentFunctionManager/exportConfig?info="+ encodeURI(data);
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
	            {field: 'agentNo', displayName: '代理商编号'},
	            {field: 'agentName', displayName: '代理商名称'},
	            {field: 'containsLower', displayName: '是否包含下级',cellFilter:"formatDropping:"+angular.toJson($scope.containsLowerJson)},
	            {field: 'id', displayName: '操作',cellTemplate:
	            	'<a  ng-show="grid.appScope.hasPermit(\'func.delete\')" ng-click="grid.appScope.deleteByAgentNo(row.entity.id)">删除</a></div>'
		        }
	        ], 
	        onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.agentNo]=rows[i].entity;
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
			               rowList[row.entity.agentNo]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.agentNo];
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
		            	$http.post("agentFunctionManager/deleteInfo",angular.toJson(list))
		   	   		 	.success(function(datas){
			   	   			 if(datas.bols){
			   	    			$scope.notice(datas.msg);
			   	    			$state.transitionTo('func.switchSet',{"functionNumber":$scope.functionNumber,"blacklist":$scope.blacklist},{reload:true});
			   	   			 }else{
			   	   				 $scope.notice(datas.msg);
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
	 $scope.deleteByAgentNo=function(id){
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
		            	$http.post('agentFunctionManager/deleteByAgentNo?id='+id+'&blacklist='+$scope.blacklist)
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