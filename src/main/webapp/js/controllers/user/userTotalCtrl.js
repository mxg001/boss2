/**
 * 用户统计
 */

angular.module('inspinia').controller('userTotalCtrl',function($scope,$http,$state,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	$scope.info={userName:"",mobilephone:"",sTime:moment(new Date().getTime()-1*24*60*60*1000).format('YYYY-MM-DD')
			,eTime:moment(new Date().getTime()-1*24*60*60*1000).format('YYYY-MM-DD')+' 23:59:59'};
	
	$scope.clear=function(){
		$scope.info={userName:"",mobilephone:""};
	}
	
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	//查询
	$scope.selectInfo=function(){
		if($scope.info.sTime>$scope.info.eTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post(
			'user/selectAllInfo',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			//响应成功]
			$scope.gridOptions.data = result.result; 
			$scope.gridOptions.totalItems = result.totalCount;
		});
	}
	var rowList={};
	var num=0;
	$scope.selectInfo();
	 $scope.gridOptions={                           //配置表格
		      paginationPageSize:10,                  //分页数量
		      paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		      useExternalPagination: true,
			  enableHorizontalScrollbar: 1,        //横向滚动条
			  enableVerticalScrollbar : 1,  		//纵向滚动条
		      columnDefs:[                           //表格数据
		         { field: 'staticUserId',displayName:'序号',width:150,cellTemplate: "<span class='lh30'>{{rowRenderIndex + 1}}</span>"},
		         { field: 'mobilephone',displayName:'手机号',width:150},
		         { field: 'userName',displayName:'用户姓名',width:150},
		         { field: 'department',displayName:'部门' ,width:150},
		         { field: 'merchantNo',displayName:'商户号',width:150 },
		         { field: 'netCnt',displayName:'累计成功交易次数',width:150},
		         { field: 'netAmt',displayName:'累计成功交易金额',width:150,cellFilter:"currency:''" },
		         { field: 'totalCnt',displayName:'累计交易次数',width:150},
		         { field: 'totalAmt',displayName:'累计交易金额',width:150,cellFilter:"currency:''"},
		         { field: 'remark',displayName:'备注',width:150},
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          //全选
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
			            if(rows[0].isSelected){
			               $scope.testRow = rows[0].entity;
			               for(var i=0;i<rows.length;i++){
			            	   rowList[rows[i].entity.staticUserId]=rows[i].entity;
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
			               rowList[row.entity.staticUserId]=row.entity;
			               num++;
			            }else{
			            	delete rowList[row.entity.staticUserId];
			            	num--;
			            	if(num<0){
			            		num=0;
			            	}
			            }
			         })
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		          	$scope.paginationOptions.pageNo = newPage;
		          	$scope.paginationOptions.pageSize = pageSize;
		             $scope.selectInfo();
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
		            	$http.post("user/deleteInfo",angular.toJson(list))
		   	   		 	.success(function(datas){
			   	   			 if(datas.bols){
			   	    			$scope.notice(datas.msg);
			   	    			$state.transitionTo('user.userTotal',null,{reload:true});
			   	   			 }else{
			   	   				 $scope.notice(datas.msg);
			   	   			 }
			   	   			 rowList={};
			   	   			 num=0;
		   	   		 	});
		            }else{
		            	 rowList={};
		   	   			 num=0;
		            }
	        });
		 
	 }
	 //导出信息//打开导出终端模板
	 $scope.exportInfo=function(){
		 if($scope.info.sTime>$scope.info.eTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		 }
		 if($scope.info.userName.length<1){
			 $scope.notice("请输入用户名称");
			 return;
		 }
		 if($scope.info.mobilephone.length<1){
			 $scope.notice("请输入用户手机号");
			 return;
		 }
		 
		 if($scope.info.sTime==""){
			$scope.notice("起始时间不能为空");
			return;
		 }
		 if($scope.info.eTime==""){
			$scope.notice("结束时间不能为空");
			return;
		 }
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
				       	location.href="user/exportInfo.do?info="+angular.toJson($scope.info);
		            }
	        });
	 }
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfo();
			}
		})
	});
})