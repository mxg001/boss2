/**
 *资金险配置修改
 */
angular.module('inspinia').controller('safeConfigEditCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.addInfo={};

    $http.post("safeConfig/getSafeConfig","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.safe;
                $scope.result=data.safe.safeLadder;
            }
        });

    $scope.userGrid={                           //配置表格
        data: 'result',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'title',displayName:'单笔刷卡金额(元)',width:180 },
            { field: 'safeQuota',displayName:'保额-资金安全限额',width:180 },
            { field: 'cost',displayName:'单笔保费成本(元)',width:180 },
            { field: 'price',displayName:'单笔保费售价(元)',width:200,
                cellTemplate:
                '<div style="margin-top:7px;">'+
                '<div class="col-sm-8" ng-show="row.entity.editState==0">{{row.entity.price}}</div>' +
                '<div class="col-sm-8" ng-show="row.entity.editState==1">' +
                '<input ng-model="row.entity.price"/>' +
                '</div>'+
                '</div>'
            },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a ng-show="grid.appScope.hasPermit(\'safeConfig.saveSafeLadder\')&&row.entity.editState==0" ng-click="grid.appScope.editEntry(row.entity)"">编辑</a> ' +
            '<a ng-show="grid.appScope.hasPermit(\'safeConfig.saveSafeLadder\')&&row.entity.editState==1" ng-click="grid.appScope.saveEntry(row.entity)"">保存</a> ' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.editEntry=function (entity) {
        entity.editState=1;
    };
    $scope.saveEntry=function (entity) {
        if(entity.price==null||entity.price==""){
            $scope.notice("单笔保费售价不能为空!");
            return;
        }
        if(Number(entity.price)<0){
            $scope.notice("单笔保费售价不能小于0!");
            return;
        }
        if(Number(entity.cost)>Number(entity.price)){
            $scope.notice("单笔保费售价不得低于单笔保费成本!");
            return;
        }
        $http.post("safeConfig/saveSafeLadder",
            "info="+angular.toJson(entity),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    entity.editState=0;
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };


    $scope.submitting = false;
    //新增banner
    $scope.saveConfig = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("safeConfig/saveSafeConfig",
            "info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('safe.safeConfig',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

});