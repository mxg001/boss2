/**
 * 用户反馈问题查询
 */
angular.module('inspinia').controller("userFeedbackQueryCtrl", function($scope,SweetAlert,$http, $state, $stateParams,$filter,i18nService,$document) {
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.appNos = [{text:"全部",value:""}];
	$scope.problemTypes=[{text:"全部",value:""}];
	$scope.userTypes=[{text:"全部",value:""},{text:"代理商",value:1},{text:"商户",value:2}]
	$scope.statusList=[{text:"全部",value:null},{text:"已处理",value:"1"},{text:"待处理",value:"0"}]
	$scope.info={problemType:"",userType:"",userName:"",mobilephone:"",title:"",status:null,appNo:"",
		submitTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',submitTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
		dealTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',dealTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	$scope.userProblemData=[];

	//是否校验时间
	isVerifyTime = 1;//校验：1，不校验：0

	//问题类型
	 $http.post("userFeedbackProblemAction/selectAllProblemInfo")
  	 .success(function(msg){
  	   	for(var i=0; i<msg.length; i++){
  	   		$scope.problemTypes.push({value:msg[i].problemType,text:msg[i].typeName});
  	   	}
  	});



	//问题来源
	$http.post("appInfoAction/getAllAppInfo")
		.success(function(msg){
			for(var i=0; i<msg.list.length; i++){
				var data = msg.list;
				$scope.appNos.push({value:data[i].appNo,text:data[i].appName});
			}
		});

	setBeginTime=function(setTime){
		$scope.info.submitTimeBegin = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}

	setEndTime=function(setTime){
		$scope.info.submitTimeEnd = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
	}
	
	//查询
	$scope.queryFunc=function(){
		var stime = new Date($scope.info.submitTimeBegin).getTime();
		var etime = new Date($scope.info.submitTimeEnd).getTime();
		if(!($scope.info.submitTimeBegin && $scope.info.submitTimeEnd)){
			$scope.notice("提交时间不能为空");
			return;
		}
		if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
			$scope.notice("提交时间范围不能超过31天");
			return false;
		}
		$http.post("userFeedbackProblemAction/selectAllInfo","info=" + angular.toJson($scope.info)+"&pageNo="+
				$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
		.success(function(data){
			if(data.bols){
				$scope.userProblemData=data.page.result;
				$scope.userFeedbackProblem.totalItems = data.page.totalCount;
			}
		});
	}
	$scope.queryFunc();

	//导出
	$scope.export=function(){
		if($scope.loadImg){
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
					$scope.exportInfoClick("userFeedbackProblemAction/export",{"info":angular.toJson($scope.info)});
				}
			});
	}

	//清空
	$scope.resetForm=function(){
		$scope.info={problemType:"",userType:"",userName:"",mobilephone:"",title:"",status:null,appNo:"",submitTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',submitTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59'),
			dealTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',dealTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
	}
	
	//列表
	$scope.paginationOptions=angular.copy($scope.paginationOptions);	
	$scope.userFeedbackProblem = {
		        data: 'userProblemData',
		        paginationPageSize:10,                  //分页数量
		        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
		        useExternalPagination: true,		  //开启拓展名
		        columnDefs: [
		        	  {field: 'id',displayName: 'ID',width: 120,pinnable: false,sortable: false},
							  {field: 'userType',displayName: '反馈用户分类',width: 150,pinnable: false,sortable: false,cellFilter:"userTypeas"},
								{field: 'appName',displayName: '问题来源',width: 180,pinnable: false,sortable: false},
		            {field: 'userName',displayName: '反馈用户',width: 150,pinnable: false,sortable: false},
		            {field: 'mobilephone',displayName: '反馈用户手机号',width: 150,pinnable: false,sortable: false},
		            {field: 'typeName',displayName: '问题类别',width:150,pinnable: false,sortable: false},
		            {field: 'title',displayName: '标题',width: 180,pinnable: false,sortable: false},
		            {field: 'submitTime',displayName: '提交时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		            {field: 'status',displayName: '状态',width: 180,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.statusList)},
		            {field: 'dealUserName',displayName: '处理人员',width: 180,pinnable: false,sortable: false},
		            {field: 'dealTime',displayName: '处理时间',width: 180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
		            {field: 'dealResult',displayName: '处理结果',width: 180,pinnable: false,sortable: false},
		            {field: 'id',displayName: '操作',width: 130,pinnedRight: true,sortable: false,editable:true,
		            	cellTemplate:'<a class="lh30" target="_blank" ng-show="grid.appScope.hasPermit(\'userFeedback.detail\')" ui-sref="sys.userFeedbackDetail({id:row.entity.id})">详情</a>'
		            }
		        ],
		        onRegisterApi: function(gridApi) {                
		            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
		            	$scope.paginationOptions.pageNo = newPage;
		            	$scope.paginationOptions.pageSize = pageSize;
		            	$scope.queryFunc();
		            });
		        }
		 };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});

}).filter('userTypeas', function () {
	return function (value) {
		switch(value) {
			case "1" :
				return "代理商";
				break;
			case "2" :
				return "商户";
				break;
		}
	}
})