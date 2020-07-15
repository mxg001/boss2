/**
 * 公告管理
 */
angular.module('inspinia',['infinity.angular-chosen','uiSwitch']).controller('systemNoticeCtrl',function($scope,$http,i18nService,$document,$window,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.statusSelect = [{text:"待发布",value:"1"},{text:"已发布",value:"2"},{text:"已废弃",value:"3"}];
    $scope.popSwitchSelect = [{text:"已关闭-非弹窗提示",value:"0"},{text:"已开启-弹窗提示",value:"1"}];

	//获取所有组织
	$scope.org=[{value:"",text:"全部"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
		});

    $scope.columnDefs = [
        {field: 'id',displayName: '公告ID',width: 100,pinnable: false,sortable: false},
        {field: 'title',displayName: '公告标题',width: 150,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '下发对象',width: 150,pinnable: false,sortable: false},
        {field: 'popSwitch',displayName: '弹窗公告提示开关',width:180,pinnable: false,sortable: false,cellTemplate:
            '<span><switch class="switch switch-s" ng-model="row.entity.popSwitch" ng-change="grid.appScope.switchStatus(row.entity)" /></span>'
        },
        {field: 'status',displayName: '公告状态',width: 150,pinnable: false,sortable: false,
        	cellFilter:"formatDropping:" + angular.toJson($scope.statusSelect)},
        {field: 'createTime',displayName: '创建时间',width: 180,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'sendTime',displayName: '下发时间',width: 180,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'senderName',displayName: '下发人',width: 150},
        {field: 'remark',displayName: '备注',width: 180},
        {field: 'action',displayName: '操作',width: 200,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30">' +
                '<div ng-show="row.entity.status==1">'+
                	'<a ui-sref="creditMgr.noticeDetail({id:row.entity.id})" target="_black">详情</a>' +
	                '<a ng-show="grid.appScope.hasPermit(\'creditMgr.updateNotice\')" ng-click="grid.appScope.sendOrRecoverNotice(row.entity.id, 2)"> | 下发</a>' +
	                '<a ng-show="grid.appScope.hasPermit(\'creditMgr.updateNotice\')" ui-sref="creditMgr.updateNotice({id:row.entity.id})" target="_black"> | 修改</a>' +
	                '<a ng-show="grid.appScope.hasPermit(\'creditMgr.delNotice\')" ng-click="grid.appScope.deleteNotice(row.entity.id)"> | 删除</a>' +
                '</div>' +
                '<div ng-show="row.entity.status==2">' +
                	'<a ui-sref="creditMgr.noticeDetail({id:row.entity.id})" target="_black">详情</a>' +
                    '<a ng-show="grid.appScope.hasPermit(\'creditMgr.updateNotice\')" ng-click="grid.appScope.sendOrRecoverNotice(row.entity.id, 1)"> | 收回</a>' +
                '</div>' +
            '</div>'
        }
    ];

    $scope.noticeBonusGrid = {
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

	//重置
	$scope.resetForm = function () {
    		$scope.info = {orgId:""};
	}
	$scope.resetForm();

	$scope.query = function () {
		$scope.submitting = true;
		$scope.loadImg = true;
		$http({
		    url: 'cmNotice/selectInfo?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
		    data: $scope.info,
		    method:'POST'
		}).success(function (data) {
		    $scope.submitting = false;
		    $scope.loadImg = false;
		    if (!data.status){
		        $scope.notice(data.msg);
		        return;
		    }
		    $scope.noticeBonusGrid.data = data.page.result;
		    $scope.noticeBonusGrid.totalItems = data.page.totalCount;
		}).error(function (msg) {
		    $scope.submitting = false;
		    $scope.loadImg = false;
		    $scope.notice('服务器异常,请稍后再试.');
		});
    };

    //修改弹窗提示开关
    $scope.switchStatus=function(entity){
        if(entity.popSwitch){
            $scope.serviceTitle = "确定开启？";
        } else {
            $scope.serviceTitle = "确定关闭？";
        }
        SweetAlert.swal({
                title: $scope.serviceTitle,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                	entity.popSwitch = entity.popSwitch ? 1 : 0;
                    $http({
                        url:"cmNotice/updateNoticePop?id=" + entity.id + "&popSwitch=" + entity.popSwitch,
                        method:"GET"
                    }).success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                entity.popSwitch = !entity.popSwitch;
                            }
                        });
                } else {
                    entity.popSwitch = !entity.popSwitch;
                }
            });
    };

    $scope.deleteNotice = function(id){
        SweetAlert.swal({
                title: "确定删除吗？",
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
                        url:"cmNotice/delNoticeById?id=" + id,
                        method:"GET"
                    }).success(function(data){
                        $scope.notice(data.msg);
                        if(data.status){
                            $scope.query();
                        }
                    });
                }
            });
    };
    //下发
    $scope.sendOrRecoverNotice = function(id, status){
        var title = "";
        if(status == 1){
            title = "确定收回吗？";
        } else {
            title = "确定下发吗？";
        }
    	SweetAlert.swal({
			title: title,
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
						url:"cmNotice/sendOrRecoverNotice",
						method:'POST',
                        data:{id:id, status:status}
					}).success(function (result) {
						$scope.notice(result.msg);
						if (result.status){
							$scope.query();
						}
					});
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