angular.module('inspinia').controller("queryAgentRoleOemCtrl", function($scope, $http, $state, $stateParams,i18nService,SweetAlert,$document) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.info = {role_name:"",role_code:""};

    $scope.agentRoleList = [];
    $http.get('agentInfo/selectAgentRole')
        .success(function(result){
            if(!result)
                return;
            $scope.agentRoleList.push({text:"请选择角色",value:""});
            angular.forEach(result,function(data){
                $scope.agentRoleList.push({text:data.role_name,value:data.id});
            });
        })

    $scope.clear=function(){
        $scope.info = {role_name:"",role_code:""};
    }

    $scope.query = function(){
        $scope.loadImg = true;
        $http.post('agentInfo/queryAgentRoleOemList',"info=" + angular.toJson($scope.info) + "&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize, {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(msg){
            $scope.loadImg = false;
            $scope.agentData = msg.result;
            $scope.agentGrid.totalItems = msg.totalCount;
        }).error(function(){
            $scope.loadImg = false;
        });
    }
    $scope.query();

	$scope.agentGrid = {
        data : "agentData",
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs : [
            {field: 'role_name', displayName: '角色名称',width: 200,},
            {field: 'agent_type', displayName: '代理商类型',width: 200,cellFilter:"formatDropping:"+angular.toJson($scope.allAgentTypeText)},
            {field: 'agent_oem',displayName:'所属品牌',width:200,cellFilter:"formatDropping:"+angular.toJson($scope.allAgentOemText)},
            {field: 'remark', displayName: '备注',width: 200},
            {field: 'action',displayName: '操作',width: 200,pinnedRight:true,editable:true,cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'agent.updateAgentRoleOem\')&&row.entity.status==0" ng-click="grid.appScope.updateAgentRole(row.entity)"> 修改 </a>'
                +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'agent.deleteAgentRoleOem\')&&row.entity.status==0" ng-click="grid.appScope.deleteAgentRole(row.entity.id)"> 删除 </a>'
                +'<a class="lh30" ng-show="grid.appScope.hasPermit(\'agent.userAgentRoleOem\')" ng-click="grid.appScope.userAgentRoleOemModel(row.entity)"> 用户清单 </a>'}
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

    $scope.updateAgentRole = function (entity) {
        $scope.baseInfo = entity;
        $scope.addAgentRoleOemModal("up");
    };

    $scope.subDisable = false;
    $scope.addAgentRoleOemModal=function (op) {
        $("#addAgentRoleOemModal").modal("show");
        if("up"===op){
            $scope.subDisable = true;
		}else{
            $scope.subDisable = false;
            $scope.baseInfo={role_id:"",agent_type:"",agent_oem:"",remark:""};
		}
    }

    $scope.cancelAgentRoleOemModal=function () {
        $('#addAgentRoleOemModal').modal('hide');
        $scope.baseInfo={role_id:"",agent_type:"",agent_oem:"",remark:""};
    }


    $scope.addAgentRoleOemSubmit = function () {
        if ($scope.loadImg) {
            return;
        }
        if($scope.baseInfo.role_id===""){
            $scope.notice("请选择角色");
            return;
        }
        if($scope.baseInfo.agent_type===""){
            $scope.notice("请选择代理商类型");
            return;
        }
        $scope.loadImg = true;
        var url = $scope.subDisable ?  "agentInfo/updateAgentRoleOem" : "agentInfo/addAgentRoleOem";
        $http.post(url, "info=" + angular.toJson($scope.baseInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
            	if(data.status){
                    $scope.cancelAgentRoleOemModal();
                    $scope.query();
                    $scope.clear();
				}
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

	//删除代理商角色
	$scope.deleteAgentRole=function(id){
        SweetAlert.swal({
            title: "确认删除？",
//            text: "",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	$http.post('agentInfo/deleteAgentRoleOem',id).success(function(msg){
	    				if(msg.status){
	    					$scope.notice(msg.msg);
	    					$scope.query();
	    				} else {
	    					$scope.notice(msg.msg);
	    				}
	    			}).error(function(){
	    			}); 
	            }
        });
    };

    $scope.agentUserlist=[];
    $scope.agentUserGrid = {
        data: 'agentUserlist',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'agent_name',displayName: '代理商名称',width:200},
            {field: 'agent_no',displayName: '代理商编号',width:200},
            {field: 'mobilephone',displayName: '手机号码',width:200},
            {field: 'status',displayName: '用户状态',width:200,cellFilter:'formatDropping:[{text:"失效",value:"0"},{text:"有效",value:"1"}]'}
        ]
    };

    $scope.userAgentRoleOemModel=function (entity) {
        $("#userAgentRoleOemModel").modal("show");
        $scope.loadImg2 = true;
        $http.post('agentInfo/userAgentRoleOem',"info=" + angular.toJson(entity),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
        ).success(function(msg){
            $scope.loadImg2 = false;
            $scope.agentUserlist = msg;
        }).error(function(){
            $scope.loadImg2 = false;
        });
    }

    $scope.closeUserAgentRoleOemModel=function () {
        $('#userAgentRoleOemModel').modal('hide');
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