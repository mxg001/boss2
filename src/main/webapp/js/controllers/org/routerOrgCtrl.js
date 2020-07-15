/**
 * 集群中收单商户
 */
angular.module('inspinia').controller("routerOrgCtrl", function($scope, $http, $state, $stateParams, i18nService, SweetAlert,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');
	$scope.quotaStatus = [{text:"全部",value:0},{text:"是",value:1},{text:"否",value:2}];
	$scope.info = {quota_status:0,acqMerchantType:-1};
	$scope.deleteText="";
	$scope.query = function() {
		$http.post('routerOrg/queryRouterAcqMerchantList.do',
       		 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
       		 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(data){
			num=0;
			rowList={};
            $scope.routerOrgDate = data.result;
			$scope.routerOrgGrid.totalItems = data.totalCount;//总记录数
        }).error(function(){
        });
	};
	// $scope.query();
	
	$scope.reset = function() {
		$scope.info = {quota_status:0,acqMerchantType:-1};
	};
	
	var rowList={};
	var num=0;
	$scope.routerOrgGrid = {
	        data: 'routerOrgDate',
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
	        useExternalPagination: true,		  //开启拓展名
	        columnDefs: [
//                {field: 'id',displayName: '序号',width: 120,pinnable: false,sortable: false},
                {field: 'acq_name',displayName: '收单机构名称',width: 180,pinnable: false,sortable: false},
                {field: 'acq_merchant_no',displayName: '收单机构商户编号',width: 180,pinnable: false,sortable: false},
                {field: 'acq_merchant_name',displayName: '收单机构商户名称',width: 180,pinnable: false,sortable: false},
                {field: 'acq_merchant_type',displayName: '收单机构商户类别',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.acqMerchantTypes)},
                {field: 'group_code',displayName: '集群编号',width: 120,pinnable: false,sortable: false},
                {field: 'group_name',displayName: '集群名称',width: 300,pinnable: false,sortable: false},
                {field: 'quota',displayName: '收单限额',width: 140,pinnable: false,sortable: false},
                {field: 'quota_status',displayName: '已超额',width: 100,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.quotaStatus)},
                {field: 'last_update_time',displayName: '最后使用时间',width: 150,pinnable: false,sortable: false,cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
	            {name: 'action',displayName: '操作',width: 250,pinnedRight: true,sortable: false,editable:true,cellTemplate:
	            	"<div  class='lh30'><a ng-show='grid.appScope.hasPermit(\"routerOrg.updateQuota\")' ng-click='grid.appScope.updateConfirm(row.entity)'>修改限额</a><a ng-show='grid.appScope.hasPermit(\"routerOrg.delete\")' ng-click='grid.appScope.delConfirm(row.entity)' > | 删除</a></div>"}
	        ],
	        onRegisterApi: function(gridApi) {	//选中行配置
	            $scope.gridApi = gridApi;
	            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row,event) {
	               if(row){
	                  $scope.testRow = row.entity;
	               }
	            });
	            //全选===========sober==========
			      $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (row) {
			            if(row[0].isSelected){
			               $scope.testRow = row[0].entity;
			               for(var i=0;i<row.length;i++){
			            	   rowList[row[i].entity.id]=row[i].entity;
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
			     //===========sober==========
		         gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		         	$scope.paginationOptions.pageNo = newPage;
		         	$scope.paginationOptions.pageSize = pageSize;
		            $scope.query();
	             });
	        }
	};
	
	$scope.delConfirm = function(record) {
		var data = {groupCode:record.group_code};
		$http.post('routerOrg/selectAcqMerCount', angular.toJson(data)
		).success(function(msg){
			$('#myModal').modal('show');
			if((msg.acqMerCount-1)===0){
				$('#myModal .modal-body').html('删除后，'+record.group_code+''+record.group_name+',收单商户数量为0，你确定删除该收单商户吗？');
			}else{
				$('#myModal .modal-body').html('确定删除记录吗？');
			}
			$scope.recordId = record.id;
			$scope.oper = "delete";
		});
	};
	$scope.closeConfirm = function(record) {
		$('#myModal').modal('show');
		$('#myModal .modal-body').html('确定关闭吗？');
		
		$scope.oper = "close";
	};
	//当模态框隐藏时触发
	$('#myModal').on('hide.bs.modal', function () {
		$scope.recordId = "";
		$scope.oper = "";
	});
	$scope.confirm = function() {
		var url;
		var data;
		if($scope.oper=="delete"){
			url = "routerOrg/delRouterAcqMerchant.do";
			data = {id:$scope.recordId};
		}
		if($scope.oper=="close"){
			url = "";
			data = {};
			$('#myModal').modal('hide');
			//暂不处理
			return;
		}
		$http.post(url, angular.toJson(data)
        ).success(function(msg){
        	$('#myModal').modal('hide');
        	$scope.notice(msg.msg);
        	if(msg.status)
        		$scope.query();
        }).error(function(){
        }); 
	};
	
	$scope.updateConfirm = function(record) {
		$('#updateModal').modal('show');
		$scope.updateInfo = angular.copy(record);
	};
	//当模态框隐藏时触发
	$('#updateModal').on('hide.bs.modal', function () {
		$scope.updateInfo = {};
	});
	$scope.update = function() {
		$scope.submitting = true;
		$http.post('routerOrg/updateAcqMerchantQuota.do',
       		 angular.toJson($scope.updateInfo)
        ).success(function(msg){
        	$('#updateModal').modal('hide');
        	$scope.notice(msg.msg);
        	if(msg.status)
        		$scope.query();
			$scope.submitting = false;
        }).error(function(){
			$scope.submitting = false;
        }); 
	};
	
	//====集群中收单商户批量删除==sober=======
	$scope.isShow=true;
	var list=[];
	$scope.del=function(){
		if(num<2){
			$scope.notice("删除最少选中两条");
			return;
		}
		list=[];
		var mm=0;
		for(index in rowList){
			list[mm]=rowList[index];
			mm++;
		}
		$http.post("routerOrg/deleteBatchCount",angular.toJson(list))
			.success(function(data){
				$scope.isShow=true;
				$scope.deleteText="";
				for(var i=0;i<data.length;i++){
					if((data[i].acqMerCount-1)===0){
						$scope.deleteText+=''+data[i].group_code+''+data[i].group_name+',收单商户数量为0;\r\n';
					}
				}
				if($scope.deleteText==""){
					$scope.isShow=false;
				}
				$("#showModel").modal("show");
			});
	}

	$scope.deleteOp=function(){
		$http.post("routerOrg/deleteBatch",angular.toJson(list))
			.success(function(datas){
				if(datas.bols){
					$(".modal-backdrop").remove();
					$scope.notice(datas.msg);
					$state.transitionTo('org.routerOrg',null,{reload:true});
				}else{
					$scope.notice(datas.msg);
				}
				rowList={};
				num=0;
			});
	}

	$scope.closeShowModel=function(){
		$("#showModel").modal("hide");
	}
	 //收单商户导出//打开导出终端模板  tgh
	 $scope.exportInfo=function(){
		 if($scope.info.sTime>$scope.info.eTime){
				$scope.notice("起始时间不能大于结束时间");
				return;
			}
			if($scope.info.auditorId==""){
				$scope.notice("必须选择审核人员");
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
							$scope.exportInfoClick("routerOrg/exportInfo.do",{"info" : angular.toJson($scope.info)});
			            }
		        });
	 }
	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});

});