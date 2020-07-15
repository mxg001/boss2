angular.module('inspinia').controller("agentAuditQueryCtrl", function($scope, $http, $state, $stateParams,i18nService,$document,SweetAlert) {
	// $scope.checkStatus=[{text:"全部",value:""},{text:"已审核",value:1},{text:"未审核",value:0}];
	// $scope.lockStatus=[{text:"全部",value:""},{text:"锁定",value:1},{text:"未锁定",value:0}];
    $scope.statusStr = '[{text:"关闭进件",value:"0"},{text:"正常",value:"1"},{text:"冻结",value:"2"}]';
    //reset
    $scope.reset=function(){
        $scope.baseInfo = {teamId:"",startTime:"",endTime:""};
    };
    $scope.reset();

	$scope.agentData = [];
	$scope.teamTypeStr=null;
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		$scope.teamType=[];
		$scope.teamType.push({text:'全部',value:""});
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
		$scope.teamTypeStr=angular.toJson($scope.teamType);
        $scope.agentGrid.columnDefs.splice($scope.agentGrid.columnDefs.length-1,0,{field: 'teamId',displayName: '所属组织',width:150,cellFilter:"formatDropping:" + $scope.teamTypeStr});
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
	         {field: 'agentNo',displayName: '代理商编号' ,pinnable: false,sortable: false},
	         {field: 'agentName',displayName: '代理商名称' ,pinnable: false,sortable: false},
	         {field: 'status',displayName: '状态' ,pinnable: false,sortable: false,cellFilter:"formatDropping:" + $scope.statusStr},
/*	         {field: 'status',displayName: '状态' ,pinnable: false,sortable: false,cellTemplate:'<switch class="switch switch-s" ng-model="row.entity.status"  ng-change="grid.appScope.switchStatus(row)" />'},
*/	         {field: 'createDate',displayName: '创建时间' ,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
	         {field: 'action',displayName: '操作',pinnedRight:true,sortable: false,editable:true,cellTemplate:
	         	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'agent.detail\')" ui-sref="agent.audit({id:row.entity.agentNo,teamId:row.entity.teamId})">审核</a>'
	        	+'</div>'}
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
        $scope.submitting = true;
        $scope.loadImg = true;
		$http({
			method:'post',
			url:'agentInfo/selectAgentShareCheckList?&pageNo='+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			data:$scope.baseInfo
		}).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
			if(result.status){
                $scope.agentData = result.data.result;
                $scope.agentGrid.totalItems = result.data.totalCount;
			} else {
				$scope.notice(result.msg);
			}
		}).error(function(){
            $scope.notice("系统异常");
            $scope.submitting = false;
            $scope.loadImg = false;
		});
	}
	// $scope.query();

    $scope.export = function () {
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
                    location.href="agentInfo/exportAgentShareCheck?param="+angular.toJson($scope.baseInfo);
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

});