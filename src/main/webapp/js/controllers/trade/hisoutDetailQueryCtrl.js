/**
 * 出款明细查询
 */
angular.module('inspinia').controller('hisoutDetailQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$log,SweetAlert,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	
	// $scope.info={outServiceId:"",id:"",status:"",transId:"",orderNo:"",acqEnname:"",settleStatus:"",settleType:"",settleUserType:"",sourceSystem:"",sdate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',edate:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
	// 		accountSerialNo:"",settleUserNo:"",mobilephone:"",sourceBatchNo:"",sourceOrderNo:""};
	$scope.settleUserTypes=[{text:"全部",value:''},{text:"商户",value:'M'},{text:"代理商",value:'A'}];//M商户；A代理商'
	// $scope.sourceSystems=[{text:"全部",value:''},{text:"交易系统",value:'core'},{text:"账户系统",value:'account'},{text:"运营系统",value:'boss'}
     //    ,{text:"代理商app",value:'agentapp'},{text:"车管家系统",value:'car'},{text:"代理商web",value:"agentweb"}
     //    ,{text:"超级银行家",value:'banker'}];
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
	//清空
	$scope.clear=function(){
		$scope.info={outServiceId:"",id:"",status:"",transId:"",acqEnname:"",orderNo:"",settleStatus:"",settleType:"",settleUserType:"",sourceSystem:"",
				sdate:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
				edate:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
				accountSerialNo:"",settleUserNo:"",mobilephone:"",sourceBatchNo:"",sourceOrderNo:"",subType:-1};
		$scope.changeType();
		isVerifyTime = 1;
	}
    $scope.clear();
	$scope.acqOrgs=[{value:"",text:"全部"}]
	$scope.outServices=[{value:"",text:"全部"}]

	var defatltCountAmount = {ckje: "0.00", cksxf: '0.00', dksxf: '0.00', sjckje: '0.00', sjcksxf: '0.00'};
	$scope.countAmount = defatltCountAmount;
	
	//收单机构
	 $http.post("acqOrgAction/selectBoxAllInfo")
 	 .success(function(msg){
 			//响应成功
 	    	for(var i=0; i<msg.length; i++){
 	    		$scope.acqOrgs.push({value:msg[i].acqName,text:msg[i].acqName});
 	    	}
 	});
	
	//收单机构
	 $http.post("outAccountService/selectBoxAllInfo")
	 .success(function(msg){
			//响应成功
	    	for(var i=0; i<msg.length; i++){
	    		$scope.outServices.push({value:msg[i].id,text:msg[i].serviceName});
	    	}
	});

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.transId) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.sdate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.edate = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
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
		if (!($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.transId)) {
			if(!($scope.info.sdate && $scope.info.edate)){
				$scope.notice("创建时间不能为空");
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
		$http.post("settleOrderInfoHistoryAction/selectOutDetailAllInfo","info="+angular.toJson($scope.info)+
				"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					if(msg.bols){
						$scope.gridOptions.data = msg.page.result; 
						$scope.gridOptions.totalItems = msg.page.totalCount;
						$scope.countAmount = msg.count;
					}else{
						$scope.notice("查询失败");
						$scope.countAmount = defatltCountAmount;
					}
					$scope.loadImg = false;
				})
				.error(function(msg){
					$scope.loadImg = false;
				});
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	
	//$scope.query();手动查询
	
	$scope.gridOptions={                           //配置表格
		 	  paginationPageSize:10,                  //分页数量
	          paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
	          useExternalPagination: true,                //分页数量
		      columnDefs:[                           //表格数据
		         { field: 'id',displayName:'出款记录ID',width:180 },
		         { field: 'accountSerialNo',displayName:'出款流水号',width:180 },
		         { field: 'transId',displayName:'来源订单号',width:150},
		         { field: 'orderNo',displayName:'交易订单号',width:150},
		         { field: 'settleStatus',displayName:'结算状态',cellFilter:"formatDropping:"+$scope.settleStatusStr,width:150},
		         { field: 'status',displayName:'出款明细状态',cellFilter:"formatDropping:"+$scope.outStatusStr,width:150},
		         { field: 'settleType',displayName:'出款类型',cellFilter:"formatDropping:"+$scope.settleTypeStr,width:150 },
		         { field: 'settleUserNo',displayName:'结算用户编号',width:180 },
		         { field: 'settleUserName',displayName:'结算用户简称',width:180 },
		         { field: 'unionpayMerNo',displayName:'银联报备商户编号',width:180 },
		         { field: 'mobile',displayName:'用户手机号',width:180 },
		         { field: 'amount',displayName:'出款金额',width:180,cellFilter:"currency:''" },
		         { field: 'feeAmount',displayName:'出款手续费',width:180,cellFilter:"currency:''" },
		         { field: 'deductionFee',displayName:'抵扣手续费',width:180,cellFilter:"currency:''" },
		         { field: 'outAmount',displayName:'实际出款金额',width:180,cellFilter:"currency:''" },
		         { field: 'actualFee',displayName:'实际出款手续费',width:180,cellFilter:"currency:''" },
		         { field: 'acqEnname',displayName:'出款通道',width:150 },
		         { field: 'outServiceName',displayName:'出款服务名称',width:150 },
		         { field: 'sourceBatchNo',displayName:'来源批次号',width:150 },
		         { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
		         { field: 'id',displayName:'操作',pinnedRight:true,width:120,
		        	 cellTemplate:'<a class="lh30" ui-sref="histrade.outDetailQueryDetail({id:row.entity.id})" target="_blank">详情</a>'
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
		if (!($scope.info.settleUserNo || $scope.info.mobilephone || $scope.info.transId)) {
			if(!($scope.info.sdate && $scope.info.edate)){
				$scope.notice("创建时间不能为空");
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
						if($scope.hasPermit('hisoutDetailQuery.showHisoutDetail')){
							$scope.info.editState=1;
						}else{
							$scope.info.editState=0;
						}
				       	location.href="settleOrderInfoHistoryAction/exportInfo?info="+encodeURI(angular.toJson($scope.info));
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
})