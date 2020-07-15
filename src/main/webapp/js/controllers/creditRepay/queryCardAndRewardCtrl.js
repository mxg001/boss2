/**
 */

angular.module('inspinia', ['infinity.angular-chosen']).controller('queryCardAndRewardCtrl',function($scope,$http,$state,$timeout,$stateParams,$compile,$filter,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	//数据源
	
	$scope.riskStatusaa=[{text:"全部",value:""},{text:"已赠送",value:1},{text:"赠送失败",value:2},{text:"未赠送",value:3}]
	$scope.statusaa=[{text:"全部",value:""},{text:"成功订单",value:5},{text:"失败订单",value:-5}]
	$scope.givenTypeaa=[{text:"全部",value:""},{text:"鼓励金",value:1},{text:"积分",value:2},{text:"刷卡金",value:3}]
	$scope.givenTypeall=[{text:"鼓励金",value:1},{text:"积分",value:2},{text:"刷卡金",value:3}]

	$scope.orderStatus=[{text:"信用卡办理",value:2},{text:"贷款",value:6}]
	
	
	$scope.sendTypeSwitch = null;
	$scope.sendTypeSingleId = null;
	$scope.sendTypeSelect = "";

	//获取所有赠送渠道
    $scope.sendChannelList=[];
	    $http.post("sysDict/getListByKey.do?sysKey=SENDCHANNEL")
	        .success(function(data){
	            //响应成功
	            for(var i=0; i<data.length; i++){
	                $scope.sendChannelList.push({value:data[i].sysValue,text:data[i].sysName});
	            }
	        });
	//获取所有赠送类型
	$scope.sendTypeList=[];
	$http.get('cardAndReward/getSendTypeList?type=12')
		.success(function(data){
			  //响应成功
            for(var i=0; i<data.list.length; i++){
                $scope.sendTypeList.push({value:data.list[i].id+"",text:"鼓励金金额"+data.list[i].couponAmount+",有效期"+data.list[i].effectiveDays+"天"});
            }
            $scope.info.sendTypeId=data.sendTypeId+"";
            $scope.sendTypeSelect = data.sendTypeId+"";
		});    
	
	//清空
	$scope.clear=function(){
		$scope.info={
				status:"",givenStatus:"",givenType:""
		};
		$scope.cityShow=false;
		isVerifyTime = 1;
	}
	$scope.clear();

	//查询
	$scope.selectInfo=function(){
		if($scope.loadImg){
			return;
		}
		$scope.loadImg = true;
		$scope.info.orderType=2;
		$http({
	            url: 'cardAndReward/selectUserInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
	            data: $scope.info,
	            method:'POST'
	        }).success(function (msg) {
	            $scope.submitting = false;
	            $scope.loadImg = false;
	            if (!msg.status){
	                $scope.notice(msg.msg);
	                return;
	            }
	            $scope.gridOptions.data = msg.page.result;
	            $scope.gridOptions.totalItems = msg.page.totalCount;
	        }).error(function (msg) {
	            $scope.submitting = false;
	            $scope.loadImg = false;
				$scope.notice("系统异常!");
	        });
		
	}

	$scope.paginationOptions=angular.copy($scope.paginationOptions);

	$scope.gridOptions={                           //配置表格
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,
		enableHorizontalScrollbar: 1,        //横向滚动条
		enableVerticalScrollbar : 1,  		//纵向滚动条
		columnDefs:[                           //表格数据
		    { field: 'id',displayName:'编号',width:150},
			{ field: 'username',displayName:'商户名称',width:150},
			{ field: 'phone',displayName:'手机号码',width:150},
			{ field: 'orgName',displayName:'机构名称',width:150},
			{ field: 'mechName',displayName:'组织名称',width:150},
			{ field: 'orderType',displayName:'订单类型',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.orderStatus)},
			{ field: 'orderNo',displayName:'订单号',width:150},
			{ field: 'statusName',displayName:'订单状态',width:150},
			{ field: 'transAmount',displayName:'交易金额',width:150},
			{field: 'transTime',displayName: '交易时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{ field: 'givenChannelName',displayName:'赠送渠道',width:150},
			{ field: 'givenType',displayName:'赠送类型',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.givenTypeall)},
			{ field: 'couponAmount',displayName:'赠送面值',width:150},
			{ field: 'effectiveDays',displayName:'有效期' ,width:150},
			{ field: 'givenStatus',displayName:'赠送状态',width:150,cellFilter:"formatDropping:"+angular.toJson($scope.riskStatusaa)},
			{ field: 'operUsername',displayName:'操作人',width:150 },
			{field: 'updateTime',displayName: '操作时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'successTime',displayName: '赠送成功时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'successTime',displayName: '鼓励金生效时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{field: 'operTime',displayName: '鼓励金到期时间',width: 150,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
			{ field: 'id',displayName:'操作',width:270,pinnedRight:true,
				cellTemplate:
					'<a class="lh30" ng-show="grid.appScope.hasPermit(\'cardAndReward.send\')&&row.entity.givenStatus!=1" ng-click="grid.appScope.hardWardAddModel(row.entity.id,1)">赠送  | </a>'
					+'<a class="lh30" ng-show="row.entity.givenStatus!=3" target="_black" ui-sref="creditMgr.queryCardAndRewardDetail({userNo:row.entity.id})"> 赠送记录</a>'
			}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
			gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
				$scope.paginationOptions.pageNo = newPage;
				$scope.paginationOptions.pageSize = pageSize;
				$scope.selectInfo();
			});
		}
	};

	
	    $scope.hardWardAddModel = function(id,sendTypeSwitch){
	    	$scope.info.sendTypeId = $scope.sendTypeSelect;
	    	$scope.info.sendId="1";
		   $scope.sendTypeSwitch = sendTypeSwitch;
		   $scope.sendTypeSingleId = id;
	       // $scope.statusType=0;
	        $("#hardWardAddModel").modal("show");
	    };
	    
	    //返回
	    $scope.cancel=function(){
	        $('#hardWardAddModel').modal('hide');
	    };

	 //新增代理商账户控制
    $scope.addAgentAccountControl = function(){
    	var merchantNoList = [];
    	if($scope.sendTypeSwitch!=1){
    		var merchantSelectedList = $scope.gridApi.selection.getSelectedRows();
    		console.debug(merchantSelectedList);
    		
    		var flag = 0;
    		angular.forEach(merchantSelectedList, function(item){
    			if("1"==item.givenStatus)
    				flag=1;
    		});
    		
    		if(flag==1){
    			$scope.notice("您已成功赠送该用户，不能再次赠送");
    			return ;
    		}
			
			angular.forEach(merchantSelectedList, function(item){
    			merchantNoList[merchantNoList.length] = item.id;
    		});
    	}else if($scope.sendTypeSwitch==1){
    		 merchantNoList[0]= $scope.sendTypeSingleId;
    	}
    	
    	if(merchantNoList.length==0){
			$scope.notice("请选择赠送商户");
    		return ;
    	}
		$scope.submitting = true;
		$http.post('cardAndReward/allSend',
				"baseInfo="+angular.toJson(merchantNoList)+"&info="+angular.toJson($scope.info)+"&type="+12,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(data){
					if(data.bols){
						$scope.notice(data.msg);
						$scope.selectInfo();
						$scope.submitting = false;
						 $("#hardWardAddModel").modal("hide");
					}else{
						$scope.notice(data.msg);
						$scope.submitting = false;
						 $("#hardWardAddModel").modal("hide");
					}
		})
    	
    	
    };

	//导出
	$scope.exportInfo=function(){
		SweetAlert.swal({
			title: "确认导出？",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true 
		},
		function (isConfirm) {
			if (isConfirm) {
				$scope.info.orderType=2;
				location.href="cardAndReward/exportUserList?info="+encodeURI(angular.toJson($scope.info));
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