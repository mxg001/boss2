/**
 * 超级推活动设置
 */
angular.module('inspinia',['uiSwitch']).controller('cjtProfitRuleCtrl',function($scope,$http,$state,$stateParams,SweetAlert){

    //查询超级推的相关配置信息
    $scope.query = function(){
        $http.get('cjtProfitRule/selectProfitRule')
            .success(function(result){
                if(result.status){
                    $scope.baseInfo = result.data;
                    $scope.noCardRuleGrid.data = $scope.baseInfo.noCardProfitRuleList;
                    $scope.posRuleGrid.data = $scope.baseInfo.posProfitRuleList;
                    angular.forEach($scope.noCardRuleGrid.data,function(data){
                        data.action = 1;
                    })
                    angular.forEach($scope.posRuleGrid.data,function(data){
                        data.action = 1;
                    })
                } else {
                    $scope.notice(result.msg);
                }
            });
    }
    $scope.query();

    $scope.queryProfitRule = function(profitRuleNo){
        $http.get('cjtProfitRule/profitRuleDetail?profitRuleNo=' + profitRuleNo)
            .success(function(result){
                return result;
            });
    }

    $scope.noCardGridColumn = [                        //表格数据
        { field: 'profit1',displayName:'一级分润',cellTemplate:
            '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
            +' <div ng-show="row.entity.profitMode==1">{{row.entity.profit1}}'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2">{{row.entity.profit1}}%'
            +' </div>'
            +'</div>'
            +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
            +' <div ng-show="row.entity.profitMode==1" style="padding-left:30px;"><input type="text" style="width:80px;float:left" ng-model="row.entity.profit1" />'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2" style="padding-left:30px;"><input type="text" ng-model="row.entity.profit1" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
            +' </div>'
            +'</div>'
        },
        { field: 'profit2',displayName:'二级分润',cellTemplate:
            '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
            +' <div ng-show="row.entity.profitMode==1">{{row.entity.profit2}}'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2">{{row.entity.profit2}}%'
            +' </div>'
            +'</div>'
            +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
            +' <div ng-show="row.entity.profitMode==1" style="padding-left:30px;"><input type="text" style="width:80px;float:left" ng-model="row.entity.profit2" />'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2"  style="padding-left:30px;"><input type="text" ng-model="row.entity.profit2" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
            +' </div>'
            +'</div>'},
        { field: 'action',displayName:'操作',
            cellTemplate:
            '<a  class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.editShareRule(row.entity, 2)"><input type="hidden" ng-model="row.entity.action" />修改</a>'+
            '<a  class="lh30" ng-show="row.entity.action==2 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.saveShareRule(row.entity)">保存</a>' +
            '<a  class="lh30" ng-show="row.entity.action==2 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.editShareRule(row.entity, 1)"> 取消</a>'
        }
    ];

    $scope.noCardRuleGrid={                  //配置表格
        enableHorizontalScrollbar: 1,       //横向滚动条
        enableVerticalScrollbar : 1,  		//纵向滚动条
        columnDefs: $scope.noCardGridColumn
    };

    $scope.posGridColumn = [                        //表格数据
        { field: 'profit1',displayName:'一级分润',cellTemplate:
            '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
            +' <div ng-show="row.entity.profitMode==1">{{row.entity.profit1}}'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2">{{row.entity.profit1}}%'
            +' </div>'
            +'</div>'
            +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
            +' <div ng-show="row.entity.profitMode==1" style="padding-left:30px;"><input type="text" style="width:80px;float:left" ng-model="row.entity.profit1" />'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2" style="padding-left:30px;"><input type="text" ng-model="row.entity.profit1" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
            +' </div>'
            +'</div>'
        },
        { field: 'profit2',displayName:'二级分润',cellTemplate:
            '<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">'
            +' <div ng-show="row.entity.profitMode==1">{{row.entity.profit2}}'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2">{{row.entity.profit2}}%'
            +' </div>'
            +'</div>'
            +' <div class="col-sm-12 checkbox" ng-show="row.entity.action==2">'
            +' <div ng-show="row.entity.profitMode==1" style="padding-left:30px;"><input type="text" style="width:80px;float:left" ng-model="row.entity.profit2" />'
            +' </div>'
            +' <div ng-show="row.entity.profitMode==2"  style="padding-left:30px;"><input type="text" ng-model="row.entity.profit2" style="width:80px;float:left"/><span style="padding-left:5px;float:left">%</span>'
            +' </div>'
            +'</div>'},
        { field: 'action',displayName:'操作',
            cellTemplate:
            '<a  class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.editShareRule(row.entity, 2)"><input type="hidden" ng-model="row.entity.action" />修改</a>'+
            '<a  class="lh30" ng-show="row.entity.action==2 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.saveShareRule(row.entity)">保存</a>' +
            '<a  class="lh30" ng-show="row.entity.action==2 && grid.appScope.hasPermit(\'cjtProfitRule.updateProfitRule\')" ng-click="grid.appScope.editShareRule(row.entity, 1)"> 取消</a>'
        }
    ];

    $scope.posRuleGrid={                  //配置表格
        enableHorizontalScrollbar: 1,       //横向滚动条
        enableVerticalScrollbar : 1,  		//纵向滚动条
        columnDefs: $scope.posGridColumn
    };

    //修改分润
    $scope.editShareRule = function(entity, type){
        entity.action = type;
        if (type == 1){
            var result = $scope.queryProfitRule(entity.profitRuleNo);
            if(result.status) {
                entity = result.data;
            }
        }
    }

    //保存分润
    $scope.saveShareRule = function(entity){
        SweetAlert.swal({
                title: "是否保存",
                //        text: "服务状态为关闭后，不能正常交易!",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        method: "post",
                        url: "cjtProfitRule/updateProfitRule",
                        data: entity
                    }).success(function(msg){
                        if(msg.status){
                            entity.action = 1;
                        }
                        $scope.notice(msg.msg);
                    }).error(function(){
                        $scope.notice('服务异常');
                    });
                }
            }
        );
    }

    //提交超级推配置信息
    $scope.submit = function(){
        $scope.submitting = true;
        $http({
            method: "post",
            url: "cjtProfitRule/updateConfig",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
            if(result.status){
                $scope.query();
            }
            $scope.submitting = false;
        }).error(function(){
            $scope.notice('服务异常');
            $scope.submitting = false;
        });
    }

});



