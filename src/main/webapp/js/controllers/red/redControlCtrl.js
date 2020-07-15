/**
 * 红包业务管理
 */
angular.module('inspinia',['uiSwitch']).controller('redControlCtrl',function(i18nService,$scope,$http,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.providerSysKey = "RED_BUSINESS_CATEGORY";// 红包业务分类系统字典表对应的KEY

    $scope.grid = {
        paginationPageSize: 10,
        paginationPageSizes: [10, 20, 50, 100],
        useExternalPagination: true,		  	//开启拓展名
        columnDefs: [
            {field: 'busCode', displayName: '活动编号'},
            {field: 'busType', displayName: '业务类型',cellFilter:'formatDropping:' + angular.toJson($scope.redBusTypeList)},
            {field: 'openStatus', displayName: '功能是否开启',cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0"' +
                    ' ng-model="row.entity.openStatus" ng-change="grid.appScope.updateOpenStatus(row.entity)" />' +
                    '</span>'
            },
            {field: 'orgStatus', displayName: '是否开启组织控制',cellTemplate:
                    '<span ng-show="row.entity.openStatus==1">' +
                    '<switch class="switch switch-s" ng-true-value="1" ng-false-value="0"' +
                    ' ng-model="row.entity.orgStatus" ng-change="grid.appScope.updateOrgStatus(row.entity)" />' +
                    '</span>'
            },
            {field: 'id', displayName: '组织管理',cellTemplate:
                    '<a class="lh30" ng-show="row.entity.openStatus==1&&row.entity.orgStatus==1"' +
                    ' ui-sref="red.redOrg({busCode:row.entity.busCode,busType:row.entity.busType})">设置</a>'
            },
            {field: 'id', displayName: '业务管理',cellTemplate:
                    '<a class="lh30" ng-show="row.entity.busType!=0 && row.entity.busType!=8" ui-sref="red.redProductControl({busType:row.entity.busType})">设置</a>'
                    +'<a class="lh30" ng-show="row.entity.busType==0"  ui-sref="red.redPersonalControl">设置</a>'
            },
            {field: 'id', displayName: '分类布局',cellTemplate:
                '<a class="lh30" ui-sref="red.redOrgSortCtrl({id:row.entity.id,busCode:row.entity.busCode,busType:row.entity.busType,orgStatus:row.entity.orgStatus})">设置</a>'
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
        $http({
            url:"red/redControl?pageNo="+$scope.paginationOptions.pageNo+"&pageSize=" + $scope.paginationOptions.pageSize,
            method:"POST"
        }).success(function(result){
            if(result.status){
                $scope.grid.data = result.data.result;
                $scope.grid.totalItems = result.data.totalCount;
            } else {
                $scope.notice(result.msg);
            }
        });
    }
    $scope.query();

    //修改开启状态
    $scope.updateOpenStatus = function(entity){
        if(entity.openStatus){
            $scope.serviceText = "确定开启？";
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
                if(entity.openStatus==true){
                    entity.openStatus=1;
                } else if(entity.openStatus==false){
                    entity.openStatus=0;
                }
                var data={"openStatus":entity.openStatus,"id":entity.id,"busType":entity.busType};
                $http({
                    url:"red/updateRedOpen",
                    method:"POST",
                    data:angular.toJson(data)
                }).success(function(result){
                    $scope.notice(result.msg);
                    if(!result.status){
                        if(entity.openStatus==true){
                            entity.openStatus = false;
                        } else {
                            entity.openStatus = true;
                        }
                    }
                });
            } else {
                if(entity.openStatus==true){
                    entity.openStatus = false;
                } else {
                    entity.openStatus = true;
                }
            }
        });
    };
    //是否开启组织控制
    $scope.updateOrgStatus = function(entity){
        if(entity.orgStatus){
            $scope.serviceText = "确定开启？";
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
                    if(entity.orgStatus==true){
                        entity.orgStatus=1;
                    } else if(entity.orgStatus==false){
                        entity.orgStatus=0;
                    }
                    var data={"orgStatus":entity.orgStatus,"id":entity.id};
                    $http({
                        url:"red/updateRedOrg",
                        method:"POST",
                        data:angular.toJson(data)
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(!result.status){
                            if(entity.orgStatus==true){
                                entity.orgStatus = false;
                            } else {
                                entity.orgStatus = true;
                            }
                        }
                    });
                } else {
                    if(entity.orgStatus==true){
                        entity.orgStatus = false;
                    } else {
                        entity.orgStatus = true;
                    }
                }
            });
    };

});