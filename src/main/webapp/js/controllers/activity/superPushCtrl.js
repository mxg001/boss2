/**
 * 微创业商户查询
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('superPushCtrl',function($scope,$http,$stateParams,i18nService,$timeout){
	//数据源
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.statusList = [{"text":"全部","value":""},{"text":"待一审","value":"1"},{"text":"待平台审核","value":"2"},{"text":"审核通过","value":"4"},
	                     {"text":"待完善资料","value":"-1"},{"text":"审核不通过","value":"3"}];
	$scope.resetForm=function(){
		isVerifyTime = 1;
		$scope.baseInfo = {merchantNo:"",merchantName:"",agentNo:"",oneAgentNo:"",mobilephone:"",
				oneMerchantNo:"",twoMerchantNo:"",threeMerchantNo:"",status:"",
			createTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
			createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	$scope.resetForm();

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	keyChange=function(){
		if ($scope.baseInfo.merchantNo || $scope.baseInfo.mobilephone) {
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
		if (!($scope.baseInfo.merchantNo || $scope.baseInfo.mobilephone)) {
			if(!($scope.baseInfo.createTimeStart && $scope.baseInfo.createTimeEnd)){
				$scope.notice("注册时间不能为空");
				return;
			}
			var stime = new Date($scope.baseInfo.createTimeStart).getTime();
			var etime = new Date($scope.baseInfo.createTimeEnd).getTime();
			if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
				$scope.notice("注册时间范围不能超过31天");
				return;
			}
		}
		$scope.submitting = true;
		$scope.loadImg = true;
		$http.post('superPush/getByParam',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(msg){
					$scope.loadImg = false;
					$scope.submitting = false;
					if(msg.status){
						$scope.superPushGrid.data = msg.page.result;
						$scope.superPushGrid.totalItems = msg.page.totalCount;
					}
		})
	}
//	$scope.query();
	$scope.superPushGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'index',displayName: '序号',pinnable: false,width: 100,sortable: false,cellTemplate: "<span class='checkbox'>{{rowRenderIndex + 1}}</span>"},
            {field: 'merchantName',displayName: '商户名称',pinnable: false,width: 180,sortable: false},
            {field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'mobilephone',displayName: '手机号',pinnable: false,width: 180,sortable: false},
            {field: 'status',displayName: '状态',pinnable: false,width: 180,sortable: false,cellFilter:'formatDropping:' + angular.toJson($scope.statusList)},
            {field: 'createTime',displayName: '注册时间',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'agentName',displayName: '直属代理商名称',pinnable: false,width: 180,sortable: false},
            {field: 'agentNo',displayName: '直属代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'oneMerchantNo',displayName: '上一级商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'twoMerchantNo',displayName: '上二级商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'threeMerchantNo',displayName: '上三级商户编号',pinnable: false,width: 180,sortable: false},
//            {field: 'avaliBalance',displayName: '账户余额',pinnable: false,width: 180,sortable: false},
            {field: 'action',displayName: '操作',width: 250,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30" ng-show="row.entity.merchantNo"><a class="lh30" target="_blank" ui-sref="activity.superPushMerchantDetail({merchantNo:row.entity.merchantNo})">商户详情</a>'
	            +'<a class="lh30" target="_blank" ui-sref="activity.superPushCashDetail({merchantNo:row.entity.merchantNo})"> | 提现详情</a>'
//            	+'<a class="lh30" target="_blank" ui-sref="activity.superPushShareDetail({merchantNo:row.entity.merchantNo})"> | 分润详情</a>'
            	+'</div>'
            }
        ],
        onRegisterApi: function(gridApi) {                
            $scope.superPushGridApi = gridApi;
            $scope.superPushGridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	     });
        }
	 };
	
	//获取所有的代理商
	$scope.oneAgentList=[{value:"",text:"全部"}];
	//代理商
	$scope.agentList=[{value:"",text:"全部"}];
	 $http.post("agentInfo/selectAllOneInfo")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.oneAgentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
  	   	}
  	});
	 $http.post("agentInfo/selectAllInfo")
  	 .success(function(msg){
  			//响应成功
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
  	   	}
  	});
	
	//异步获取直属代理商
	 var oldValue="";
	 var timeout="";
	$scope.getAgentList = function(value) {
		$scope.agentt = [];
			var newValue=value;
			if(newValue != oldValue){
				if (timeout) $timeout.cancel(timeout);
				timeout = $timeout(
					function(){
						$http.post('agentInfo/selectAllInfo','item=' + value,
								{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
							.then(function (response) {
								if(response.data.length==0) {
									$scope.agentt.push({value: "", text: "全部"});
								}else{
									$scope.agentt.push({value: "", text: "全部"});
									for(var i=0; i<response.data.length; i++){
										$scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
									}
								}
								$scope.agentList = $scope.agentt;
								oldValue = value;
							});
					},800);
			}
	};
	//异步获取一级代理商
	var oneOldValue="";
	var oneTimeout="";
	$scope.getOneAgentList = function(value) {
		$scope.agentt = [];
			var newValue=value;
			if(newValue != oneOldValue){
				if (oneTimeout) $timeout.cancel(oneTimeout);
				oneTimeout = $timeout(
					function(){
						$http.post('agentInfo/selectAllOneInfo','item=' + value,
								{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
							.then(function (response) {
								if(response.data.length==0) {
									$scope.agentt.push({value: "", text: "全部"});
								}else{
									$scope.agentt.push({value: "", text: "全部"});
									for(var i=0; i<response.data.length; i++){
										$scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
									}
								}
								$scope.oneAgentList = $scope.agentt;
								oneOldValue = value;
							});
					},800);
			}
	};
	
});