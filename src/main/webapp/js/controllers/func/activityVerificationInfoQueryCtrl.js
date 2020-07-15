/**
 * 使用券查询
 */
angular.module('inspinia').controller('activityVerificationInfoQueryCtrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.verCount = {};
	//券类型
	$scope.couponTypeSelect = angular.copy($scope.couponTypeList);
	$scope.couponTypeStr=angular.toJson($scope.couponTypeSelect);
	$scope.couponTypeSelect.unshift({text:"全部",value:""});
	//核销方式
	$scope.useTypeSelect = angular.copy($scope.cancelVerificationList);
	$scope.useTypeStr=angular.toJson($scope.useTypeSelect);
	$scope.useTypeSelect.unshift({text:"全部",value:""});

    $scope.clear = function () {
    	isVerifyTime = 1;
        $scope.info={agentName:"",getStartTime:"",getEndTime:"",couponType:"",useType:"",
			startTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
        		endTime:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),couponCode:""};
    }
    $scope.clear();

	$scope.gridOptions = {
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
		useExternalPagination: true,                //分页数量//配置表格
		columnDefs:[                        //表格数据
            {field: 'id', displayName: '序号',width:80 },
            { field: 'merchantNo',displayName:'商户编号',width:130 },
            { field: 'merchantName',displayName:'商户名称',width:130 },
            { field: 'mobileNo',displayName:'商户手机号',width:120 },
            { field: 'agentName',displayName:'直属代理商',width:120 },
            { field: 'oneAgentName',displayName:'一级代理商',width:120 },
            { field: 'verFee',displayName:'抵扣手续费',width:90 },
			{ field: 'couponCode',displayName:'活动来源',width:90,cellFilter:"formatDropping:" + angular.toJson($scope.couponCode) },
			{ field: 'couponType',displayName:'券类型',width:90,cellFilter:"formatDropping:" + $scope.couponTypeStr },
			{field:'type',displayName:'使用说明',width:120,
                cellFilter: "formatDropping:"+$scope.useTypeStr},
            {field:'orderNo',displayName:'关联交易订单',width:150},
			{ field: 'transAmount',displayName:'交易金额',width:180,cellFilter:"currency:''" },
			{field:'couponNo',displayName:'关联券',width:120},
            { field: 'getTime',displayName:'券获取时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
            { field: 'createTime',displayName:'券使用时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:150},
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

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.info.merchantName || $scope.info.mobileNo || $scope.info.orderNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.info.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.info.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
    setGetBeginTime=function(setTime){
        $scope.info.getStartTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setGetEndTime=function(setTime){
        $scope.info.getEndTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    $scope.query = function () {
    	if ($scope.loadImg) {
			return;
		}
        if (!($scope.info.merchantName || $scope.info.mobileNo || $scope.info.orderNo)) {
    		if(!($scope.info.startTime && $scope.info.endTime)){
    			$scope.notice("使用时间不能为空");
    			return;
    		}
    		var stime = new Date($scope.info.startTime).getTime();
    		var etime = new Date($scope.info.endTime).getTime();
    		if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
    			$scope.notice("使用时间范围不能超过31天");
    			return;
    		}
    	}
        $scope.loadImg = true;
        $http.post("verificationInfo/verificationInfoQuery","info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        	.success(function (data) {
	            $scope.loadImg = false;
	            $scope.gridOptions.data = data.page.result;
	            $scope.gridOptions.totalItems = data.page.totalCount;
	            $scope.verCount.verFee = data.verCount.verFee;
        });
    }

    $scope.export = function () {
    	if (!($scope.info.merchantName || $scope.info.mobileNo || $scope.info.orderNo)) {
    		if(!($scope.info.startTime && $scope.info.endTime)){
    			$scope.notice("使用时间不能为空");
    			return;
    		}
    		var stime = new Date($scope.info.startTime).getTime();
    		var etime = new Date($scope.info.endTime).getTime();
    		if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
    			$scope.notice("使用时间范围不能超过31天");
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
                    
                    location.href="verificationInfo/verificationInfoExport?info="+encodeURI(angular.toJson($scope.info));
                }
            });

    }
    $scope.agent = [{text:"全部",value:""}];
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            //响应成功"
            for(var i = 0; i < msg.length; i++){
                $scope.agent.push({value:msg[i].agentNo,text:msg[i].agentName});
            }
        });

})








