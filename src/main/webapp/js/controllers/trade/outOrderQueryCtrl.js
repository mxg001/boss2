/**
 * 出款订单查询
 */
angular.module('inspinia').controller('outOrderQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,SweetAlert,$log,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.settleUserTypes=[{text:"全部",value:''},{text:"商户",value:'M'},{text:"代理商",value:'A'},{text:"用户",value:'U'}];
	// $scope.sourceSystems=[{text:"全部",value:''},{text:"交易系统",value:'core'},{text:"账户系统",value:'account'},{text:"运营系统",value:'boss'}
	// 						,{text:"代理商app",value:'agentapp'},{text:"车管家系统",value:'car'},{text:"代理商web",value:"agentweb"}
     //    					,{text:"超级银行家",value:'banker'}];
	$scope.settleOrderStatusList = [{text:"全部",value:''},{text:"待确认",value:'0'},{text:"已确认",value:'1'},{text:"确认失败",value:'2'}];

	$scope.info={acqEnname:"",settleStatus:"",settleType:"",settleUserType:"",sourceSystem:"",
			sdate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
			edate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
			settleOrder:"",settleUserNo:"",mobilephone:"",sourceBatchNo:"",sourceOrderNo:"",subType:-1,settleOrderStatus:""};

	//清空
	$scope.clear=function(){
		$scope.info={acqEnname:"",settleStatus:"",settleType:"",settleUserType:"",sourceSystem:"",
				sdate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				edate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				settleOrder:"",settleUserNo:"",mobilephone:"",sourceBatchNo:"",sourceOrderNo:"",subType:-1,settleOrderStatus:""};
		$scope.changeType();
		isVerifyTime = 1;
	}

	$scope.acqOrgs=[{value:"",text:"全部"}];
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
	 .success(function(msg){
			//响应成功
	    	for(var i=0; i<msg.length; i++){
	    		$scope.acqOrgs.push({value:msg[i].acqName,text:msg[i].acqName});
	    	}
	});
	$scope.subDisable = true;

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.sourceBatchNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	$scope.query=function(){
		if ($scope.loadImg) {
			return;
		}
		var reg = /^[0-9]*$/;
		if ($scope.info.inAccName != "" && reg.test($scope.info.inAccName)) {
			$scope.notice("请输入正确的收款账户名称");
			return;
		}
		if (!($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.sourceBatchNo)) {
			if(!($scope.info.sdate && $scope.info.edate)){
				$scope.notice("创建时间不能为空!");
				return;
			}
			var stime = new Date($scope.info.sdate).getTime();
			var etime = new Date($scope.info.edate).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		$http.post("settleOrderInfoAction/getTotalNumAndTotalMoney","info="+angular.toJson($scope.info),
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	 	 .success(function(msg){
	 		 if(msg.bols){
	 			$scope.zmoney=msg.totalMoney;
	 		 }else{
	 			$scope.notice("查询汇总金额失败");
	 		 }
	 	 });
		
		$http.post("settleOrderInfoAction/selectAllInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.bols){
					$scope.gridOptions.data = msg.page.result; 
					$scope.gridOptions.totalItems = msg.page.totalCount;
					$scope.znum=msg.page.totalCount;
					$scope.subDisable = false;
				}else{
					$scope.notice("查询失败");
				}
				$scope.loadImg = false;
			})
			.error(function(msg){
				$scope.loadImg = false;
			});
	}

	setBeginTime=function(setTime){
		$scope.info.sdate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndTime=function(setTime){
		$scope.info.edate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	//$scope.query();手动查询
	
	$scope.gridOptions={                           //配置表格
		 	  paginationPageSize:10,                  //分页数量
	          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	          useExternalPagination: true,                //分页数量
		      columnDefs:[                           //表格数据
		         { field: 'settleOrder',displayName:'出款订单ID',width:180 },
		         { field: 'settleType',displayName:'来源类型',
		        	 cellFilter:"formatDropping:"+$scope.settleTypeStr,width:150 },
		         { field: 'settleUserType',displayName:'结算用户类型',width:150,
		        	 cellFilter:"formatDropping:"+angular.toJson($scope.settleUserTypes)},
		         { field: 'settleUserName',displayName:'结算用户简称',width:180 },
		         { field: 'settleUserNo',displayName:'结算用户编号',width:180 },
		         { field: 'settleAmount',displayName:'出款金额',width:180,cellFilter:"currency:''" },
		         { field: 'settleStatus',displayName:'结算状态',cellFilter:"formatDropping:"+$scope.settleStatusStr,width:150},
		         { field: 'agentNoOne',displayName:'一级代理商编号',width:180},
		         { field: 'agentNameOne',displayName:'一级代理商名称',width:180},
		         { field: 'sourceSystem',displayName:'来源系统',width:150,
		        	 cellFilter:"formatDropping:"+angular.toJson($scope.sourceSystems)},
		         { field: 'sourceBatchNo',displayName:'来源系统批次号',width:150 },
		         { field: 'settleOrderStatus',displayName:'确认出款',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.settleOrderStatusList)},
		         { field: 'sourceOrderNo',displayName:'来源订单号',width:150},
		         { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		         { field: 'id',displayName:'操作',pinnedRight:true,width:120,
		        	 cellTemplate:'<a class="checkbox" ui-sref="trade.outOrderDetail({id:row.entity.settleOrder})" target="_black">详情</a>'
		         }
		      ],
			  onRegisterApi: function(gridApi) {                
		          $scope.gridApi = gridApi;
		          gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		        	  $scope.paginationOptions.pageNo = newPage;
		        	  $scope.paginationOptions.pageSize = pageSize;
		        	  $scope.query();
		          });
		      }
		};
	
	$scope.import=function(){
		if (!($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.sourceBatchNo)) {
			if(!($scope.info.sdate && $scope.info.edate)){
				$scope.notice("创建时间不能为空!");
				return;
			}
			var stime = new Date($scope.info.sdate).getTime();
			var etime = new Date($scope.info.edate).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
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
				       	location.href="settleOrderInfoAction/exportAllInfo?info="+encodeURI(angular.toJson($scope.info));
		            }
	        });
	}
	
	//确认出款
	$scope.confimPayment = function(){
		var selectedRecordRows = $scope.gridApi.selection.getSelectedRows();
		var settleOrderList = [];
		//通用代付且未结算
		angular.forEach(selectedRecordRows,function(item){
			if(item.settleType==5 && item.settleStatus==0){
				settleOrderList[settleOrderList.length] = item.settleOrder;
			}
		});
		if(settleOrderList.length==0){
			$scope.notice('请勾选符合出款条件的记录');
			return;
		}
		 SweetAlert.swal({
	            title: "您正在执行出款操作，此操作需谨慎！您确定这些订单需要出款吗？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "提交",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$scope.subDisable = true;
		            	$scope.loadImg = true;
		            	$http.post('settleOrderInfoAction/confimPayment',angular.toJson(settleOrderList))
		            	.success(function(msg){
		            		if(msg.status){
		            			$scope.query();
		            		}
		            		$scope.notice(msg.msg);
		            		$scope.subDisable = false;
		            		$scope.loadImg = false;
		            	});
		            }
	        });
	}

	//出款类型为手工提现，结算用户类型为代理商时，显示“收款账户名称”条件
	$scope.changeType = function(){
		if(2 == $scope.info.settleType && "A" == $scope.info.settleUserType){
			$("#syncLabel").css("display", "block");
			$("#syncDiv").css("display", "block");
		}else{
			$scope.info.inAccName = "";
			$("#syncLabel").css("display", "none");
			$("#syncDiv").css("display", "none");
		}
	};

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.query();
			}
		})
	});
})