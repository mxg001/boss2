/**
*/
angular.module('inspinia').controller('msgCtrl',function($scope,$http,$state,$stateParams,i18nService,SweetAlert,$document){
	i18nService.setCurrentLang('zh-cn');
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.statusType = [{text:"全部",value:""},{text:"失效",value:0},{text:"生效",value:1}];


	   //模块名
    $scope.modelTypeList=[{text:"全部",value:""}];
    $http.post("sysDict/getListByKey.do?sysKey=MODELTYPE")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.modelTypeList.push({value:data[i].sysValue,text:data[i].sysName});
            }
           console.debug(angular.toJson($scope.modelTypeList));
        });
    
    
    //收单机构
    $http.post('groupService/acqOrgSelectBox.do'
	).success(function(data){
		$scope.acqOrgs = data;
		$scope.acqOrgs.splice(0,0,{"acqName":"全部","acqEnname":""});
	}).error(function(){
	}); 
    

    $scope.msgTypeList=[{text:"全部",value:""}];
    $http.post("sysDict/getListByKey.do?sysKey=MSGTYPE")
        .success(function(data){
            //响应成功
            for(var i=0; i<data.length; i++){
                $scope.msgTypeList.push({value:data[i].sysValue,text:data[i].sysName});
            }
        });
	
	
    //清空查询条件
    $scope.resetForm = function(){
        $scope.baseInfo = {status:"",moduleName:"",msgType:"",sourceOrg:""};
    }

    $scope.resetForm();

    $scope.calendar = {
		data: 'calendarData',
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
            {field: 'userMsg',width: 200, displayName: '提示信息'},
            {field: 'reason',width: 200, displayName: '错误原因'},
            {field: 'mdName',width: 200, displayName: '模块名'},
            {field: 'sourceOrg',width: 200, displayName: '收单机构'},
            {field: 'sourceCode',width: 200, displayName: '收单机构错误码'},
            {field: 'sourceMsg',width: 200, displayName: '收单机构错误信息'},
            {field: 'sourceRemark',width: 200, displayName: '收单机构错误备注'},
            {field: 'msgCode',width: 200, displayName: '错误码'},
            {field: 'status',width: 100, displayName: '生效状态', cellFilter:"formatDropping:"+ angular.toJson($scope.statusType)},
            {field: 'mtName',width: 100, displayName: '消息类型'},
            {field: 'options',width: 400, displayName: '操作',pinnedRight:true, cellTemplate:
            	'<div class="lh30"><a ng-show="grid.appScope.hasPermit(\'sys.msgDetail\')" ui-sref="sys.msgDetail({id:row.entity.id})" target="_black">详情 | </a><a ng-show="grid.appScope.hasPermit(\'sys.msgUpdate\')" ui-sref="sys.msgUpdate({id:row.entity.id})" target="_black">修改</a>'
               +'<a ng-show="grid.appScope.hasPermit(\'sys.msgrefresh\')" ng-click="grid.appScope.refresh(row.entity.id)"> | 刷新</a>'
               +'<a ng-show="grid.appScope.hasPermit(\'sys.msgUpdate\') && row.entity.status==0" ng-click="grid.appScope.changeStatus(row.entity.id,1)"> | 生效</a>'
               +'<a ng-show="grid.appScope.hasPermit(\'sys.msgUpdate\') && row.entity.status==1" ng-click="grid.appScope.changeStatus(row.entity.id,0)"> | 失效</a></div>'
            }
        ],
        onRegisterApi: function(gridApi){
        	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.query();
        	});
        }
	};
	
	//查询
	$scope.query = function(){
		$http.post('msg/selectMsgByCondition.do',"baseInfo="+angular.toJson($scope.baseInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
			$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
				.success(function(page){
					if(!page){
						return;
					}
					$scope.calendarData = page.result;
					$scope.calendar.totalItems = page.totalCount;
				}).error(function(){
				});
	}
	$scope.query();
	
	$scope.refresh = function(id){
		$http.post('msg/refresh.do',"id="+id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
					.success(function(data){
						if(data.status){
							$scope.notice(data.msg);
						}else{
							$scope.notice(data.msg);
						}
						$scope.query();
					}).error(function(){
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

    $scope.changeStatus = function (id,status) {
    	var text = "确定失效?";
    	if(status==1){
            text = "确定生效?";
		}

        SweetAlert.swal({
                title: text,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    var data={"id":id,"status":status};
                    $http.post('msg/changeStatus.do',"data="+angular.toJson(data),{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.query();
                        }).error(function(){
                    });
                }
            });
    }
});