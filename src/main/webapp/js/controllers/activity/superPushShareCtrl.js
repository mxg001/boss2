/**
 * 微创业收益明细
 */
angular.module('inspinia').controller('superPushShareCtrl',function($scope,$http,$stateParams,i18nService,SweetAlert){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	//分润入账状态
	$scope.shareStatusList = [{"text":"全部","value":""},{"text":"未入账","value":"0"},{"text":"已入账","value":"1"},{"text":"入账失败","value":"2"}];
	//分润级别
	$scope.shareTypeList = [{"text":"全部","value":""},{"text":"一级代理商分润","value":"0"},{"text":"直属代理商分润","value":"1"},
	                          {"text":"一级分润","value":"2"},{"text":"二级分润","value":"3"},{"text":"三级分润","value":"4"}];
	$scope.resetForm=function(){
		$scope.baseInfo = {shareStatus:"", shareType:"",
			createTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
			createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
		isVerifyTime = 1;
	}
	$scope.resetForm();

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.shareNo || $scope.baseInfo.orderNo || $scope.baseInfo.merchantNo) {
			isVerifyTime = 0;
		} else {
			isVerifyTime = 1;
		}
	}

	setBeginTime=function(setTime){
		$scope.baseInfo.createTimeStart = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	setEndTime=function(setTime){
		$scope.baseInfo.createTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	//查询
	$scope.query=function(){
		if ($scope.loadImg) {
			return;
		}
		if (!($scope.baseInfo.shareNo || $scope.baseInfo.orderNo || $scope.baseInfo.merchantNo)) {
			if(!($scope.baseInfo.createTimeStart && $scope.baseInfo.createTimeEnd)){
				$scope.notice("分润创建时间不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.createTimeStart).getTime();
			var etime = new Date($scope.baseInfo.createTimeEnd).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("分润创建时间范围不能超过31天");
				return;
			}
		}
		$scope.submitting = true;
		$scope.loadImg = true;
		$http.post('superPush/getShareByParam',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
			.success(function(msg){
				if(msg.status){
					$scope.superPushShareGrid.data = msg.page.result;
					$scope.superPushShareGrid.totalItems = msg.page.totalCount;
					$scope.totalAmountMap = msg.totalAmountMap;
					$scope.totalShareMap = msg.totalShareMap;
				}
				$scope.loadImg = false;
				$scope.submitting = false;
			})
	}
//	$scope.query();
	$scope.superPushShareGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'index',displayName: '序号',pinnable: false,width: 100,sortable: false,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
            {field: 'createTime',displayName: '分润创建时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'shareAmount',displayName: '分润金额',pinnable: false,width: 180,sortable: false,cellFilter:'currency:""'},
            {field: 'shareType',displayName: '分润级别',pinnable: false,width: 180,sortable: false,cellFilter:'formatDropping:' + angular.toJson($scope.shareTypeList)},
            {field: 'shareRate',displayName: '分润百分比',pinnable: false,width: 180,sortable: false,cellTemplate:"<span class='checkbox'>{{row.entity.shareRate}}<span ng-show='row.entity.shareRate'>%</span></span>"},
            {field: 'shareName',displayName: '商户/代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'shareNo',displayName: '商户/代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'transAmount',displayName: '交易金额',pinnable: false,width: 180,sortable: false,cellFilter:'currency:""'},
            {field: 'orderNo',displayName: '交易订单号',pinnable: false,width: 180,sortable: false},
            {field: 'merchantNo',displayName: '交易商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'shareStatus',displayName: '入账状态',pinnable: false,width: 180,sortable: false,cellFilter:'formatDropping:' + angular.toJson($scope.shareStatusList)},
            {field: 'shareTime',displayName: '入账时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
//            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
//	         	'<div class="lh30" ng-show="row.entity.merchantNo"><a class="lh30" target="_blank" ui-sref="activity.superPushMerchantDetail({merchantNo:row.entity.merchantNo})">商户详情</a>'
//	            +'<a class="lh30" target="_blank" ui-sref="activity.superPushCashDetail({merchantNo:row.entity.merchantNo})"> | 提现详情</a>'
////            	+'<a class="lh30" target="_blank" ui-sref="activity.superPushShareDetail({merchantNo:row.entity.merchantNo})"> | 分润详情</a>'
//            	+'</div>'
//            }
        ],
        onRegisterApi: function(gridApi) {                
            $scope.superPushShareGridApi = gridApi;
            $scope.superPushShareGridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	     });
        }
	 };
	
	$scope.export = function () {
		if (!($scope.baseInfo.shareNo || $scope.baseInfo.orderNo || $scope.baseInfo.merchantNo)) {
			if(!($scope.baseInfo.createTimeStart && $scope.baseInfo.createTimeEnd)){
				$scope.notice("分润创建时间不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.createTimeStart).getTime();
			var etime = new Date($scope.baseInfo.createTimeEnd).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("分润创建时间范围不能超过31天");
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
                    location.href="superPush/exportShare?param="+angular.toJson($scope.baseInfo);
                }
            });

    }
	
//	//获取所有的代理商
//	$scope.oneAgentList=[{value:"",text:"全部"}];
//	//代理商
//	$scope.agentList=[{value:"",text:"全部"}];
//	 $http.post("agentInfo/selectAllOneInfo")
//  	 .success(function(msg){
//  			//响应成功
//  	   	for(var i=0; i<msg.length; i++){
//  	   		$scope.oneAgentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
//  	   	}
//  	});
//	 $http.post("agentInfo/selectAllInfo")
//  	 .success(function(msg){
//  			//响应成功
//  	   	for(var i=0; i<msg.length; i++){
//  	   		$scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
//  	   	}
//  	});
	
});