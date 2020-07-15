/**
 * 查询banner
 */
angular.module('inspinia',['uiSwitch']).controller("queryBannerCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document) {
	//数据源
	$scope.bool=[{text:"是",value:"1"},{text:"否",value:"0"}];
	$scope.statusTypes=[{text:"全部",value:2},{text:"开启",value:1},{text:"关闭",value:0}];
	$scope.statusTypesStr = angular.toJson($scope.statusTypes);
	$scope.query = {bannerStatus:2,bannerPosition:-1,appNo:""};
	$scope.paginationOptions=angular.copy($scope.paginationOptions);
	i18nService.setCurrentLang('zh-cn');  //设置语言为中文
	$scope.appListAll = [];
	$scope.appList = [];
	$scope.getAppList = function(){	
		$http.get('banner/getAppInfo.do').success(function(msg) {
	    	if(msg!=null){
	    		$scope.appListAll = angular.copy(msg);
	    		$scope.appListAll.unshift({appName:"全部",appNo:""});
	    	}
	    });
	}
	$scope.getAppList();
	$scope.queryFunc = function(){	
		$http.post('banner/selectByCondition.do',"query=" + angular.toJson($scope.query)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
				  {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function(msg){
			$scope.banners = msg.result;
			$scope.bannerListGrid.totalItems = msg.totalCount;
		}).error(function(){
		})
	};
	$scope.bannerListGrid = {
        data: 'banners',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条 
        columnDefs: [
            {field: 'bannerId',displayName: 'ID',width: 120,pinnable: false,sortable: false},
            {field: 'appName',displayName: 'APP名称',width: 120,pinnable: false,sortable: false},
            {field: 'bannerPosition',displayName: 'banner位置',width: 120,pinnable: false,sortable: false,cellFilter:"formatDropping:"+angular.toJson($scope.bannerPosition)},
            {field: 'bannerName',displayName: 'banner名称',width: 200,pinnable: false,sortable: false,cellTemplate:'<div style="float: left;"><span ng-bind="row.entity.bannerName"></span></div>'},
            {field: 'onlineTime',displayName: '上线时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'offlineTime',displayName: '下线时间',width: 150,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'weight',displayName: '权重',width:150,pinnable: false,sortable: false},
            /*{field: 'bannerStatus',displayName: '状态',width: 150,pinnable: false,sortable: false,cellTemplate:
            	'<span ng-show="grid.appScope.hasPermit(\'banner.switch\')"><switch class="switch switch-s" ng-model="row.entity.bannerStatus" ng-change="grid.appScope.switchStatus(row)" /></span>'
	            +'<span ng-show="!grid.appScope.hasPermit(\'banner.switch\')"> <span class="lh30" ng-show="row.entity.bannerStatus==1">开启</span><span class="lh30" ng-show="row.entity.bannerStatus==0">关闭</span></span>'
            },*/
            {field: 'bannerStatus',displayName: '状态',width: 150,pinnable: false,sortable: false,cellTemplate:
            	'<span ng-show="grid.appScope.hasPermit(\'banner.switch\')">' +
					'<switch class="switch switch-s" ng-model="row.entity.bannerStatus" ng-change="grid.appScope.switchStatus(row)" />' +
				'</span>'
	            +'<span ng-show="!grid.appScope.hasPermit(\'banner.switch\')"> ' +
					'<span class="lh30" ng-show="row.entity.bannerStatus==1">开启</span>' +
					'<span class="lh30" ng-show="row.entity.bannerStatus==0">关闭</span>' +
				'</span>'
            },
            {field: 'bannerStatus',displayName: '操作',width: 180,pinnable: false,sortable: false,editable:true,cellTemplate:
            	'<div class="ui-grid-cell-contents ng-binding ng-scope" style="line-height:20px;"><div style="margin-right:5px;"><a ng-show="grid.appScope.hasPermit(\'banner.detail\')" ui-sref="sys.bannerDetail({id:row.entity.bannerId})">详情</a>'
            	+'<a ng-show="grid.appScope.hasPermit(\'banner.update\')" ui-sref="sys.modifyBanner({id:row.entity.bannerId})"> | 修改</a><a ng-show="grid.appScope.isDelete(row.entity)" ng-click="grid.appScope.deleteBanner(row.entity.bannerId)"> | 删除</a>' +
            	'</div>'}
        ],
        onRegisterApi: function(gridApi) {                
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
            	$scope.paginationOptions.pageNo = newPage;
            	$scope.paginationOptions.pageSize = pageSize;
            	$scope.queryFunc();
            });
        }
	 };
	$scope.queryFunc();

    $scope.deleteBanner = function(ntId){
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
                    $http.post("banner/deleteBanner.do","id="+ntId,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.queryFunc();
                        }).error(function(){
                    });
                }
            });
    }
    $scope.isDelete=function(obj){
    	if(obj.bannerStatus==0){
    		return true;
		}
		return false;
	}
	//修改banner状态
	$scope.switchStatus=function(row){
		if(row.entity.bannerStatus){
			$scope.serviceText = "确定开启？";
		} else {
			$scope.serviceText = "确定关闭？";
		}
        SweetAlert.swal({
            title: $scope.serviceText,
//            text: "服务状态为关闭后，不能正常交易!",
            type: "warning",
            showCancelButton: true,
            confirmButtonColor: "#DD6B55",
            confirmButtonText: "提交",
            cancelButtonText: "取消",
            closeOnConfirm: true,
            closeOnCancel: true },
	        function (isConfirm) {
	            if (isConfirm) {
	            	if(row.entity.bannerStatus==true){
	            		row.entity.bannerStatus=1;
	            	} else if(row.entity.bannerStatus==false){
	            		row.entity.bannerStatus=0;
	            	}
	            	var data={"bannerStatus":row.entity.bannerStatus,"bannerId":row.entity.bannerId};
	                $http.post("banner/switchStatus.do",angular.toJson(data))
	            	.success(function(data){
	            		if(data.status){
	            			$scope.notice("操作成功");
	            		}else{
	            			if(row.entity.bannerStatus==true){
	    	            		row.entity.bannerStatus = false;
	    	            	} else {
	    	            		row.entity.bannerStatus = true;
	    	            	}
	            			$scope.notice("操作失败");
	            		}
	            	})
	            	.error(function(data){
	            		if(row.entity.bannerStatus==true){
    	            		row.entity.bannerStatus = false;
    	            	} else {
    	            		row.entity.bannerStatus = true;
    	            	}
	            		$scope.notice("服务器异常")
	            	});
	            } else {
	            	if(row.entity.bannerStatus==true){
	            		row.entity.bannerStatus = false;
	            	} else {
	            		row.entity.bannerStatus = true;
	            	}
	            }
        });
    	
    };
	//reset
	$scope.resetForm=function(){
		$scope.query= {bannerStatus:'2'};
	}

	//页面绑定回车事件
	$document.bind("keypress", function(event) {
		$scope.$apply(function (){
			if(event.keyCode == 13){
				$scope.queryFunc();
			}
		})
	});

});
