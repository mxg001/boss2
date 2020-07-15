/**
 * 欢乐返活动设置
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('happyReturnSetUpCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal,$timeout){
i18nService.setCurrentLang('zh-cn');

    $scope.info={startTime:"",endTime:"",cumulateTransDay:"",cumulateAmountMinus:"",cumulateAmountAdd:""};
    $scope.submitting = false;
    $scope.subType ="1";
    $scope.hardWare={};
    $scope.repeatRegist={};
    $scope.queryInfo={};
    $scope.types = [{text:"欢乐返",value:"009"},{text:"新欢乐送",value:"010"},{text:"超级返活动",value:"011"}];
    $scope.typeNos1=[];
    $scope.typeNos2=[];
    $scope.typeNos3=[];
    $scope.typeNos4=[];
    $scope.saveStatus=0;//0保存1修改
    $scope.team=[];

    $scope.clear009= function(){
        $scope.queryInfo={};
    }


    $scope.hardes=[{value:"",text:"全部"}];
    //机具类型
    $http.get('hardwareProduct/selectAllInfo.do')
        .success(function(result){
            if(!result)
                return;
            //响应成功
            $scope.termianlTypes=result;
            for(var i=0; i<result.length; i++){
                $scope.hardes.push({value:result[i].hpId,text:result[i].hpId + " " + result[i].typeName});
            }
        });
    //动态机具类型
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.hardess = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('hardwareProduct/selectAllInfo.do','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            if(response.data.length==0) {
                                $scope.hardess.push({value: "", text: "全部"});
                            }else{
                                $scope.hardess.push({value: "", text: "全部"});
                                for(var i=0; i<response.data.length; i++){
                                    $scope.hardess.push({value:response.data[i].hpId,text:response.data[i].hpId + " " + response.data[i].typeName});
                                }
                            }
                            $scope.hardes = $scope.hardess;
                            oldValue = value;
                        });
                },800);
        }
    };

	// 欢乐返-循环送的表格
    $scope.gridOptions008={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据

            { field: 'typeName',displayName:'硬件产品名称',width:200},
            { field: 'hardId',displayName:'硬件产品ID',width:200},
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:200},
            { field: 'activityTypeName',displayName:'欢乐返子类型',width:200},
            { field: 'teamName',displayName:'所属组织',width:200},
            { field: 'teamEntryName',displayName:'所属子组织',width:200},
            { field: 'transAmount',displayName:'交易金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.transAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.transAmount"/></div>'},
            { field: 'cashBackAmount',displayName:'返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cashBackAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.cashBackAmount"/></div>'},
            { field: 'cashLastAllyAmount',displayName:'返盟主金额',width:150},
            { field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatRegisterAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'emptyAmount',displayName:'首次注册不满扣N值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.emptyAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.emptyAmount"/></div>'},
            { field: 'fullAmount',displayName:'首次注册满奖M值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fullAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fullAmount"/></div>'},
            { field: 'repeatEmptyAmount',displayName:'重复注册不满扣N值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatEmptyAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatEmptyAmount"/></div>'},
            { field: 'repeatFullAmount',displayName:'重复注册满奖M值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatFullAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatFullAmount"/></div>'},
            { field: 'defaultStatus',displayName:'是否取默认活动内容',width:150,cellFilter:"formatDropping:" + angular.toJson($scope.bool)},
            { field: 'cumulateTransDay',displayName:'累计交易奖励时间',width:150},
            { field: 'cumulateTransMinusDay',displayName:'累计交易扣费时间',width:150},
            { field: 'cumulateAmountMinus',displayName:'累计交易（扣）',width:150,cellFilter:"currency:''"},
            { field: 'cumulateAmountAdd',displayName:'累计交易（奖）',width:150,cellFilter:"currency:''"},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,
                cellTemplate:
                '<a class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'activity.editHlfHardWare\')" ng-click="grid.appScope.actHardwareEdit(row.entity)"><input type="hidden" ng-model="row.entity.action" />修改</a>'
            }
        ]
    };
	// 欢乐返的表格
    $scope.gridOptions009={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'typeName',displayName:'硬件产品名称',width:200},
            { field: 'hardId',displayName:'硬件产品ID',width:200},
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:200},
            { field: 'activityTypeName',displayName:'欢乐返子类型',width:200},
            { field: 'teamName',displayName:'所属组织',width:200},
            { field: 'teamEntryName',displayName:'所属子组织',width:200},
            { field: 'activityMerchantId',displayName:'活跃商户活动ID',width:200,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.activityMerchantId==null?"未参加":row.entity.activityMerchantId}}</div>'},
            { field: 'activityAgentId',displayName:'代理商奖励活动ID',width:200,
                cellTemplate:'<div class="col-sm-12 checkbox">{{row.entity.activityAgentId==null?"未参加":row.entity.activityAgentId}}</div>'},
            { field: 'transAmount',displayName:'交易金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.transAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.transAmount"/></div>'},
            { field: 'cashBackAmount',displayName:'返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cashBackAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.cashBackAmount"/></div>'},
            { field: 'cashLastAllyAmount',displayName:'返盟主金额',width:150},
            /*{ field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatRegisterAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},*/
            { field: 'emptyAmount',displayName:'首次注册不满扣N值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.emptyAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.emptyAmount"/></div>'},
            { field: 'fullAmount',displayName:'首次注册满奖M值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fullAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fullAmount"/></div>'},
            { field: 'repeatEmptyAmount',displayName:'重复注册不满扣N值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatEmptyAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatEmptyAmount"/></div>'},
            { field: 'repeatFullAmount',displayName:'重复注册满奖M值',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatFullAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatFullAmount"/></div>'},
            { field: 'defaultStatus',displayName:'是否取默认活动内容',width:150,cellFilter:"formatDropping:" +  angular.toJson($scope.bool)},
            { field: 'cumulateTransMinusDay',displayName:'首次注册累计交易扣费时间(天)',width:240,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cumulateTransMinusDay}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'cumulateTransDay',displayName:'首次注册累计交易奖励时间(天)',width:240,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cumulateTransDay}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'repeatCumulateTransMinusDay',displayName:'重复注册累计交易扣费时间(天)',width:240,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatCumulateTransMinusDay}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'repeatCumulateTransDay',displayName:'重复注册累计交易奖励时间(天)',width:240,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatCumulateTransDay}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,
                cellTemplate:
                '<a class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'activity.editHlfHardWare\')" ng-click="grid.appScope.actHardwareEdit(row.entity)"><input type="hidden" ng-model="row.entity.action" />修改</a>'
            }
        ]
    };

    // 新欢乐送活动的表格
    $scope.gridOptions010={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'typeName',displayName:'硬件产品名称',width:200},
            { field: 'hardId',displayName:'硬件产品ID',width:200},
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:200},
            { field: 'activityTypeName',displayName:'欢乐返子类型',width:200},
            { field: 'teamName',displayName:'所属组织',width:200},
            { field: 'teamEntryName',displayName:'所属子组织',width:200},
            { field: 'transAmount',displayName:'交易金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.transAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.transAmount"/></div>'},
            { field: 'cashBackAmount',displayName:'首次注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cashBackAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.cashBackAmount"/></div>'},
            { field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatRegisterAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'oneRewardAmount',displayName:'第1次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.oneRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.oneRewardAmount"/></div>'},
            { field: 'oneRepeatRewardAmount',displayName:'第1次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.oneRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.oneRepeatRewardAmount"/></div>'},
            { field: 'twoRewardAmount',displayName:'第2次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.twoRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.twoRewardAmount"/></div>'},
            { field: 'twoRepeatRewardAmount',displayName:'第2次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.twoRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.twoRepeatRewardAmount"/></div>'},
            { field: 'threeRewardAmount',displayName:'第3次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.threeRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.threeRewardAmount"/></div>'},
            { field: 'threeRepeatRewardAmount',displayName:'第3次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.threeRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.threeRepeatRewardAmount"/></div>'},
            { field: 'fourRewardAmount',displayName:'第4次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fourRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fourRewardAmount"/></div>'},
            { field: 'fourRepeatRewardAmount',displayName:'第4次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fourRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fourRepeatRewardAmount"/></div>'},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,
                cellTemplate:
                    '<a class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'activity.editHlfHardWare\')" ng-click="grid.appScope.actHardwareEdit(row.entity)"><input type="hidden" ng-model="row.entity.action" />修改</a>'
            }
        ]
    };


    // 超级返活动的表格
    $scope.gridOptions011={						//配置表格
        paginationPageSize:10,					//分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        enableHorizontalScrollbar: 1,			//横向滚动条
        enableVerticalScrollbar : 1,			//纵向滚动条
        columnDefs:[							//表格数据
            { field: 'typeName',displayName:'硬件产品名称',width:200},
            { field: 'hardId',displayName:'硬件产品ID',width:200},
            { field: 'activityTypeNo',displayName:'欢乐返子类型编号',width:200},
            { field: 'activityTypeName',displayName:'欢乐返子类型',width:200},
            { field: 'teamName',displayName:'所属组织',width:200},
            { field: 'teamEntryName',displayName:'所属子组织',width:200},
            { field: 'transAmount',displayName:'交易金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.transAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.transAmount"/></div>'},
            { field: 'cashBackAmount',displayName:'首次注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.cashBackAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.cashBackAmount"/></div>'},
            { field: 'repeatRegisterAmount',displayName:'重复注册返现金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.repeatRegisterAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.repeatRegisterAmount"/></div>'},
            { field: 'oneRewardAmount',displayName:'第1次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.oneRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.oneRewardAmount"/></div>'},
            { field: 'oneRepeatRewardAmount',displayName:'第1次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.oneRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.oneRepeatRewardAmount"/></div>'},
            { field: 'twoRewardAmount',displayName:'第2次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.twoRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.twoRewardAmount"/></div>'},
            { field: 'twoRepeatRewardAmount',displayName:'第2次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.twoRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.twoRepeatRewardAmount"/></div>'},
            { field: 'threeRewardAmount',displayName:'第3次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.threeRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.threeRewardAmount"/></div>'},
            { field: 'threeRepeatRewardAmount',displayName:'第3次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.threeRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.threeRepeatRewardAmount"/></div>'},
            { field: 'fourRewardAmount',displayName:'第4次考核奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fourRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fourRewardAmount"/></div>'},
            { field: 'fourRepeatRewardAmount',displayName:'第4次考核重复注册奖励金额',width:150,cellFilter:"currency:''",
                cellTemplate:'<div class="col-sm-12 checkbox" ng-show="row.entity.action==1">{{row.entity.fourRepeatRewardAmount}}</div><div class="col-sm-12 checkbox" ng-show="row.entity.action==2"><input ng-model="row.entity.fourRepeatRewardAmount"/></div>'},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,
                cellTemplate:
                    '<a class="lh30" ng-show="row.entity.action==1 && grid.appScope.hasPermit(\'activity.editHlfHardWare\')" ng-click="grid.appScope.actHardwareEdit(row.entity)"><input type="hidden" ng-model="row.entity.action" />修改</a>'
            }
        ]
    };
    $scope.allHard=[];
    $scope.removeHardIdDate=function(entity){
        if($scope.allHard!=null&&$scope.allHard.length>0){
            angular.forEach($scope.allHard, function(item,index) {
                if(entity.hpId==item.hpId){
                    $scope.allHard.splice(index, 1);
                }
            });
        }
    };
    $scope.activityTypeStr="";
    $scope.getharPro=function(){
        $scope.allHardQuery=[];
        if($scope.activityTypeStr==null&&$scope.activityTypeStr==""){
            $scope.allHardQuery = angular.copy($scope.termianlTypes);
        }else{
            $http.post('hardwareProduct/selectAllInfo.do','item=' + $scope.activityTypeStr,
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .then(function (response) {
                    if(response.data.length>0) {
                        $scope.allHardQuery=response.data;
                    }
                });
        }
    };

    //硬件设备表格
    $scope.hardIdGrid = { // 配置表格
        data : 'allHard',
        enableHorizontalScrollbar : 1, // 去掉滚动条
        enableVerticalScrollbar : 1, // 去掉滚动条
        columnDefs : [ // 表格数据
            {field : 'typeName', displayName : '硬件产品', width:150},
            {field : 'orgName', displayName : '所属组织', width:150},
            { field: 'action',displayName:'操作',width:150,cellTemplate:
                '<div class="lh30">'+
                        '<a ng-click="grid.appScope.removeHardIdDate(row.entity)">移除</a> ' +
                '</div>'

            }
        ],
        onRegisterApi : function(gridApi) {
            $scope.gridApiHard = gridApi;
        }
    };

    $scope.queryHardIdGrid = { // 配置表格
        data : 'allHardQuery',
        enableHorizontalScrollbar : 1, // 去掉滚动条
        enableVerticalScrollbar : 1, // 去掉滚动条
        enableSelectAll : false,
        columnDefs : [ // 表格数据
            {field : 'typeName', displayName : '硬件产品', width:150},
            {field : 'orgName', displayName : '所属组织', width:150}],
        onRegisterApi : function(gridApi) {
            $scope.queryHardIdGridApi = gridApi;
            // //单选
            $scope.queryHardIdGridApi.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    var item=row.entity;
                    if($scope.allHard!=null&&$scope.allHard.length>0){
                        for(var i=0;i<$scope.allHard.length;i++){
                            var item1=$scope.allHard[i];
                            if(item1.hpId==item.hpId){
                                return;
                            }
                        }
                    }
                    $scope.allHard.push(item);
                }else{
                    $scope.removeHardIdDate(row.entity);
                }
            });
        }
    };
	//

    $scope.checkActivityCode = function(activityCode){
        $scope.typeNos=[];
        $scope.hardTypeNos=[];
        if(activityCode=="008"){
            $scope.typeNos=$scope.typeNos1;
            $scope.hardTypeNos=$scope.hardTypeNos1;
        }else if(activityCode=="009"){
            $scope.typeNos=$scope.typeNos2;
            $scope.hardTypeNos=$scope.hardTypeNos2;
        }else if(activityCode=="010"){
            $scope.typeNos=$scope.typeNos3;
            $scope.hardTypeNos=$scope.hardTypeNos3;
        }else if(activityCode=="011"){
            $scope.typeNos=$scope.typeNos4;
            $scope.hardTypeNos=$scope.hardTypeNos4;
        }
    };

    $scope.checkActivityCode1 = function(){
        $scope.typeNos1=[];
        $scope.hardTypeNos1=[];
        $http.post("activity/queryByactivityTypeNoList","008").success(function (data) {
            if(data.status){
                $scope.hardTypeNos1=data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos1.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName});
                }
            }
        })
    };
    $scope.checkActivityCode1();
    $scope.checkActivityCode2 = function(){
        $scope.typeNos2=[];
        $scope.hardTypeNos2=[];
        $http.post("activity/queryByactivityTypeNoList","009").success(function (data) {
            if(data.status){
                $scope.hardTypeNos2=data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos2.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName,transAmount:data.info[i].transAmount});
                }
            }
        })
    };
    $scope.checkActivityCode2();


    $scope.checkActivityCode3 = function(){
        $scope.typeNos3=[];
        $scope.hardTypeNos3=[];
        $http.post("activity/queryByactivityTypeNoList","010").success(function (data) {
            if(data.status){
                $scope.hardTypeNos3=data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos3.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName,transAmount:data.info[i].transAmount});
                }
            }
        })
    };
    $scope.checkActivityCode3();

    $scope.checkActivityCode4 = function(){
        $scope.typeNos4=[];
        $scope.hardTypeNos4=[];
        $http.post("activity/queryByactivityTypeNoList","011").success(function (data) {
            if(data.status){
                $scope.hardTypeNos4=data.info;
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos4.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeNo+" "+data.info[i].activityTypeName,transAmount:data.info[i].transAmount});
                }
            }
        })
    };
    $scope.checkActivityCode4();

    $scope.arcStatus=false;
    $scope.checkActivityAmount = function(activityTypeNo){
        angular.forEach($scope.typeNos2, function(item){
            if($scope.hardWare.activityCode=="009"&&item.value==activityTypeNo){
                if(item.transAmount==0){
                    $scope.arcStatus=true;
                    $scope.hardWare.defaultStatus=1;
                    $scope.hardWare.cumulateTransDay="";
                    $scope.hardWare.cumulateTransMinusDay="";
                    $scope.hardWare.cumulateAmountMinus="";
                    $scope.hardWare.cumulateAmountAdd="";
                    $scope.hardWare.repeatCumulateTransDay="";
                    $scope.hardWare.repeatCumulateTransMinusDay="";
                    $scope.hardWare.repeatCumulateAmountMinus="";
                    $scope.hardWare.repeatCumulateAmountAdd="";
                }else{
                    $scope.arcStatus=false;
                    $scope.hardWare.activityRewardConfigId="";
                }
            }
        });
    };

    $scope.checkActivityRewardConfig = function(){
        $scope.typeRewardConfig=[];
        $http.post("activity/queryByActivityRewardConfigList").success(function (data) {
            if(data.status){
                for(var i=0; i<data.info.length; i++){
                    $scope.typeRewardConfig.push({value:data.info[i].id,text:data.info[i].activityName});
                }
            }
        })
    };
    $scope.checkActivityRewardConfig();

	// 查询欢乐返活动配置
    $scope.query = function(){
        $http.post('activity/happyReturnConfig')
            .success(function(data){
                if(data.status){
                    $scope.info = data.info;
                    if(data.info != null) {
                        var stime = new Date(data.info.startTime).format("yyyy-MM-dd hh:mm:ss");
                        var etime = new Date(data.info.endTime).format("yyyy-MM-dd hh:mm:ss");
                        $scope.info.startTime = stime;
                        $scope.info.endTime = etime;
                    }
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }
    // 查询欢乐返活动重复注册配置
    $scope.query001 = function(){
        $http.post('activity/happyReturnRepeatRegistConfig')
            .success(function(data){
                if(data.status){
                    $scope.repeatRegist = data.info;
                    if(data.info != null) {
                        var stime = new Date(data.info.startTime).format("yyyy-MM-dd hh:mm:ss");
                        var etime = new Date(data.info.endTime).format("yyyy-MM-dd hh:mm:ss");
                        $scope.repeatRegist.startTime = stime;
                        $scope.repeatRegist.endTime = etime;
                    }
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }
	//获取欢乐返-循环送的硬件设备列表
    $scope.query008 = function(){
        $http.post('activity/selectHlfHardware','008')
            .success(function(data){
                if(data.status){
                    $scope.gridOptions008.data = data.page;
                    $scope.gridOptions008.totalItems = data.page.length;
                    angular.forEach($scope.gridOptions008.data,function(item){
                        item.action=1;
                    })
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }
    $scope.query009New = function(){
        $scope.query009();
        $scope.query010();
        $scope.query011();
    }

	//获取欢乐返的硬件设备列表
    $scope.query009 = function(){
        $scope.queryInfo.activityCode="009";
        var queryInfo = {"queryInfo" : $scope.queryInfo};
        $http.post('activity/selectHlfActivityHardware',angular.toJson(queryInfo))
            .success(function(data){
                if(data.status){
                    $scope.gridOptions009.data = data.page;
                    $scope.gridOptions009.totalItems = data.page.length;
                    angular.forEach($scope.gridOptions009.data,function(item){
                        item.action=1;
                    })
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }

    //获取欢乐返的硬件设备列表
    $scope.query010 = function(){
        $scope.queryInfo.activityCode="010";
        var queryInfo = {"queryInfo" : $scope.queryInfo};
        $http.post('activity/selectHlfActivityHardware',angular.toJson(queryInfo))
            .success(function(data){
                if(data.status){
                    $scope.gridOptions010.data = data.page;
                    $scope.gridOptions010.totalItems = data.page.length;
                    angular.forEach($scope.gridOptions010.data,function(item){
                        item.action=1;
                    })
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    }

    //获取欢乐返的硬件设备列表
    $scope.query011 = function(){
        $scope.queryInfo.activityCode="011";
        var queryInfo = {"queryInfo" : $scope.queryInfo};
        $http.post('activity/selectHlfActivityHardware',angular.toJson(queryInfo))
            .success(function(data){
                if(data.status){
                    $scope.gridOptions011.data = data.page;
                    $scope.gridOptions011.totalItems = data.page.length;
                    angular.forEach($scope.gridOptions011.data,function(item){
                        item.action=1;
                    })
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(){
            });
    };

    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.team.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
    });

	//提交确认
    $scope.commit = function(){
        SweetAlert.swal({
                title: "请确认是否修改？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {

                    $scope.commitOK();
                }
            });
    }
    $scope.commitOK = function(){
        if($scope.info.startTime==null||$scope.info.startTime==""
            ||$scope.info.endTime==null||$scope.info.endTime==""){
            $scope.notice("活动时间不能为空!");
            return;
        }
        if($scope.info.cumulateTransDay==null||$scope.info.cumulateTransDay==""){
            $scope.notice("累计交易时间不能为空!");
            return;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.info.cumulateAmountMinus==null||$scope.info.cumulateAmountMinus==""){
            $scope.notice("累计交易金额<不能为空");
            return;
        }else{
            if($scope.info.cumulateAmountMinus!=0 && !isNum.test($scope.info.cumulateAmountMinus)){
                $scope.notice("累计交易金额<格式不正确!");
                return;
            }
        }
        if($scope.info.cumulateAmountAdd==null||$scope.info.cumulateAmountAdd==""){
            $scope.notice("累计交易金额≥不能为空!");
            return;
        }else{
            if($scope.info.cumulateAmountAdd!=0 && !isNum.test($scope.info.cumulateAmountAdd)){
                $scope.notice("累计交易金额≥格式不正确");
                return;
            }
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.info};
        $http.post("activity/addHappyReturnConfig",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

	//取消
    $scope.submitCancel=function(){
        $scope.query();
        $scope.query001();
        $scope.query008();
        $scope.query009();
        $scope.query010();
        $scope.query011();
    }
    $scope.submitCancel();

	//修改配置
    $scope.actHardwareEdit = function(entity){
        $scope.typeNos=[];
        $scope.saveStatus=1;
        $scope.subType="1";
        if(entity.activityCode=="008"){
            $scope.typeNos=$scope.typeNos1;
        }else if(entity.activityCode=="009"){
            $scope.typeNos=$scope.typeNos2;
        }else if(entity.activityCode=="010"){
            $scope.typeNos=$scope.typeNos3;
        }else if(entity.activityCode=="011") {
            $scope.typeNos = $scope.typeNos4;
        }
        $http.post("activity/selectHlfHardwareByHardId",angular.toJson(entity)).success(function (data) {
            if(data.status){
                $scope.hardWare = data.param;
                $scope.checkActivityAmount($scope.hardWare.activityTypeNo);
                if($scope.hardWare.subType=="2"){
                    $scope.hardWare.activityCode="010";
                    $scope.typeNos=$scope.typeNos3;
                    $scope.subType="2";
                }else if($scope.hardWare.subType=="3"){
                    $scope.hardWare.activityCode="011";
                    $scope.typeNos=$scope.typeNos4;
                    $scope.subType="3";
                }
                $("#hardWardAddModel").modal("show");
                $("#hardId").attr("disabled","disabled");
                $("#activityCode").attr("disabled","disabled");
            }
        })
    };

    //添加
    $scope.hardWardAddModel = function(){
        $scope.allHardQuery = angular.copy($scope.termianlTypes);
        $scope.allHard=[];
        $scope.saveStatus=0
        $("#hardWardAddModel").modal("show");
        $("#hardId").removeAttr("disabled");
        $("#activityCode").removeAttr("disabled");
        $scope.activityTypeStr="";
        $scope.hardWare={hardId:"",emptyAmount:"",fullAmount:"",defaultStatus:1};
    }
    //返回
    $scope.cancel=function(){
        $scope.hardWare={};
        $('#hardWardAddModel').modal('hide');
    }

    //新增硬件设置
    $scope.saveHardWare = function(){
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.saveStatus==0) {//新增
            if ($scope.allHard == null || $scope.allHard.length <= 0) {
                $scope.notice("请选择硬件设备!");
                return;
            }
        }

        if($scope.hardWare.activityCode == undefined || $scope.hardWare.activityCode == null || $scope.hardWare.activityCode==""){
            $scope.notice("请选择欢乐返类型!");
            return;
        }

        if($scope.hardWare.activityTypeNo == undefined || $scope.hardWare.activityTypeNo == null || $scope.hardWare.activityTypeNo==""){
            $scope.notice("请选择欢乐返子类型!");
            return;
        }
        var hardTypeNos=[];
        hardTypeNos=hardTypeNos.concat($scope.hardTypeNos1);
        hardTypeNos=hardTypeNos.concat($scope.hardTypeNos2);
        hardTypeNos=hardTypeNos.concat($scope.hardTypeNos3);
        hardTypeNos=hardTypeNos.concat($scope.hardTypeNos4);
        var typeNo;
        for(var i=0; i<hardTypeNos.length; i++){
            if(hardTypeNos[i].activityTypeNo==$scope.hardWare.activityTypeNo){
                typeNo=hardTypeNos[i];
                break;
            }
        }

        if($scope.saveStatus==0){//新增
            var termianlTypes=$scope.allHard;
            var hasSame=false;
            for(var i=0; i<termianlTypes.length; i++){
                if(typeNo.orgId==termianlTypes[i].orgId
                    &&typeNo.teamEntryId==termianlTypes[i].teamEntryId
                ){
                    hasSame=true;
                    break;
                }else{

                }
            }
            if(!hasSame){
                $scope.notice("硬件设备所属组织与欢乐返子类型所属组织不一致");
                return;
            }

        }else if($scope.saveStatus==1){//修改
            var termianlType;
            for(var i=0; i<$scope.termianlTypes.length; i++){
                if($scope.termianlTypes[i].hpId==$scope.hardWare.hardId){
                    termianlType=$scope.termianlTypes[i];
                    break;
                }
            }
            if(typeNo.orgId==termianlType.orgId
                &&typeNo.teamEntryId==termianlType.teamEntryId
            ){

            }else{
                $scope.notice("硬件设备所属组织与欢乐返子类型所属组织不一致");
                return;
            }
        }

        if($scope.arcStatus){
            if($scope.hardWare.activityRewardConfigId == undefined || $scope.hardWare.activityRewardConfigId == null || $scope.hardWare.activityRewardConfigId==""){
                $scope.notice("请选择0元个性化活动!");
                return;
            }
        }
        if($scope.subType!="2"&&$scope.subType!="3") {
            if ($scope.hardWare.cashLastAllyAmount == null || $scope.hardWare.cashLastAllyAmount === "") {
                $scope.notice("返盟主金额不能为空!");
                return;
            } else {
                if ($scope.hardWare.cashLastAllyAmount != 0 && !isNum.test($scope.hardWare.cashLastAllyAmount)) {
                    $scope.notice("返盟主金额格式不正确!");
                    return;
                }
            }
        }
        if($scope.hardWare.defaultStatus==1){
            $scope.hardWare.cumulateTransDay="";
            $scope.hardWare.cumulateTransMinusDay="";
            $scope.hardWare.cumulateAmountMinus="";
            $scope.hardWare.cumulateAmountAdd="";
            $scope.hardWare.repeatCumulateTransDay="";
            $scope.hardWare.repeatCumulateTransMinusDay="";
            $scope.hardWare.repeatCumulateAmountMinus="";
            $scope.hardWare.repeatCumulateAmountAdd="";
        }
        if($scope.hardWare.defaultStatus == 0){
            if($scope.hardWare.activityCode=='008'){
                if($scope.hardWare.cumulateTransDay==null || $scope.hardWare.cumulateTransDay===""){
                    $scope.notice("累计交易奖励时间不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateTransDay < 1){
                    $scope.notice("累计交易奖励时间格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateTransMinusDay==null || $scope.hardWare.cumulateTransMinusDay===""){
                    $scope.notice("累计交易扣费时间不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateTransMinusDay < 1){
                    $scope.notice("累计交易扣费时间格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateAmountMinus==null || $scope.hardWare.cumulateAmountMinus===""){
                    $scope.notice("累计交易金额（扣）不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateAmountMinus!=0 && !isNum.test($scope.hardWare.cumulateAmountMinus)){
                    $scope.notice("累计交易金额（扣）格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateAmountAdd==null || $scope.hardWare.cumulateAmountAdd===""){
                    $scope.notice("累计交易金额（奖）不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateAmountAdd!=0 && !isNum.test( $scope.hardWare.cumulateAmountAdd)){
                    $scope.notice("累计交易金额（奖）格式不正确!");
                    return;
                }
            }else{
                if($scope.hardWare.cumulateTransDay==null || $scope.hardWare.cumulateTransDay===""){
                    $scope.notice("首次注册累计交易奖励时间不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateTransDay < 1){
                    $scope.notice("首次注册累计交易奖励时间格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateTransMinusDay==null || $scope.hardWare.cumulateTransMinusDay===""){
                    $scope.notice("首次注册累计交易扣费时间不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateTransMinusDay < 1){
                    $scope.notice("首次注册累计交易扣费时间格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateAmountMinus==null || $scope.hardWare.cumulateAmountMinus===""){
                    $scope.notice("首次注册累计交易金额（扣）不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateAmountMinus!=0 && !isNum.test($scope.hardWare.cumulateAmountMinus)){
                    $scope.notice("首次注册累计交易金额（扣）格式不正确!");
                    return;
                }
                if($scope.hardWare.cumulateAmountAdd==null || $scope.hardWare.cumulateAmountAdd===""){
                    $scope.notice("首次注册累计交易金额（奖）不能为空!");
                    return;
                }
                if($scope.hardWare.cumulateAmountAdd!=0 && !isNum.test( $scope.hardWare.cumulateAmountAdd)){
                    $scope.notice("首次注册累计交易金额（奖）格式不正确!");
                    return;
                }

                if($scope.hardWare.repeatCumulateTransDay==null || $scope.hardWare.repeatCumulateTransDay===""){
                    $scope.notice("重复注册累计交易奖励时间不能为空!");
                    return;
                }
                if($scope.hardWare.repeatCumulateTransDay < 1){
                    $scope.notice("重复注册累计交易奖励时间格式不正确!");
                    return;
                }
                if($scope.hardWare.repeatCumulateTransDay==null || $scope.hardWare.repeatCumulateTransDay===""){
                    $scope.notice("重复注册累计交易扣费时间不能为空!");
                    return;
                }
                if($scope.hardWare.repeatCumulateTransMinusDay < 1){
                    $scope.notice("重复注册累计交易扣费时间格式不正确!");
                    return;
                }
                if($scope.hardWare.repeatCumulateTransDay==null || $scope.hardWare.repeatCumulateTransDay===""){
                    $scope.notice("重复注册累计交易金额（扣）不能为空!");
                    return;
                }
                if($scope.hardWare.repeatCumulateAmountMinus!=0 && !isNum.test($scope.hardWare.repeatCumulateAmountMinus)){
                    $scope.notice("重复注册累计交易金额（扣）格式不正确!");
                    return;
                }
                if($scope.hardWare.repeatCumulateTransDay==null || $scope.hardWare.repeatCumulateTransDay===""){
                    $scope.notice("重复注册累计交易金额（奖）不能为空!");
                    return;
                }
                if($scope.hardWare.repeatCumulateAmountAdd!=0 && !isNum.test( $scope.hardWare.repeatCumulateAmountAdd)){
                    $scope.notice("重复注册累计交易金额（奖）格式不正确!");
                    return;
                }
            }
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"hardWare" : $scope.hardWare,"hards" : $scope.allHard};
        if($scope.saveStatus==0){
            $http.post("activity/addHlfHardWare",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#hardWardAddModel").modal("hide");
                        $scope.submitCancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
		}else if($scope.saveStatus==1){
            $http.post("activity/editHlfHardWare",angular.toJson(data))
                .success(function(data){
                    if(data.status){
                        $scope.notice(data.msg);
                        $("#hardWardAddModel").modal("hide");
                        $scope.submitCancel();
                    }else{
                        $scope.notice(data.msg);
                    }
                    $scope.submitting = false;
                })
                .error(function(){
                    $scope.submitting = false;
                });
		}

    }

    $("#activityCode").change(function(){
        $scope.showBySubType();
    });

    $scope.showBySubType=function(){
        var selVal = $("#activityCode").children('option:selected').val();
        //$scope.notice(selVal);
        if(selVal=="string:010"){
            $scope.subType="2";
        }else if(selVal=="string:011") {
            $scope.subType="3";
        }else{
            $scope.subType="1";
        }
    }

    //添加
    $scope.repeatRegistAddModel = function(){
        $scope.submitCancel();
        $("#repeatRegistAddModel").modal("show");
    }
    //返回
    $scope.cancelRepeatRegistAddModel=function(){
        $scope.submitCancel();
        $('#repeatRegistAddModel').modal('hide');
    }

    //提交
    $scope.commitRepeatRegistAddModel = function(){
        if($scope.repeatRegist.startTime==null||$scope.repeatRegist.startTime==""
            ||$scope.repeatRegist.endTime==null||$scope.repeatRegist.endTime==""){
            $scope.notice("活动时间不能为空!");
            return;
        }
        if($scope.repeatRegist.cumulateTransDay==null||$scope.repeatRegist.cumulateTransDay==""){
            $scope.notice("累计交易奖励时间不能为空!");
            return;
        }
        if($scope.repeatRegist.cumulateTransMinusDay==null||$scope.repeatRegist.cumulateTransMinusDay==""){
            $scope.notice("累计交易扣费时间不能为空!");
            return;
        }
        var isNum=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.repeatRegist.cumulateAmountMinus==null||$scope.repeatRegist.cumulateAmountMinus==""){
            $scope.notice("累计交易金额<不能为空");
            return;
        }else{
            if($scope.repeatRegist.cumulateAmountMinus!=0 && !isNum.test($scope.repeatRegist.cumulateAmountMinus)){
                $scope.notice("累计交易金额<格式不正确!");
                return;
            }
        }
        if($scope.repeatRegist.cumulateAmountAdd==null||$scope.repeatRegist.cumulateAmountAdd==""){
            $scope.notice("累计交易金额≥不能为空!");
            return;
        }else{
            if($scope.repeatRegist.cumulateAmountAdd!=0 && !isNum.test($scope.repeatRegist.cumulateAmountAdd)){
                $scope.notice("累计交易金额≥格式不正确");
                return;
            }
        }

        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.repeatRegist};
        $http.post("activity/addHappyReturnRepeatRegistConfig",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancelRepeatRegistAddModel();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

    $scope.export009 = function () {
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
                    $scope.queryInfo.activityCode="009";
                    $scope.exportInfoClick("activity/exportHlfActivityHardware",{"queryInfo" : angular.toJson($scope.queryInfo)});
                }
            });
    }

//console.log();
//转换日期格式
Date.prototype.format = function(fmt) { 
    var o = { 
       "M+" : this.getMonth()+1,			//月份 
       "d+" : this.getDate(),				//日 
       "h+" : this.getHours(),				//小时 
       "m+" : this.getMinutes(),			//分 
       "s+" : this.getSeconds(),			//秒 
       "q+" : Math.floor((this.getMonth()+3)/3),	//季度 
       "S"  : this.getMilliseconds()		//毫秒 
   };
   if(/(y+)/.test(fmt)) {
           fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));
   }
    for(var k in o) {
       if(new RegExp("("+ k +")").test(fmt)){
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));
        }
    }
   return fmt;
}

});
