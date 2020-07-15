/**
 * 收款码管理
*/
angular.module('inspinia').controller('gatherCodeManagerCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.status = [{text:'未导出',value:0},{text:'已导出',value:1,},{text:'已使用',value:2},{text:'弃用',value:3}];
	$scope.statusAll = angular.copy($scope.status);
	$scope.statusAll.unshift({text:'全部',value:-1});
	$scope.resetForm = function(){
		$scope.baseInfo = {status:-1,materialType:-1,number:0};
	}
	$scope.resetForm();
	$scope.gatherCodeData = [];
	$scope.gatherCodeTable = {
		data:'gatherCodeData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 50, 100, 500],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs:[
		            {field:'number',displayName: '序号'},
		            {field:'sn',displayName:'编号'},
		            {field:'merchantNo',displayName:'商户编号'},
		            {field:'merchantName',displayName:'商户名称'},
		            {field:'materialType',displayName:'物料种类',cellFilter:'formatDropping:' + angular.toJson($scope.materialType)},
		            {field:'status',displayName:'收款码状态',cellFilter:'formatDropping:' + angular.toJson($scope.status)},
		            {field:'options',displayName:'操作',cellTemplate:
		            	'<div class="lh30"><a ui-sref="gatherCodeDetail({id:row.entity.id})">详情</a>'
		            	+'<a ng-show="grid.appScope.hasPermit(\'gatherCode.abandon\')" ng-click="grid.appScope.gatherCodeAbandon(row.entity.id)"> | 弃用</a>'
		                +'<a ng-click="grid.appScope.gatherCodeUrl(row.entity.id)"> | 收款码</a></div>'
		              
		            }
		            ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	
	//弃用
	$scope.gatherCodeAbandon = function(id){
		 SweetAlert.swal({
	            title:"确认弃用吗",
//	            text: "服务状态为关闭后，不能正常交易!",
	            type: "warning",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.get('gatherCode/gatherCodeAbandon/' + id)
		        		.success(function(msg){
		        			if(msg.status){
		        				$scope.notice(msg.msg);
		        				$scope.query();
		        			} else {
		        				$scope.notice(msg.msg);
		        			}
		        		});
		            }
	        });
		
	}
	
	//收款码
	$scope.gatherCodeUrl = function(id){
		$http.get('gatherCode/gatherCodeUrl/' + id)
		.success(function(msg){
			$scope.gatherCode = msg.msg;
			$('#gatherCodeUrlModal').modal('show');
		});
	}
	
	//查询
	$scope.query = function(){
		$http.post('gatherCode/selectByParams.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.gatherCodeData = page.result;
					$scope.gatherCodeTable.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	//生成收款码
	$scope.createGatherCodeModal = function(){
		$scope.info = {number:0,materialType:1};
		$('#createGatherCodeModal').modal('show');
	}
	$scope.createGatherCode = function(){
		$scope.submitting = true;
		if($scope.info.number<1){
			$scope.notice('数量不正确');
			$scope.submitting = false;
			return;
		}
		if($scope.info.number>100000){
			$scope.notice('每次最多能生成10万个');
			$scope.submitting = false;
			return;
		}
		$http.post('gatherCode/createGatherCode',angular.toJson($scope.info))
		.success(function(msg){
			if(msg.status){
				$scope.cancel();
				$scope.query();
			}
			$scope.notice(msg.msg);
			$scope.submitting = false;
		}).error(function(){
			$scope.submitting = false;
		})
	}
	
	//导出收款码
	$scope.exportGatherCodeModal = function(){
		$scope.info = {number:0,materialType:1,status:0};
		$('#exportGatherCodeModal').modal('show');
	}
	$scope.exportGatherCode = function(){
		$scope.submitting = true;
		if($scope.info.number<1){
			$scope.notice('数量不正确');
			$scope.submitting = false;
			return;
		}
		if($scope.info.number>100000){
			$scope.notice('每次最多能生成10万个');
			$scope.submitting = false;
			return;
		}
		$http.post('gatherCode/checkGatherNumber',angular.toJson($scope.info))
		.success(function(msg){
			if(!msg.status){
				$scope.notice('符合条件的收款码共剩余：'+msg.msg);
				$scope.submitting = false;
				return false;
			} else {
				location.href="gatherCode/exportGatherCode?number="+$scope.info.number+"&materialType="+$scope.info.materialType;
				$scope.submitting = false;
				$scope.cancel();
			}
		}).error(function(){
		})
	}
	//隐藏弹窗
	$scope.cancel = function(){
		$('#exportGatherCodeModal').modal('hide');
		$('#createGatherCodeModal').modal('hide');
		$('#gatherCodeUrlModal').modal('hide');
	}
	
});