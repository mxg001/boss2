/**
 * 代付订单查询
 */
angular.module('inspinia', ['infinity.angular-chosen']).controller('queryTradeSettleCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.typeAll=[{text:'全部',value:""},{text:'交易',value:'1'},{text:'出款',value:'2'}]
	$scope.statusAll=[{text:'全部',value:""},{text:'成功',value:'0'},{text:'失败',value:'1'},{text:'待处理',value:'2'},{text:'处理中',value:'3'},{text:'需要重出',value:'4'}]
	$scope.acqOrgs=[{text:"全部",value:""}];
	$scope.typeStr = angular.toJson($scope.typeAll);
	$scope.settleStatusStr = angular.toJson($scope.statusAll);
	//清空
	$scope.clear=function(){
		isVerifyTime = 1;
		$scope.info={
				orderType:"",merchantNo:"",shareSettleNo:"",orderNo:"",type:"",amountMin:"",amountMax:"",zqMerchantNo:"",acqEnname:"",status:"",
				startTimeMin:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',startTimeMax:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				endTimeMin:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',endTimeMax:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
				originTimeMin:moment(new Date().getTime()-24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',originTimeMax:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
				};
	}
	$scope.clear();
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 		//响应成功
 		 if(msg==null){
 			 return;
 		 }
 	    for(var i=0; i<msg.length; i++){
 	    	$scope.acqOrgs.push({value:msg[i].acqName,text:msg[i].acqName});
 	    }
 	});

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantNo || $scope.info.shareSettleNo || $scope.info.orderNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.startTimeMin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.startTimeMax = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	$scope.query=function(){
		if ($scope.loadImg) {
			return;
		}

		var orderNo=$scope.info.orderNo;
		if(orderNo!=null&&orderNo!=""){
			if(orderNo.indexOf("，") != -1){
				orderNo=orderNo.replace(/，/ig,',');
			}
			if(orderNo.indexOf(",") != -1){
				var strs=orderNo.split(",");
				if(strs!=null&&strs.length>100){
					$scope.notice("最多只能输入100个来源单号,超出" + (strs.length-100) +  "个!");
					return;
				}
			}
		}

		if (!($scope.info.merchantNo || $scope.info.shareSettleNo || $scope.info.orderNo)) {
			if(!($scope.info.startTimeMin && $scope.info.startTimeMax)){
				$scope.notice("创建时间不能为空");
				return;
			}
			var stime = new Date($scope.info.startTimeMin).getTime();
			var etime = new Date($scope.info.startTimeMax).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("创建时间范围不能超过31天");
				return;
			}
		}
		$scope.loadImg = true;
		 $http.post("transInfoAction/getShareSettleInfo","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
	  	 .success(function(msg){
	  		$scope.loadImg = false;
	  		 if(msg.bols){
	  			$scope.gridOptions.data = msg.page.result; 
	  			$scope.gridOptions.totalItems = msg.page.totalCount;
	  			$scope.znum=msg.page.totalCount;
	  		 }else{
	  			$scope.notice("查询出错");
	  		 }
	  	}).error(function(){
	  		$scope.loadImg = false;
	  	});
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	var rowList={};
	var num=0;
	$scope.gridOptions={                           //配置表格
		 	  paginationPageSize:10,                  //分页数量
	          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	          useExternalPagination: true,                //分页数量
		      columnDefs:[                           //表格数据
		         { field: 'id',displayName:'交易流水',width:180},
		         { field: 'shareSettleNo',displayName:'代付结算订单号',width:180},
		         { field: 'orderNo',displayName:'来源单号',width:180 },
		         { field: 'merchantNo',displayName:'商户号',width:180 },
		         { field: 'type',displayName:'来源类型',cellFilter:"formatDropping:"+$scope.typeStr,width:150 },
		         { field: 'acqEnname',displayName:'收单结构',width:180 },
		         { field: 'zqMerchantNo',displayName:'收单商户号',width:180 },
		         { field: 'amount',displayName:'金额',cellFilter:"currency:''",width:180 },
				  { field: 'nPrm',displayName:'保费',cellFilter:"currency:''",width:180 },
				  { field: 'orderType',displayName:'活动类型',cellFilter:"formatDropping:"+angular.toJson($scope.orderType),width:150 },
		         { field: 'status',displayName:'状态',cellFilter:"formatDropping:"+$scope.settleStatusStr,width:150},
		         { field: 'originTime',displayName:'交易/提现时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		         { field: 'opertor',displayName:'创建人',width:150},
				  { field: 'errMsg',displayName:'原因',width:200,
					  cellTemplate:'<div>' +
					  '<div ng-show="row.entity.status==1">{{row.entity.errMsg}}</div>' +
					  '<div ng-show="row.entity.status!=1"></div>' +
					  '</div>'},
				  { field: 'startTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		         { field: 'endTime',displayName:'完成时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150}
//		         { field: 'id',displayName:'操作',pinnedRight:true,width:200,
//		        	 cellTemplate:'<div><a target="_blank" ng-show="grid.appScope.hasPermit(\'trade.detail\')" ui-sref="trade.tradeQueryDetail({id:row.entity.orderNo,val:0})">详情</a></div>'
//		         }
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
	//========批量手工结算 by sober==========================================================================
	$scope.settleButch=function(){
		debugger;
		//直接猎取到所有选中的行
		$scope.list = $scope.gridApi.selection.getSelectedRows();
		 if($scope.list.length<2){
			 $scope.notice("批量结算最少选中两条");
			 return;
		 }
		 var list=$scope.list;
		 SweetAlert.swal({
	            title: "确认批量手工结算？",
	            showCancelButton: true,
	            confirmButtonColor: "#DD6B55",
	            confirmButtonText: "确认",
	            cancelButtonText: "取消",
	            closeOnConfirm: true,
	            closeOnCancel: true 
	            },
		        function (isConfirm) {
		            if (isConfirm) {
		            	$http.post("transInfoAction/shareSettleButch",angular.toJson($scope.list))
		   	   		 	.success(function(datas){
			   	   			 if(datas.bols){
			   	   				$scope.settleMsg = datas;
			   	   				$('#settleModal').modal('show');
			   	   				$scope.query();
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
		 if (!($scope.info.merchantNo || $scope.info.shareSettleNo || $scope.info.orderNo)) {
				if(!($scope.info.startTimeMin && $scope.info.startTimeMax)){
					$scope.notice("创建时间不能为空");
					return;
				}
				var stime = new Date($scope.info.startTimeMin).getTime();
				var etime = new Date($scope.info.startTimeMax).getTime();
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
			            	var token = $("meta[name='_csrf']").attr("content");  
			        		var header = $("meta[name='_csrf_header']").attr("content");
			        		$("#token").val(token);
			        		$("#header").val(header);
					       	$("#inputInfo").val(angular.toJson($scope.info));
							 $("#saveForm").attr("action","transInfoAction/exportShareSettleInfo");
							 $("#saveForm").submit();
			            }
		        });
	 }
	//隐藏出款提示窗口
		$scope.settleModalHide = function(){
			$('#settleModal').modal('hide');
		}
})