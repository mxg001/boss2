angular.module('inspinia', ['infinity.angular-chosen']).controller('switchSpecialSet070Ctrl',function($scope,$http,$state,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.systems = [{text:"快钱系统",value:"kq"},{text:"中付系统",value:"zf"}];
    $scope.addInfo = {};

    //查询开关设置相关信息
	$scope.getFunInfo = function(){
		$http.get('functionManager/getFunctionManagerInfo?functionNumber=' + $stateParams.functionNumber)
			.success(function(data){
				if(data.status){
					$scope.baseInfo = data.info;
					$scope.titleName = $scope.baseInfo.functionName;
					var limitSys = $scope.baseInfo.limitSys;
					if(limitSys!=null && limitSys.indexOf('kq')!=-1){
						$scope.selected.push('kq');
					}
					if(limitSys!=null && limitSys.indexOf('zf')!=-1){
						$scope.selected.push('zf');
					}
				} else {
					$scope.notice(data.msg);
				}
			});
	};
	$scope.getFunInfo();
    $scope.selected = [];
    $scope.expression = function(id){
    	return $scope.selected.indexOf(id)!=-1;
	}

    $scope.updateSelection = function ($event, id) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');
        if (action == 'add' && $scope.selected.indexOf(id) == -1) {
            $scope.selected.push(id);
        }
        if (action == 'remove' && $scope.selected.indexOf(id) != -1) {
            var idx = $scope.selected.indexOf(id);
            $scope.selected.splice(idx, 1);
        }
    }
	//查询开关设置相关信息
	$scope.query = function(){
		$http.get('functionManager/getFunctionManagerTeamList?functionNumber=' + $stateParams.functionNumber)
			.success(function(data){
				if(data.status){
					$scope.result = data.list;
				} else {
					$scope.notice(data.msg);
				}
			});
	};
	$scope.query();

	$scope.teamGrid={                           //配置表格
		data: 'result',
		paginationPageSize:10,                  //分页数量
		paginationPageSizes: [10,20,50,100],	//切换每页记录数
		useExternalPagination: true,		    //开启拓展名
		enableHorizontalScrollbar: true,        //横向滚动条
		enableVerticalScrollbar : true,  		//纵向滚动条
		columnDefs:[                           //表格数据
			{field: 'teamName', displayName: '组织'},
			{field: 'teamId', displayName: '组织ID'},
		/*	{field: 'teamEntryName', displayName: '子组织'},
			{field: 'teamEntryId', displayName: '子组织ID'},*/
			{field: 'beginTime', displayName: '生效时间'},
			{field: 'endTime', displayName: '失效时间'},
			{field: 'activity_days', displayName: '交易天数限制'},
			{ field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:

					'<div class="lh30">'+
						'<a ng-show="grid.appScope.hasPermit(\'func.delectFunctionConfig\')" ng-click="grid.appScope.deleteTeamInfo(row.entity.id)">删除 |</a> ' +
                    	'<a ng-show="grid.appScope.hasPermit(\'func.updateFunctionConfig\')" ng-click="grid.appScope.updateTeamInfo(row.entity.id)">修改</a> ' +
					'</div>'
			}
		],
		onRegisterApi: function(gridApi) {
			$scope.gridApi = gridApi;
		}
	};

	$scope.submitting = false;
	//保存开关基本信息
	$scope.submit = function(){
		if($scope.submitting){
			return;
		}
		if($scope.selected!=null){
            $scope.baseInfo.limitSys = $scope.selected.toString();
		}
		$scope.submitting = true;
		$http({
			method: "post",
			url: "functionManager/updateBaseInfo",
			data: $scope.baseInfo
		}).success(function(result){
			$scope.notice(result.msg);
			$scope.submitting = false;
		}).error(function(){
			$scope.notice('服务异常');
			$scope.submitting = false;
		});
	};

	$scope.teamType=[];
	//组织
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		$scope.teamType.push({text:"请选择",value:null});
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId+","+msg.teamInfo[i].teamName});
		}
	});
	//获取所有的子组织数据
	$scope.allSubTeams = [];
    /*$http.get('teamInfo/querySubTeams').success(function(result){
        $scope.subTeamMap = result.subTeamMap;
    });*/

	//组织数据变动时间
	$scope.hasSubTeam = function(teamId){
		if(teamId == "" || teamId == null){
			$scope.subTeams = [];
		}else {
			var key=teamId.split(",")[0];
			$scope.subTeams = [];
		}
		//$scope.addInfo.teamEntryId = null;
	};

    $scope.teamAddModalHide = function(){
    	 $('#teamAddModal').modal('hide');
    };

	$scope.teamAddModalShow = function(){
		$scope.addInfo = {teamId:null};
        document.getElementsByName("beginTime")[0].value = null  //获取输入框元素
        document.getElementsByName("endTime")[0].value = null//获取输入框元素
		$scope.subTeams = [];
		$('#teamAddModal').modal('show');
	};
    $scope.updateTeamInfo=function(id){
        $http.post("functionManager/getFunctionTeamById?id="+id)
            .success(function(data){
                if(data.status){
                    $scope.addInfo=angular.copy(data.info);
                    $scope.addInfo.teamId= data.info.teamId+","+data.info.teamName;
                    document.getElementsByName("beginTime")[0].value = $scope.addInfo.beginTime //获取输入框元素
                    document.getElementsByName("endTime")[0].value = $scope.addInfo.endTime//获取输入框元素
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
        $('#teamAddModal').modal('show');
	}


	//新增
	$scope.submittingMode=false;
	$scope.teamAdd=function () {
		if($scope.submittingMode){
			return;
		}
		var beginTime = document.getElementsByName("beginTime")[0].value//获取输入框元素
        var endTime = document.getElementsByName("endTime")[0].value//获取输入框元素
		if(beginTime==null || beginTime=="" || endTime==null || endTime==""){
			$scope.notice('生效/失效时间不能为空');
            return;
		}
        $scope.addInfo.beginTime = beginTime;
        $scope.addInfo.endTime = endTime;
		//校验
		$scope.submittingMode=true;
		$scope.subInfo=angular.copy($scope.addInfo);
		$scope.subInfo.functionNumber=$stateParams.functionNumber;
		//数据转换
		if($scope.subInfo.teamId!=null&&$scope.subInfo.teamId!=""){
			var strs=$scope.subInfo.teamId.split(",");
			$scope.subInfo.teamId=strs[0];
			$scope.subInfo.teamName=strs[1];
		}
		if($scope.subInfo.teamEntryId!=null&&$scope.subInfo.teamEntryId!=""){
			var strs=$scope.subInfo.teamEntryId.split(",");
			$scope.subInfo.teamEntryId=strs[0];
			$scope.subInfo.teamEntryName=strs[1];
		}

		var data={
			info:angular.toJson($scope.subInfo),
		};
		var postCfg = {
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			transformRequest: function (data) {
				return $.param(data);
			}
		};
		$http.post("functionManager/saveFunctionTeam",data,postCfg)
			.success(function(data){
				if(data.status){
					$scope.query();
					$scope.notice(data.msg);
					$scope.teamAddModalHide();
				}else{
					$scope.notice(data.msg);
				}
				$scope.submittingMode=false;
			})
			.error(function(data){
				$scope.notice(data.msg);
				$scope.submittingMode=false;
			});
	};
	//删除
	$scope.deleteTeamInfo=function(id){
		SweetAlert.swal({
				title: "确认删除?",
				showCancelButton: true,
				confirmButtonColor: "#DD6B55",
				confirmButtonText: "提交",
				cancelButtonText: "取消",
				closeOnConfirm: true,
				closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http.post("functionManager/deleteFunctionTeam","id="+id,
						{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
						.success(function(data){
							if(data.status){
								$scope.query();
								$scope.notice(data.msg);
							}else{
								$scope.notice(data.msg);
							}
						})
						.error(function(data){
							$scope.notice(data.msg);
						});

				}
			});
	};
});