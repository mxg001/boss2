/**
 * 欢乐返子类型
 */
angular.module('inspinia',['uiSwitch','infinity.angular-chosen']).controller('happyReturnTypeCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout){
    i18nService.setCurrentLang('zh-cn');

    $scope.clear = function () {
        $scope.info = {activityCode:"",orgId:null,teamEntryId:null,ruleId:null,hlfAgentRewardConfigId:null};
        $scope.showSubTeams1 = false;
    };
    $scope.clear();
    // $scope.info.subType="1";//欢乐返子类型类型,1:原来的欢乐返,2:欢乐返新活动
    $scope.types = [{text:"欢乐返",value:"009"},{text:"新欢乐送",value:"010"},{text:"超级返活动",value:"011"}];
    $scope.typesAll = angular.copy($scope.types);
    $scope.typesAll.unshift({text:"全部",value:""});
    $scope.paginationOptions = {
        pageNo: 1,
        pageSize: 10
    };

    $scope.team = [];
    $scope.subTeamMap = [];
    $scope.subTeams1 = [];
    $scope.subTeams2 = [];
    $scope.showSubTeams1 = false;
    $scope.showSubTeams2 = false;

    $scope.hasSubTeam1 = function(teamId){
        var temp = $scope.subTeamMap[teamId];
        if(null != temp && temp != undefined){
            $scope.subTeams1 = [];
            angular.forEach(temp, function (e) {
                $scope.subTeams1.push({text:e.teamEntryName,value:e.teamEntryId});
            });
            $scope.showSubTeams1 = true;

        }else {
            $scope.info.teamEntryId=null;
            $scope.showSubTeams1 = false;
        }
    }

    $scope.hasSubTeam2 = function(teamId){
        var temp = $scope.subTeamMap[teamId];
        if(null != temp && temp != undefined){
            $scope.subTeams2 = [];
            angular.forEach(temp, function (e) {
                $scope.subTeams2.push({text:e.teamEntryName,value:e.teamEntryId});
            });
            $scope.showSubTeams2 = true;
        }else {
            $scope.happyReturnType.teamEntryId=null;
            $scope.showSubTeams2 = false;
        }
    };

    $http.get('teamInfo/querySubTeams').success(function(result){
        $scope.subTeamMap = result.subTeamMap;
    });

    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
    });



    $scope.hlfMer=[];
    //活跃商户类型
    $http.get('activity/selectHlfActivityMerchantRuleAllInfo')
        .success(function(result){
            if(!result)
                return;
            //响应成功
            for(var i=0; i<result.length; i++){
                $scope.hlfMer.push({value:result[i].ruleId,text:result[i].ruleId + " " + result[i].ruleName});
            }
        });

    $scope.hlfAgent=[];
    //代理商活动奖励类型
    $http.get('activity/selectHlfActivityAgentRuleAllInfo')
        .success(function(result){
            if(!result)
                return;
            //响应成功
            for(var i=0; i<result.length; i++){
                $scope.hlfAgent.push({value:result[i].id,text:result[i].id + " " + result[i].activityName});
            }
        });

    $scope.xhlfSmartConfigs=[];
    //代理商活动奖励类型
    $http.get('activity/selectXhlfSmartAllInfo')
        .success(function(result){
            if(!result)
                return;
            //响应成功
            for(var i=0; i<result.length; i++){
                $scope.xhlfSmartConfigs.push({value:result[i].id,text:result[i].id + " " + result[i].activityName});
            }
        });


    // 欢乐返子类型的表格
    $scope.gridOptions={                           //配置表格
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量
        columnDefs:[                           //表格数据
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:170},
            { field: 'activityTypeName',displayName:'欢乐返子类型名称',width:150},
            { field: 'orgName',displayName:'所属组织',width:150},
            { field: 'teamEntryName',displayName:'所属子组织',width:150},
            { field: 'activityCode',displayName:'欢乐返类型',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.types)},
            { field: 'ruleId',displayName:'活跃商户活动ID',width:150,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.ruleId==null?"未参与":row.entity.ruleId}}</div>'},
            { field: 'ruleName',displayName:'活跃商户活动名称',width:150},
            { field: 'hlfAgentRewardConfigId',displayName:'代理商奖励活动ID',width:150,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.hlfAgentRewardConfigId==null?"未参与":row.entity.hlfAgentRewardConfigId}}</div>'},
            { field: 'activityName',displayName:'代理商奖励活动名称',width:150},
            { field: 'xhlfSmartConfigId',displayName:'新欢乐送智能版活动ID',width:150,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.xhlfSmartConfigId==null?"未参与":row.entity.xhlfSmartConfigId}}</div>'},
            { field: 'smartActivityName',displayName:'新欢乐送智能版活动名称',width:150},
            { field: 'transAmount',displayName:'交易金额',width:150},
            { field: 'cashBackAmount',displayName:'返现金额',width:170},
            { field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:170},
            { field: 'emptyAmount',displayName:'首次注册不满扣(元)',width:170},
            { field: 'fullAmount',displayName:'首次注册满奖(元)',width:170},
            { field: 'repeatEmptyAmount',displayName:'重复注册不满扣(元)',width:170},
            { field: 'repeatFullAmount',displayName:'重复注册满奖(元)',width:170},
            { field: 'updateAgentStatus',displayName:'允许代理商更改',width:200,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.updateAgentStatus" ng-change="grid.appScope.updateAgentStatus(row)" /></span>'
            },
            { field: 'remark',displayName:'备注',width:100},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,
                cellTemplate:
                '<a class="lh30" target="_blank" ui-sref="func.happyReturnTypeDetail({id:row.entity.activityTypeNo})">详情</a>'+
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.editHappyReturnType\')" ng-click="grid.appScope.editHappyReturnType(row.entity.activityTypeNo)"> | 修改</a>'+
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'activity.deleteHappyReturnType\')" ng-click="grid.appScope.deleteHappyReturnType(row.entity.activityTypeNo)"> | 删除</a>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    $scope.query = function () {
        $http({
            url: 'activity/selectHappyReturnType?pageNo=' + $scope.paginationOptions.pageNo + "&pageSize=" + $scope.paginationOptions.pageSize,
            data: $scope.info,
            method: 'POST'
        }).success(function (data) {
            if(data.status){
                $scope.gridOptions.data = data.page.result;
                $scope.gridOptions.totalItems = data.page.totalCount;
                $scope.gridOptions.totalItems = data.page.totalCount;
            }else{
                $scope.notice(data.msg);
            }
        }).error(function(){
        });
    };
    $scope.query();


    $scope.commitType=1;//1-新增，2-修改

    $scope.oldConfigId=null;
    //修改
    $scope.editHappyReturnType = function(activityTypeNo){
        $http.post("activity/queryByActivityHardwareType",activityTypeNo).success(function (data) {
            if(data.status){
                $scope.happyReturnType = data.param;
                $("#activityCode").attr("disabled","disabled");
                $("#orgId").attr("disabled","disabled");
                $("#teamEntryId").attr("disabled","disabled");
                $("#hardWardAddModel").modal("show");
                $("#activityTypeNo").show();
                $scope.hasSubTeam2($scope.happyReturnType.orgId);
                $scope.oldConfigId=$scope.happyReturnType.hlfAgentRewardConfigId;
                $scope.commitType=2;
                //$scope.showBySubType();
                // if($scope.happyReturnType.subType=="2"){
                //     $("#subType1").hide();
                //     $("#subType2").show();
                // }else{
                //     $("#subType1").show();
                //     $("#subType2").hide();
                // }
            }
        })
    };

    // $("#subType1").show();
    // $("#subType2").hide();
    // $("#activityCode").change(function(){
    //     $scope.showBySubType();
    // });
    //
    // $scope.showBySubType=function(){
    //     var selVal = $("#activityCode").children('option:selected').val();
    //     //$scope.notice(selVal);
    //     if(selVal=="string:010"){
    //         $("#subType1").hide();
    //         $("#subType2").show();
    //     }else{
    //         $("#subType1").show();
    //         $("#subType2").hide();
    //     }
    // }

    //添加
    $scope.hardWardAddModel = function(){
        $scope.commitType=1;
        $("#hardWardAddModel").modal("show");
        // $("#subType1").show();
        // $("#subType2").hide();

        $("#orgId").removeAttr("disabled","disabled");
        $("#teamEntryId").removeAttr("disabled","disabled");
        $("#activityCode").removeAttr("disabled");
        $("#activityTypeNo").hide();
        $scope.showSubTeams2=false;
        $scope.happyReturnType={agentTransTotalType:1,activityCode:'009',orgId:null,teamEntryId:null,hardId:"",price:"",targetAmout:"",cashBackAmount:"",emptyAmount:"",ruleId:"",
            fullAmount:"",cashLastAllyAmount:"",cashLastTeamAmount:"",repeatRegisterAmount:"",
            emptyAmount:"",fullAmount:"",repeatEmptyAmount:"",repeatFullAmount:"",activityDetails:"",oneSubTransAmount:"",oneSubRewardAmount:"",oneSubRepeatReward:""};
    };
    //返回
    $scope.cancel=function(){
        $scope.happyReturnType={};
        $('#hardWardAddModel').modal('hide');
    };

    //校验唯一性
    $scope.checkUnique = function(){
        if($scope.happyReturnType.activityTypeName){
            $http.post("activity/queryByActivityTypeName",$scope.happyReturnType).success(function (data) {
                if(data.status){
                    $scope.notice("欢乐返子类型名称已存在！");
                    $scope.happyReturnType.activityTypeName=data.param.activityTypeName;
                }
            })
        }
    };

    //新增欢乐返子类型
    $scope.saveHappyReturnType = function(){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.happyReturnType.activityCode == undefined || $scope.happyReturnType.activityCode == null || $scope.happyReturnType.activityCode==""){
            $scope.notice("请选择欢乐返类型!");
            return;
        }

         //判断所属组织
             if($scope.commitType==1&&$scope.happyReturnType.orgId==null){
                $scope.notice("请选择所属组织！");
               return;
            }
        // 所属子组织必填
             if($scope.commitType==1&&$scope.subTeamMap[$scope.happyReturnType.orgId]!=null &&  $scope.happyReturnType.teamEntryId == null ){
               $scope.notice("请选择所属子组织！");
                return;
            }

        if($scope.happyReturnType.activityTypeName == null || $scope.happyReturnType.activityTypeName==""){
            $scope.notice("欢乐返子类型名称不能为空!");
            return;
        }
        if($scope.happyReturnType.transAmount==null || $scope.happyReturnType.transAmount===""){
            $scope.notice("交易金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.transAmount!=0&&!isNum.test($scope.happyReturnType.transAmount)){
                $scope.notice("交易金额格式不正确!");
                return;
            }
        }

        if($scope.happyReturnType.cashBackAmount==null || $scope.happyReturnType.cashBackAmount===""){
            $scope.notice("返现金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.cashBackAmount!=0&&!isNum.test($scope.happyReturnType.cashBackAmount)){
                $scope.notice("返现金额格式不正确!");
                return;
            }
        }

        if($scope.happyReturnType.repeatRegisterAmount==null || $scope.happyReturnType.repeatRegisterAmount===""){
            $scope.notice("重复注册返现金额不能为空!");
            return;
        }else{
            if($scope.happyReturnType.repeatRegisterAmount!=0&&!isNum.test($scope.happyReturnType.repeatRegisterAmount)){
                $scope.notice("重复注册返现金额格式不正确!");
                return;
            }
        }

        if($scope.happyReturnType.activityCode=="009") {
            if ($scope.happyReturnType.emptyAmount == null || $scope.happyReturnType.emptyAmount === "") {
                $scope.notice("首次注册不满扣N值金额不能为空!");
                return;
            } else {
                if ($scope.happyReturnType.emptyAmount != 0 && !isNum.test($scope.happyReturnType.emptyAmount)) {
                    $scope.notice("首次注册不满扣N值金额格式不正确!");
                    return;
                }
            }
            if ($scope.happyReturnType.fullAmount == null || $scope.happyReturnType.fullAmount === "") {
                $scope.notice("首次注册满奖M值金额不能为空!");
                return;
            } else {
                if ($scope.happyReturnType.fullAmount != 0 && !isNum.test($scope.happyReturnType.fullAmount)) {
                    $scope.notice("首次注册满奖M值金额格式不正确!");
                    return;
                }
            }

            if ($scope.happyReturnType.repeatEmptyAmount == null || $scope.happyReturnType.repeatEmptyAmount === "") {
                $scope.notice("重复注册不满扣N值金额不能为空!");
                return;
            } else {
                if ($scope.happyReturnType.repeatEmptyAmount != 0 && !isNum.test($scope.happyReturnType.repeatEmptyAmount)) {
                    $scope.notice("重复注册不满扣N值金额格式不正确!");
                    return;
                }
            }
            if ($scope.happyReturnType.repeatFullAmount == null || $scope.happyReturnType.repeatFullAmount === "") {
                $scope.notice("重复注册满奖M值金额不能为空!");
                return;
            } else {
                if ($scope.happyReturnType.repeatFullAmount != 0 && !isNum.test($scope.happyReturnType.repeatFullAmount)) {
                    $scope.notice("重复注册满奖M值金额格式不正确!");
                    return;
                }
            }
        }
        if ($scope.happyReturnType.activityDetails == null || $scope.happyReturnType.activityDetails === "") {
            $scope.notice("活动说明不能为空!");
            return;
        }
        if ($scope.happyReturnType.oneSubTransAmount > 0
            && (
                ($scope.happyReturnType.oneSubRewardAmount == null || $scope.happyReturnType.oneSubRewardAmount === "")
                ||
                ($scope.happyReturnType.oneSubRepeatReward == null || $scope.happyReturnType.oneSubRepeatReward === "")
            )
        ) {
            $scope.notice("第一次子考核，累计交易金额大于0时，首次注册奖励跟重复注册奖励不能为空!");
            return;
        }
        if ($scope.happyReturnType.oneSubTransAmount != null && $scope.happyReturnType.oneSubTransAmount != 0 && !isNum.test($scope.happyReturnType.oneSubTransAmount)) {
            $scope.notice("如累计交易格金额式不正确!");
            return;
        }
        if ($scope.happyReturnType.oneSubRewardAmount != null && $scope.happyReturnType.oneSubRewardAmount != 0 && !isNum.test($scope.happyReturnType.oneSubRewardAmount)) {
            $scope.notice("首次注册奖励金额格式不正确!");
            return;
        }
        if ($scope.happyReturnType.oneSubRepeatReward != null && $scope.happyReturnType.oneSubRepeatReward != 0 && !isNum.test($scope.happyReturnType.oneSubRepeatReward)) {
            $scope.notice("重复注册奖励金额格式不正确!");
            return;
        }
        if ($scope.submitting == true) {
            return;
        }
        if($scope.happyReturnType.activityCode=="011"){
            $scope.happyReturnType.merchantLimitDays=0;
            $scope.happyReturnType.merchantTransAmount=0;
            $scope.happyReturnType.merchantRewardAmount=0;
            $scope.happyReturnType.merchantRepeatRewardAmount=0;
        }
        $scope.submitting = true;
        if($scope.happyReturnType.activityTypeNo == undefined || $scope.happyReturnType.activityTypeNo == null || $scope.happyReturnType.activityTypeNo==""){
            // $scope.happyReturnType.subType=$scope.info.subType;
            $http.post("activity/addHappyReturnType",$scope.happyReturnType)
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#saveHappyReturnType").modal("hide");
                        $scope.cancel();
                        $scope.query();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
            $scope.clear = function () {
                $scope.info = {};
            };
        }else{
            if($scope.oldConfigId!=null&&$scope.happyReturnType.hlfAgentRewardConfigId==null){
                SweetAlert.swal({
                        title: "警告!",
                        text: "取消代理商奖励活动，将会把全层级代理商已关联设置好的参数全部清空，是否继续操作？",
                        //type: "info",
                        showCancelButton: true,
                        confirmButtonColor: "#DD6B55",
                        confirmButtonText: "确认",
                        cancelButtonText: "取消",
                        closeOnConfirm: true,
                        closeOnCancel: true },
                    function (isConfirm) {
                        if (isConfirm) {
                            $scope.updHappyReturnType();
                        }
                    });
            }else{
                $scope.updHappyReturnType();
            }






        }

    };
    $scope.updHappyReturnType = function(){
        $http.post("activity/editHappyReturnType",$scope.happyReturnType)
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $("#saveHappyReturnType").modal("hide");
                    $scope.cancel();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
        $scope.clear = function () {
            $scope.info = {};
        };
    }

    //删除欢乐返子类型
    $scope.deleteHappyReturnType = function(activityTypeNo){
        SweetAlert.swal({
                title: "确认删除欢乐返子类型?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('activity/deleteHappyReturnType',activityTypeNo)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.query();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    };

    $scope.updateAgentStatus=function(row){
        if(row.entity.updateAgentStatus){
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
                    if(row.entity.updateAgentStatus==true){
                        row.entity.updateAgentStatus=1;
                    } else if(row.entity.updateAgentStatus==false){
                        row.entity.updateAgentStatus=0;
                    }
                    $http.post("activity/updateAgentStatusSwitch",row.entity)
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
                                $scope.query();
                            }else{
                                if(row.entity.updateAgentStatus==true){
                                    row.entity.updateAgentStatus = false;
                                } else {
                                    row.entity.updateAgentStatus = true;
                                }
                                $scope.notice("操作失败！");
                            }
                        })
                        .error(function(data){
                            if(row.entity.updateAgentStatus==true){
                                row.entity.updateAgentStatus = false;
                            } else {
                                row.entity.updateAgentStatus = true;
                            }
                            $scope.notice("服务器异常")
                        });
                } else {
                    if(row.entity.updateAgentStatus==true){
                        row.entity.updateAgentStatus = false;
                    } else {
                        row.entity.updateAgentStatus = true;
                    }
                }
            });

    };

    $scope.export = function () {
        SweetAlert.swal({
                title: "确认导出？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.exportInfoClick("activity/exportActivityHardwareType",{"info" : angular.toJson($scope.info)});
                }
            });
    };

});
