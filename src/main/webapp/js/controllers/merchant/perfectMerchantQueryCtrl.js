/**
 * 待完善商户查询
 */
angular.module('inspinia').controller('perfectMerchantQueryCtrl',function($scope,$http,$state,$stateParams,SweetAlert,i18nService,$document){
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文

	$scope.info={telNo:"",teamId:""};
	
	$scope.team=[{text:"全部",value:""}];
	$http.get('teamInfo/queryTeamName.do').success(function(msg){
		for(var i=0;i<msg.teamInfo.length;i++){
			$scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
		}
	});
	$scope.resetForm=function(){
		$scope.info={telNo:"",teamId:""};
	}
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	$scope.selectInfos=function(){
		if($scope.info.createTime>$scope.info.updatePwdTime){
			$scope.notice("起始时间不能大于结束时间");
			return;
		}
		$http.post(
			'user/perfectMerchantQuery.do',
			 "info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
			 {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
		).success(function(result){
			//响应成功
			if(result.bols){
				$scope.perfectTable.data = result.page.result; 
				$scope.perfectTable.totalItems = result.page.totalCount;
			}else{
				$scope.notice(result.msg);
			}
		});
	}
	$scope.selectInfos();
	$scope.perfectTable = {
		paginationPageSize: 10,
		paginationPageSizes: [10, 20, 50, 100],
		useExternalPagination: true,		  	//开启拓展名
		columnDefs: [
	           {field: 'telNo', displayName: '手机号'},
	           {field: 'teamId', displayName: '所属组织'},
	           {field: 'createTime', displayName: '注册时间',cellFilter: "date:'yyyy-MM-dd HH:mm:ss'"},
            {field: 'action',displayName: '操作',width: 150,pinnedRight: true,pinnable: false,sortable: false,editable:true,cellTemplate:
                "<div  class='lh30'>" +
                "<a ng-show=\"grid.appScope.hasPermit('merchant.delPerMer')\" ng-click='grid.appScope.deletePerMer(row.entity.id)'>  删除 </a>" +
                "</div>"},
        ],
        onRegisterApi: function(gridApi){
       	$scope.gridApi = gridApi;
        	$scope.gridApi.pagination.on.paginationChanged($scope, function(newPage, pageSize){
        		$scope.paginationOptions.pageNo = newPage;
        		$scope.paginationOptions.pageSize = pageSize;
        		$scope.selectInfos();
        	});
        }
	};

    //删除商户
    $scope.deletePerMer=function(id){
        SweetAlert.swal({
                title: "确认删除？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('user/delPerfectMerchant.do?ids='+id).success(function(msg){
                        if(msg.bols){
                            $scope.notice(msg.msg);
                            $scope.selectInfos();
                        } else {
                            $scope.notice(msg.msg);
                        }
                    }).error(function(){
                    });
                }
            });
    };

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.selectInfos();
			}
		})
	});
})