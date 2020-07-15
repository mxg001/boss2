/**
 * 功能开关业务设置
 */
angular.module('inspinia').controller('settingCtrl',function($scope,$http,$state,$stateParams,SweetAlert){

    $scope.titleName = $stateParams.functionName;
    $scope.baseInfo = {};
    $scope.baseInfo.functionNumber = 59;

    $scope.addChannel={};

    $scope.acqOrgs = [];
    //收单机构
    $http.post("acqOrgAction/selectBoxAllInfo")
        .success(function(msg){
            //响应成功
            if(msg==null){
                return;
            }
            for(var i=0; i<msg.length; i++){
                $scope.acqOrgs.push({value:msg[i].acqEnname,text:msg[i].acqEnname});
            }
        });

    //查询超级推的相关配置信息
    $scope.query = function(){
        $http.get('functionManager/selectSettingList?functionNumber=' + $stateParams.functionNumber)
            .success(function(result){
                if(result.status){
                    $scope.baseInfo = result.data.baseInfo;
                    if($stateParams.functionNumber == "062"){
                        $scope.channelValue = result.data.channelValue;
                        if(null != $scope.channelValue){
                            $scope.channelGrid.data = $scope.channelValue.channels;
                        }
                        //$scope.channelGrid.data = result.data.channelValue.channels;
                    } else {
                        $scope.valueInfo = result.data.valueInfo;
                        $scope.teamGrid.data = result.data.valueInfo.teams;
                    }
                } else {
                    $scope.notice(result.msg);
                }
            });
    }
    $scope.query();

    $scope.teamGridColumn = [                        //表格数据
        {field: 'teamName', displayName: '组织'},
        {field: 'teamId', displayName: '组织ID'},
        {field: 'teamEntryName', displayName: '子组织'},
        {field: 'teamEntryId', displayName: '子组织ID'},
        { field: 'action',displayName:'操作',
            cellTemplate:
            '<a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.saveSettingList\')" ng-click="grid.appScope.teamDelete(row.entity)"> 删除</a>'
        }
    ];

    $scope.teamGrid = {                  //配置表格
        enableHorizontalScrollbar: 1,       //横向滚动条
        enableVerticalScrollbar : 1,  		//纵向滚动条
        columnDefs: $scope.teamGridColumn
    };


    $scope.channelGridColumn = [                        //表格数据
        {field: 'channel', displayName: '通道名称',width:250 },
        { field: 'action',displayName:'操作',width:200,
            cellTemplate:
                '<a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.saveSettingList\')" ng-click="grid.appScope.channelDelete(row.entity)"> 删除</a>'
        }
    ];

    $scope.channelGrid = {                  //配置表格
        enableHorizontalScrollbar: 0,       //横向滚动条
        enableVerticalScrollbar : 1,  		//纵向滚动条
        columnDefs: $scope.channelGridColumn
    };


    $scope.hasChannel = function (acqEnname) {
        $scope.existsChannel = false;
        var channels = $scope.channelGrid.data;
        if(channels.length > 0){
            angular.forEach(channels, function (item) {
                if(item.channel == acqEnname){
                    $scope.existsChannel = true;
                }
            });
        }
        if($scope.existsChannel){
            $scope.notice("通道已存在");
        }

    }

    if($stateParams.functionNumber == 59) {
        $scope.teamGrid.columnDefs.splice($scope.teamGrid.columnDefs.length-1,0,
            {field: 'settleRateAmount', displayName: '显示费率（元）'});
        $scope.teamGrid.columnDefs.splice($scope.teamGrid.columnDefs.length-1,0,
            {field: 'activityTime', displayName: '激活时间',cellFilter: 'date:"yyyy-MM-dd"'});
        $scope.teamGrid.columnDefs[$scope.teamGrid.columnDefs.length-1].cellTemplate +=
            '<a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.saveSettingList\')" ng-click="grid.appScope.teamUpdateModal(row.entity)"> 修改</a>'
    }

    if($stateParams.functionNumber == 64) {
        $scope.teamGrid.columnDefs.splice($scope.teamGrid.columnDefs.length-1,0,
            {field: 'activityNum', displayName: '激活天数'});
        $scope.teamGrid.columnDefs.splice($scope.teamGrid.columnDefs.length-1,0,
            {field: 'swipeNum', displayName: '刷卡笔数'});
        $scope.teamGrid.columnDefs[$scope.teamGrid.columnDefs.length-1].cellTemplate +=
            '<a  class="lh30" ng-show="grid.appScope.hasPermit(\'func.saveSettingList\')" ng-click="grid.appScope.teamUpdateModal(row.entity)"> 修改</a>'
    }

    //删除组织
    $scope.teamDelete = function(entity){
        SweetAlert.swal({
                title: "确认删除？",
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
                    $scope.teamGrid.data.splice($scope.teamGrid.data.indexOf(entity), 1);
                    teamSave();
                }
            }
        );
    }


    $scope.channelDelete = function(entity){
        SweetAlert.swal({
                title: "确认删除？",
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
                    $scope.channelGrid.data.splice($scope.channelGrid.data.indexOf(entity), 1);
                    teamSave();
                }
            }
        );
    }

    $scope.teamUpdateModal = function(entity){
        $scope.oldInfo = angular.copy(entity);
        $scope.addInfo = angular.copy(entity);
        $scope.hasSubTeam($scope.addInfo.teamId);
        $('#teamAddModal').modal('show');
    }

    $scope.teamType=[];
    //组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId});
        }
    });

    $scope.allSubTeams = [];
    $http.get('teamInfo/querySubTeams').success(function(result){
        $scope.subTeamMap = result.subTeamMap;
        // angular.forEach($scope.subTeamMap, function (x) {
        //     angular.forEach(x, function (y) {
        //         $scope.allSubTeams.push({text:y.teamEntryName,value:y.teamEntryId});
        //     });
        // });
        // $scope.subTeams = $scope.allSubTeams;
    });

    $scope.hasSubTeam = function(teamId){
        if(teamId == "" || teamId == null){
            $scope.subTeams = [];
        }else {
            $scope.subTeams = [];
            var temp = $scope.subTeamMap[teamId];
            if(null != temp && temp != undefined){
                angular.forEach(temp, function (e) {
                    $scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId});
                });
            }
        }
        if(!$scope.addInfo.teamEntryId) {
            if($scope.subTeams && $scope.subTeams.length > 0) {
                $scope.addInfo.teamEntryId = $scope.subTeams[0].value;
            } else {
                $scope.addInfo.teamEntryId = null;
            }
        }

    }

    $scope.teamAddModalShow = function(){
        $scope.addInfo = {};
        $scope.subTeams = [];
        if($scope.teamType && $scope.teamType.length > 0) {
            $scope.addInfo.teamId = $scope.teamType[0].value;
        }

        $('#teamAddModal').modal('show');
    }

    $scope.teamAddModalHide = function(){
        $('#teamAddModal').modal('hide');
        $scope.oldInfo = null;
    }

    //判断子组织是否存在
    function checkTeamEntryExists() {
        var existsStatus = false;
        if ($scope.teamGrid.data && $scope.teamGrid.data.length > 0) {
            angular.forEach($scope.teamGrid.data, function (item) {
                if (item.teamEntryId == $scope.addInfo.teamEntryId) {
                    existsStatus = true;
                    return;
                }
            });
        }
        if(existsStatus) {
            $scope.notice("子组织已存在");
        }
        return existsStatus;
    }

    //判断组织是否存在
    function checkTeamExists() {
        var existsStatus = false;
        if ($scope.teamGrid.data && $scope.teamGrid.data.length > 0) {
            angular.forEach($scope.teamGrid.data, function (item) {
                if (item.teamId == $scope.addInfo.teamId) {
                    existsStatus = true;
                    return;
                }
            });
        }
        if(existsStatus) {
            $scope.notice("组织已存在");
        }
        return existsStatus;
    }

    function getTeamEntryName() {
        angular.forEach($scope.subTeams, function (item) {
            if (item.value == $scope.addInfo.teamEntryId) {
                $scope.addInfo.teamEntryName = item.text;
                return;
            }
        });
    }

    function getTeamName() {
        angular.forEach($scope.teamType, function (item) {
            if (item.value == $scope.addInfo.teamId) {
                $scope.addInfo.teamName = item.text;
                return;
            }
        });
    }

    /**
     * 如果是修改数据
     * @returns {boolean}
     */
    function updateInfo() {
        //如果组织数据有变动，需要校验是否已存在，以及获取对应的组织名称
        //如果组织数据没变动，用addInfo替换oldInfo
        if ($scope.oldInfo.teamId != $scope.addInfo.teamId ||
            $scope.oldInfo.teamEntryId != $scope.addInfo.teamEntryId) {
            if ($scope.addInfo.teamEntryId) {
                //判断子组织是否已存在
                if(checkTeamEntryExists()){
                    return false;
                };
                //获取子组织名称
                getTeamEntryName();
            } else {
                $scope.addInfo.teamEntryName = null;
            }
            if (!$scope.addInfo.teamEntryId) {
                //判断组织是否已存在
                if(checkTeamExists()) {
                    return false;
                }
            }
            //获取组织名称
            getTeamName();
        }
        //将新的数据替换到旧的数组里面
        if ($scope.teamGrid.data && $scope.teamGrid.data.length > 0) {
            angular.forEach($scope.teamGrid.data, function (item) {
                if (item.teamId == $scope.oldInfo.teamId && item.teamEntryId == $scope.oldInfo.teamEntryId) {
                    $scope.teamGrid.data[$scope.teamGrid.data.indexOf(item)] = $scope.addInfo;
                    return;
                }
            });
        }
        return true;
    }

    $scope.addChannel = function () {
        if(""==$scope.addChannel.acqEnname || null == $scope.addChannel.acqEnname){
            return;
        }
        if($scope.channelGrid.data.length < 1){
            $scope.channelValue = {channels:{}};
        }
        $scope.channelGrid.data.push({channel: $scope.addChannel.acqEnname});
        $scope.channelValue.channels = $scope.channelGrid.data;
        teamSave();

    }

    /**
     * 如果是新增数据
     * @returns {boolean}
     */
    function addInfo() {
        if ($scope.addInfo.teamEntryId) {
            //判断子组织是否已存在
            if(checkTeamEntryExists()){
                return false;
            };
            //获取子组织名称
            getTeamEntryName();
        }
        if (!$scope.addInfo.teamEntryId) {
            //判断组织是否已存在
            if(checkTeamExists()) {
                return false;
            }
        }
        //获取组织名称
        getTeamName();
        $scope.teamGrid.data.push($scope.addInfo);
        return true;
    }

    //保存组织数据
    $scope.teamAdd = function(){
        //加载最新的数据回来校验,后面考虑放在后台进行校验
        // $scope.query();
        //如果是修改
        if($scope.oldInfo != null) {
            if(!updateInfo()) {
                return;
            }
        } else {
            if(!addInfo()) {
                return;
            }
        }
        teamSave();
    }


    //保存
    function teamSave() {
        $http({
            method: "post",
            url: "functionManager/saveSettingList",
            data: {
                "functionNumber": $stateParams.functionNumber,
                "valueInfo": angular.toJson($scope.valueInfo),
                "channels": angular.toJson($scope.channelValue)
            }
        }).success(function (result) {
            if (result && result.status) {
                $scope.teamAddModalHide();
                // $scope.query();
            }
            $scope.notice(result.msg);
        }).error(function () {
            $scope.notice('服务异常');
        });
    }

    //提交配置信息
    $scope.submit = function(){
        // $scope.baseInfo.valueInfo = angular.toJson($scope.valueInfo);
        $scope.submitting = true;
        $http({
            method: "post",
            url: "functionManager/updateBaseInfo",
            data: $scope.baseInfo
        }).success(function(result){
            $scope.notice(result.msg);
            $scope.submitting = false;
        }).error(function(){
            $scope.notice('服务异常');
            $scope.submitting = false;
        });
    }

});



