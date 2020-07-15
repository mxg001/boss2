/**
 * 公告管理
 */
angular.module('inspinia',['uiSwitch']).controller('noticeCtrl',function($scope,$http,i18nService,$document,$window,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.noticeStatus = [{value:"-1",text:"全部"},{value:"0",text:"待发布"},{value:"1",text:"已发布"},{value:"2",text:"已废弃"}];
    $scope.popStatusAll = [{value:"",text:"全部"},{value:"0",text:"已关闭-非弹窗提示"},{value:"1",text:"已开启-弹窗提示"}];

    $scope.columnDefs = [
        {field: 'id',displayName: '公告ID',width: 100,pinnable: false,sortable: false},
        {field: 'title',displayName: '公告标题',width: 150,pinnable: false,sortable: false},
        {field: 'orgId',displayName: '下发对象',width: 150,pinnable: false,sortable: false},
        {field: 'popSwitch',displayName: '弹窗公告提示开关',width:180,pinnable: false,sortable: false,cellTemplate:
            '<span><switch class="switch switch-s" ng-model="row.entity.popSwitch" ng-change="grid.appScope.switchProfitStatus(row)" /></span>'
/*
            +'<span ng-show="!grid.appScope.hasPermit(\'agent.switchProfitStatus\')"> <span ng-show="row.entity.profitSwitch==1">开启</span><span ng-show="row.entity.profitSwitch==0">关闭</span></span>'
*/
        },
        {field: 'status',displayName: '公告状态',width: 150,pinnable: false,sortable: false,
        	cellFilter:"formatDropping:" + angular.toJson($scope.noticeStatus)
        },
        {field: 'createDate',displayName: '创建时间',width: 180,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'sendTime',displayName: '下发时间',width: 180,cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
        {field: 'sendByName',displayName: '下发人',width: 150},
        {field: 'remark',displayName: '备注',width: 180},
        {field: 'action',displayName: '操作',width: 180,pinnedRight:true,
        	cellTemplate:
        	'<div  class="lh30">' +
                '<div ng-show="row.entity.status==0">'+
                '<a ng-show="grid.appScope.hasPermit(\'superBank.sendNotice\')" ng-click="grid.appScope.sendNotice(row.entity.id, 1)">下发</a>' +
                '<a ng-show="grid.appScope.hasPermit(\'superBank.updNotice\')" ui-sref="superBank.updateNotice({id:row.entity.id})">  修改</a>' +
                '<a ng-show="grid.appScope.hasPermit(\'superBank.deleteNotice\')" ng-click="grid.appScope.deleteNotice(row.entity.id)">  删除</a>' +
            '<a  ui-sref="superBank.noticeDetail({id:row.entity.id})"> 详情</a>' +
                '</div>'+
                '<div ng-show="row.entity.status==1">'+
                    '<a ng-show="grid.appScope.hasPermit(\'superBank.sendNotice\')" ng-click="grid.appScope.sendNotice(row.entity.id, 0)"> 收回</a>' +
            '<a  ui-sref="superBank.noticeDetail({id:row.entity.id})"> 详情</a>' +
                '</div>'+
            ' </div>'
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

    //修改弹窗提示开关
    $scope.switchProfitStatus=function(row){
        if(row.entity.popSwitch){
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
                    if(row.entity.popSwitch==true){
                        row.entity.popSwitch=1;
                    } else if(row.entity.popSwitch==false){
                        row.entity.popSwitch=0;
                    }
                    var data={"popSwitch":row.entity.popSwitch,"id":row.entity.id};
                    // $http.post("superBank/updateNoticePop",angular.toJson(data))
                    $http({
                        url:"superBank/updateNoticePop",
                        method:"POST",
                        data:{id:row.entity.id, popSwitch:row.entity.popSwitch}
                    }).success(function(data){
                            $scope.notice(data.msg);
                            if(data.status){
                                $scope.query();
                            }else{
                                row.entity.popSwitch = !row.entity.popSwitch;
                            }
                        });
                } else {
                    row.entity.popSwitch = !row.entity.popSwitch;
                }
            });
    };


    //deleteNotice
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
                        url:"superBank/deleteNotice",
                        method:"POST",
                        data:{"id":id}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    };
    //下发
    $scope.sendNotice = function(id, status){
        var title = "";
        if(status == 0){
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
						url:"superBank/sendNotice",
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
    
    //重置
    $scope.resetForm = function () {
    		$scope.entityData = {status:"-1",orgId:-1,popSwitch:"",
                // startReleaseTime:moment(new Date().getTime()-2*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
                // endReleaseTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
            };
    }
    $scope.resetForm();
    //返回上页
    $scope.rgoback = function () {
    	history.back();
    }
    
    $scope.query = function () {
        $scope.submitting = true;
        $scope.loadImg = true;
        
        $http({
            url: 'superBank/noticeList?pageNo='+$scope.paginationOptions.pageNo+'&pageSize='+$scope.paginationOptions.pageSize,
            data: $scope.entityData,
            method:'POST'
        }).success(function (result) {
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result.status){
                $scope.notice(result.msg);
                
                return;
            }
            $scope.noticeBonusGrid.data = result.data.result;
            $scope.noticeBonusGrid.totalItems = result.data.totalCount;
        }).error(function (msg) {
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice('服务器异常,请稍后再试.');
        });
    };
   
    $scope.addNotice = function(){
    	$("#addNotice").modal("show");
    };
    
    $scope.cancel = function(){
    	$("#addNotice").modal("hide");
    };
    
    //保存数据
    $scope.saveNotice = function(){
    	 var datas = {
 			 	"title" : $scope.noticeData.title,
 				"link":$scope.noticeData.link
 		};
      	 
     $http.post("superBank/addNotice",angular.toJson(datas))
 		.success(function(data){
 			if(data.status){
 				$scope.notice(data.msg);
 				$scope.cancel();//关闭弹窗
 				$scope.query();//重新查询
 			}else{
 				$scope.notice(data.msg);
 				$scope.submitting = false;
 			}
 		});
    };

    //获取所有的银行家组织
    $scope.orgInfoList = [{orgId:-1,orgName:"全部"}];
    $scope.getOrgInfoList = function () {
        $http({
            url:"superBank/getOrgInfoList",
            method:"POST"
        }).success(function(msg){
            if(msg.status){
                $scope.orgInfoList = msg.data;
                $scope.orgInfoList.unshift({orgId:-1,orgName:"全部"});
            } else {
                $scope.notice("获取组织信息异常");
            }
        }).error(function(){
            $scope.notice("获取组织信息异常");
        })
    };
    $scope.getOrgInfoList();

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        });
    });
});