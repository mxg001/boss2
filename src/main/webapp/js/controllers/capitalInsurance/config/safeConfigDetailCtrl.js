/**
 * 资金险配置详情
 */
angular.module('inspinia').controller('safeConfigDetailCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    //清空
    $scope.clear=function(){
        $scope.addInfo={oemNo:"",applyType:"1"};
    };
    $scope.clear();


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
            { field: 'safeQuota',displayName:'保额-资金安全限额',width:200,
                cellTemplate:
                '<div style="margin-top:7px;">'+
                '<div class="col-sm-8" ng-show="row.entity.editState==0">{{row.entity.safeQuota}}</div>' +
                '<div class="col-sm-8" ng-show="row.entity.editState==1">' +
                '<input ng-model="row.entity.safeQuota"/>' +
                '</div>'+
                '</div>'
            },
            { field: 'cost',displayName:'单笔保费成本(元)',width:200,
                cellTemplate:
                '<div style="margin-top:7px;">'+
                '<div class="col-sm-8" ng-show="row.entity.editState==0">{{row.entity.cost}}</div>' +
                '<div class="col-sm-8" ng-show="row.entity.editState==1">' +
                '<input ng-model="row.entity.cost"/>' +
                '</div>'+
                '</div>'
            },
            { field: 'price',displayName:'单笔保费售价(元)',width:200,
                cellTemplate:
                '<div style="margin-top:7px;">'+
                '<div class="col-sm-8" ng-show="row.entity.editState==0">{{row.entity.price}}</div>' +
                '<div class="col-sm-8" ng-show="row.entity.editState==1">' +
                '<input ng-model="row.entity.price"/>' +
                '</div>'+
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
});