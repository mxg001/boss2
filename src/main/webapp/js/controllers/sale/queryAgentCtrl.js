angular.module('inspinia').controller("saleQueryAgentCtrl", function($scope, $http, $state, $stateParams,i18nService,$document) {
	$scope.checkStatus=[{text:"全部",value:""},{text:"待审核",value:0},{text:"审核通过",value:1},{text:'审核不通过',value:2}];
	$scope.lockStatus=[{text:"全部",value:""},{text:"锁定",value:1},{text:"未锁定",value:0}];
	$scope.baseInfo = {shareCheck:"",shareLock:"",rateCheck:"",rateLock:"",hasAccount:"",quotaCheck:"",quotaLock:"",agentLevel:-1,teamId:"",hasSub:0};
	$scope.statusStr = '[{text:"关闭进件",value:"0"},{text:"正常",value:"1"},{text:"冻结",value:"2"}]';
	$scope.agentData = [];
	$scope.teamTypeStr=null;
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		$scope.teamType=[];
		$scope.teamType.push({text:'全部',value:""});
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		$scope.teamTypeStr=angular.toJson($scope.teamType);
        $scope.agentGrid.columnDefs.splice($scope.agentGrid.columnDefs.length-1,0,{field: 'teamId',displayName: '所属组织',width:150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.teamTypeStr});
    });
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.agentGrid = {
		data : "agentData",
		paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条 
        columnDefs: [
	         {field: 'agentNo',displayName: '代理商编号',width: 120,pinnable: false,sortable: false},
	         {field: 'agentLevel',displayName: '代理商级别',width: 120,pinnable: false,sortable: false},
	         {field: 'agentName',displayName: '代理商名称',width: 120,pinnable: false,sortable: false},
	         {field: 'parentId',displayName: '上级代理商编号',width: 150,pinnable: false,sortable: false},
	         {field: 'parentAgentName',displayName: '上级代理商名称',width: 150,pinnable: false,sortable: false},
	         {field: 'oneLevelId',displayName: '一级代理商编号',width:150,pinnable: false,sortable: false},
	         {field: 'status',displayName: '状态',width:150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
	         {field: 'createDate',displayName: '创建时间',width:150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	         {field: 'hasAccount',displayName: '是否开户',width:150,pinnable: false,sortable: false,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
	         {field: 'action',displayName: '操作',width: 180,pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<a class="lh30" ng-show="grid.appScope.hasPermit(\'agentSelect.detail\')" ui-sref="sale.agentDetail({id:row.entity.agentNo,teamId:row.entity.teamId})">详情</a>'
	         }
	     ],
	     onRegisterApi: function(gridApi) {              
	         $scope.gridApi = gridApi;
	         $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	          	$scope.paginationOptions.pageNo = newPage;
	          	$scope.paginationOptions.pageSize = pageSize;
	            $scope.query();
	         });
	     }
	};
	
	$scope.query = function(){
		$http.post('agentInfo/queryAgentInfoListSale',"baseInfo=" + angular.toJson($scope.baseInfo) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
				$scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(msg){
			//tgh328
			if (!msg.result) {
				$scope.notice("没查询到数据");
				return;
			}
			$scope.agentData  = msg.result;
			$scope.agentGrid.totalItems = msg.totalCount;
		}).error(function(){
		});
	}
	//328tgh
//	$scope.query();
	//reset
	$scope.resetForm=function(){
		$scope.baseInfo= {shareCheck:"",shareLock:"",rateCheck:"",rateLock:"",quotaCheck:"",quotaLock:"",hasAccount:"",agentLevel:-1,teamId:"",hasSub:0};
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