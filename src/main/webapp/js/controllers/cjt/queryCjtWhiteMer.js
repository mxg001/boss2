/**
 * 超级推商户白名单（已下架的商品，白名单用户仍能看到）查询
 */
angular.module('inspinia',['uiSwitch']).controller("queryCjtWhiteMer", function($scope, $http, i18nService,$document,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusList = [{text:"全部",value:null},{text:"启用",value:1},{text:"未启用",value:0}];

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = { status: null,
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.resetForm();
    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"cjtWhiteMer/selectPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtWhiteMerGrid.data = result.data.result;
            $scope.cjtWhiteMerGrid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'merchantNo',displayName: '商户编号'},
                    {field: 'merchantName',displayName: '商户名称'},
                    {field: 'remark',displayName: '备注'},
                    {field: 'status',displayName: '状态',width:150,cellTemplate:
                        '<div ng-show="grid.appScope.hasPermit(\'cjtWhiteMer.updateStatus\')">' +
                            '<switch class="switch switch-s" ng-model="row.entity.status" ng-change="grid.appScope.updateStatus(row.entity)" /></div>'
                        +'<div class="lh30" ng-show="!grid.appScope.hasPermit(\'cjtWhiteMer.updateStatus\')"> ' +
                        '<span ng-show="row.entity.status==1">启用</span><span ng-show="row.entity.status==0">未启用</span></div>'
                    },
                    {field: 'createTime',displayName: '创建时间', cellFilter:"date:'yyyy-MM-dd HH:mm:ss'"},
                    {field: 'action',displayName: '操作',width: 120,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                        '<a class="lh30"  ng-show="grid.appScope.hasPermit(\'cjtWhiteMer.delete\')"'
                        + 'ng-click="grid.appScope.delete(row.entity.id)">删除</a>'
                        + '<a class="lh30" ng-show="grid.appScope.hasPermit(\'cjtWhiteMer.update\')" '
                        + 'ng-click="grid.appScope.updateMerModal(row.entity)"> 修改</a>'

                    }
    ];

    $scope.cjtWhiteMerGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.addMerModal = function(){
        $scope.newInfo = {type: "add"};
        $('#newInfoModal').modal('show');
    }

    $scope.updateMerModal = function(item){
        $scope.newInfo = angular.copy(item);
        $scope.newInfo.type = "update";
        $('#newInfoModal').modal('show');
    }

    $scope.cancel = function(){
        $('#newInfoModal').modal('hide');
    }

    $scope.submit = function(){
        var url = "cjtWhiteMer/update";
        if($scope.newInfo.type == "add"){
            url = "cjtWhiteMer/insert";
        }
        $http({
            method: "post",
            url: url,
            data: $scope.newInfo
        }).success(function(result){
            if(result.status){
                $scope.cancel();
                $scope.query();
            }
            $scope.notice(result.msg);
        });
    }

    //修改状态
    $scope.updateStatus = function(entity){
        if(entity.status){
            $scope.serviceTitle = "确定开启？";
        } else {
            $scope.serviceTitle = "确定关闭？";
        }
        SweetAlert.swal({
                title: $scope.serviceTitle,
                text: "",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    if(entity.status==true){
                        entity.status=1;
                    } else if(entity.status==false){
                        entity.status=0;
                    }
                    $http({
                        method: "post",
                        url: "cjtWhiteMer/updateStatus",
                        data: {"status":entity.status,"id":entity.id}
                    }).success(function(data) {
                        $scope.notice(data.msg);
                        if (data.status) {
                            $scope.query();
                        } else {
                            entity.status = !entity.status;
                        }
                    });
                } else {
                    entity.status = !entity.status;
                }
            });
    };

    //删除
    $scope.delete = function(id){
        SweetAlert.swal({
                title:  "确定删除吗?",
                text: "",
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
                        url:"cjtWhiteMer/delete?id=" + id,
                        method:"post",
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    }


    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});