/**
 * banner广告管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('bannerMgrCtrl',function($scope,$http,i18nService,$document,$window,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"关闭",value:'0'},{text:"打开",value:'1'}];
    $scope.positionTypeSelect = [{text:"不限位置",value:'0'},{text:"H5",value:'1'}];

    //获取所有组织
	$scope.org=[{value:"",text:"全部"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
		});
    
	$scope.resetForm = function () {
		$scope.ad = {orgId:""};
	};
	$scope.resetForm();

	$scope.columnDefs = [
        {field: 'id',displayName: 'ID',width: 60},
        {field: 'orgName',displayName: '组织',width: 150},
        {field: 'positionType',displayName: 'banner位置',width: 150,
        	cellFilter:"formatDropping:"+ angular.toJson($scope.positionTypeSelect)},
        {field: 'bannerTitle',displayName: 'banner标题',width: 150},
        {field: 'beginTime',displayName: '上线时间',width: 180,cellFilter:"date:'yyyy-MM-dd hh:mm:ss'"},
        {field: 'endTime',displayName: '下线时间',width: 180,cellFilter:"date:'yyyy-MM-dd hh:mm:ss'"},
        {field: 'showNo',displayName: '权重',width: 80,cellFilter:"number"},
        {field: 'status',displayName: '状态',width:120,pinnable: false,sortable: false,cellTemplate:
        	'<span ><switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.onOff(row.entity)" /></span>'},
        {field: 'action',displayName: '操作',width: 150,pinnedRight:true,
        	cellTemplate:
        	'<div class="lh30"><a ui-sref="creditMgr.bannerDetail({id:row.entity.id})" target="_black">详情</a>'
        		+'<a ng-show="grid.appScope.hasPermit(\'creditMgr.updateBanner\')" ui-sref="creditMgr.updateBanner({id:row.entity.id})" target="_black"> | 修改</a>'
        		+'<a ng-show="grid.appScope.hasPermit(\'creditMgr.delBanner\')" ng-click="grid.appScope.delBanner(row.entity.id)"> | 删除</a>'
        		+'</div>'
        }
    ];

    $scope.adGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  	//开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url: 'cmBanner/selectInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.ad,
            method:'POST'
        }).success(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!msg.status){
            	$scope.notice(msg.msg);
                return;
            }
            $scope.adGrid.data = msg.page.result;
            $scope.adGrid.totalItems = msg.page.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };

    //删除Banner
    $scope.delBanner = function (id) {
    	SweetAlert.swal({
			title: "确定删除这条数据吗？",
			type: "warning",
			showCancelButton: true,
			confirmButtonColor: "#DD6B55",
			confirmButtonText: "确定",
			cancelButtonText: "取消",
			closeOnConfirm: true,
			closeOnCancel: true },
			function (isConfirm) {
				if (isConfirm) {
					$http({
						url: "cmBanner/delBannerById?id="+id,
						method:'GET'
					}).success(function (msg) {
						$scope.notice(msg.msg);
						if (msg.status){
							$scope.query();
						}
					}).error(function (msg) {
						$scope.notice('服务器异常,请稍后再试.');
					});
				}
			});
    };
    
    //状态开关
    $scope.onOff = function(entity){
		if(entity.status){
			$scope.serviceText = "确定打开？";
		} else {
			$scope.serviceText = "确定关闭？";
		}
		SweetAlert.swal({
		    title: $scope.serviceText,
		    type: "warning",
		    showCancelButton: true,
		    confirmButtonColor: "#DD6B55",
		    confirmButtonText: "提交",
		    cancelButtonText: "取消",
		    closeOnConfirm: true,
		    closeOnCancel: true },
		    function (isConfirm) {
		        if (isConfirm) {
		        	entity.status = entity.status ? 1 : 0;
		            $http.post("cmBanner/updateBannerStatus?id=" + entity.id + "&status=" + entity.status)
		        	.success(function(data){
		        		$scope.notice(data.msg);
		        		if(data.status){
		        			$scope.query();
		        		}else{
		        			entity.status = !entity.status;
		        		}
		        	})
		        	.error(function(data){
		        		entity.status = !entity.status;
		        		$scope.notice("服务器异常");
		        	});
		        } else {
		        	entity.status = !entity.status;
		        }
		});
			
	};

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});