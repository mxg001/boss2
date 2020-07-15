angular.module('inspinia',['uiSwitch']).controller('switchSpecialSet073Ctrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.rateTypeList = [{value:"2",text:"%"},{value:"1",text:"元"}];
    $scope.orderTypes = [{sysName:"全部",sysValue:"-1"},{sysName:"普通订单",sysValue:"0"},{sysName:"云闪付",sysValue:"5"},{sysName:"境外卡交易",sysValue:"7"},{sysName:"点付VIP优享",sysValue:"10"}];
    $scope.serviceTypes = [];
    $scope.merchantTypes = [];
    $scope.addInfo = {};

    //查询收费订单、收费交易方式
    $scope.getNeedTypes = function(){
        $http.get('functionManager/get073NeedTypes?functionNumber=' + $stateParams.functionNumber)
            .success(function(data){
                if(data.status){
                    //$scope.orderTypes= data.orderTypes;
                    $scope.serviceTypes= data.serviceTypes;
                    $scope.merchantTypes= data.merchantTypes;
                    //$scope.orderTypes.unshift({sysName:"全部",sysValue:"-1"});
                    $scope.serviceTypes.unshift({sysName:"全部",sysValue:"-1"});
                } else {
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getNeedTypes();

    //查询开关设置相关信息
    $scope.getFunInfo = function(){
        $http.get('functionManager/getFunctionManagerInfo?functionNumber=' + $stateParams.functionNumber)
            .success(function(data){
                if(data.status){
                    $scope.baseInfo = data.info;
                    $scope.titleName = $scope.baseInfo.functionName;
                } else {
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.getFunInfo();

    //查询开关设置相关信息
    $scope.query = function(){
        $http.get('functionManager/getFunctionManagerTeamList?functionNumber=' + $stateParams.functionNumber)
            .success(function(data){
                if(data.status){
                    $scope.result = data.list;
                } else {
                    $scope.notice(data.msg);
                }
            });
    };
    $scope.query();

    $scope.teamGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            {field: 'teamName', displayName: '组织',width:150},
            {field: 'teamId', displayName: '组织ID',width:90},
            {field: 'teamEntryName', displayName: '子组织',width:90},
            {field: 'teamEntryId', displayName: '子组织ID',width:90},
            {field: 'showRate', displayName: '服务费',width:90},
            {field: 'merchantTypeName', displayName: '商户类型',width:150},
            {field: 'orderTypeName', displayName: '收费订单',width:90},
            {field: 'serviceTypeName', displayName: '收费交易方式',width:90},
            {field: 'status', displayName: '开关状态',width:90,cellTemplate:
                '<div class="lh30">'+
                '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.status" ng-change="grid.appScope.openSwitch(row)" /></span>' +
                '</div>'
            },
            { field: 'id',displayName:'操作',width:150,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-click="grid.appScope.teamEditModalShow(row.entity.id)">修改</a> ' +
                '<a ng-show="false" ng-click="grid.appScope.deleteTeamInfo(row.entity.id)">删除</a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };

    $scope.submitting = false;
    //保存开关基本信息
    $scope.submit = function(){
        if($scope.submitting){
            return;
        }
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
    };

    $scope.teamType=[];
    //组织
    $http.get('teamInfo/queryTeamName.do').success(function(msg){
        $scope.teamType.push({text:"请选择",value:null});
        for(var i=0;i<msg.teamInfo.length;i++){
            $scope.teamType.push({text:msg.teamInfo[i].teamName,value:msg.teamInfo[i].teamId+""});
        }
    });
    //获取所有的子组织数据
    $scope.allSubTeams = [];
    $http.get('teamInfo/querySubTeams').success(function(result){
        $scope.subTeamMap = result.subTeamMap;
    });

    //组织数据变动时间
    $scope.hasSubTeam = function(teamId){
        if(teamId == "" || teamId == null){
            $scope.subTeams = [];
        }else {
            var key=teamId;
            $scope.subTeams = [];
            var temp = $scope.subTeamMap[key];
            if(null != temp && temp != undefined){
                $scope.subTeams.push({text:"请选择",value:null});
                angular.forEach(temp, function (e) {
                    $scope.subTeams.push({text:e.teamEntryName,value:e.teamEntryId+""});
                });
            }
        }
        //$scope.addInfo.teamEntryId = null;
    };


    $scope.commitType=1;//1-新增，2-修改

    $scope.teamAddModalShow = function(){
        $scope.addInfo = {teamId:null,teamEntryId:null,rateType:"2"};
        $scope.subTeams = [];
        $scope.commitType=1;
        $scope.toCheckAll($(".checkOrderType"),false);
        $scope.toCheckAll($(".checkServiceType"),false);
        $('#teamAddModal').modal('show');
    };



    //修改
    $scope.teamEditModalShow = function(id){
        $http.post("functionManager/getTeamModelInfo","functionNumber=073&id="+id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    //$scope.addInfo = data.info;
                   //$scope.notice(data.info);
                    $("#teamAddModal").modal("show");
                    $scope.commitType=2;
                    $scope.addInfo.id=data.info.id;
                    $scope.addInfo.teamId=data.info.teamId;
                    $scope.addInfo.teamEntryId=data.info.teamEntryId;
                    $scope.addInfo.rateType=data.info.rateType;
                    if(data.info.rateType=='1'){
                        $scope.addInfo.rateInput=data.info.singleNumAmount;
                    }else if(data.info.rateType=='2'){
                        $scope.addInfo.rateInput=data.info.rate;
                    }
                    $scope.hasSubTeam($scope.addInfo.teamId);
                    $scope.orderCheck(data.info.orderType);
                    $scope.serviceCheck(data.info.serviceType);
                    $scope.merchantCheck(data.info.merchantType);
                    //$scope.notice($scope.addInfo.teamId);
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };
    $scope.orderCheck = function(orderTypes){
        var checkTypes = $(".checkOrderType");
        if(orderTypes=="-1"){
            $scope.toCheckAll(checkTypes,true);
        }else{
            var typesAry=orderTypes.split(",");
            for(var i=0;i<checkTypes.length;i++){
                var checkName=checkTypes[i].name;
                var isCheck=false;
                for(var k=0;k<typesAry.length;k++){
                    if(typesAry[k]==checkName){
                        isCheck=true;
                        break;
                    }
                }
                checkTypes[i].checked=isCheck;
            }
        }
    }

    $scope.serviceCheck = function(serviceTypes){
        var checkTypes = $(".checkServiceType");
        if(serviceTypes=="-1"){
            $scope.toCheckAll(checkTypes,true);
        }else{
            var typesAry=serviceTypes.split(",");
            for(var i=0;i<checkTypes.length;i++){
                var checkName=checkTypes[i].name;
                var isCheck=false;
                for(var k=0;k<typesAry.length;k++){
                    if(typesAry[k]==checkName){
                        isCheck=true;
                        break;
                    }
                }
                checkTypes[i].checked=isCheck;
            }
        }
    }
    $scope.merchantCheck = function(merchantTypes){
        var checkTypes = $(".checkMerchantType");
        if(merchantTypes=="-1"){
            $scope.toCheckAll(checkTypes,true);
        }else{
            var typesAry=merchantTypes.split(",");
            for(var i=0;i<checkTypes.length;i++){
                var checkName=checkTypes[i].name;
                var isCheck=false;
                for(var k=0;k<typesAry.length;k++){
                    if(typesAry[k]==checkName){
                        isCheck=true;
                        break;
                    }
                }
                checkTypes[i].checked=isCheck;
            }
        }
    }


    $scope.teamAddModalHide = function(){
        $('#teamAddModal').modal('hide');
    };

    $scope.checkOrderTypes = function(item){
        var checkName=item.sysValue;
        var checkTypes = $(".checkOrderType");
        if(checkName=="-1"){
            $scope.toCheckAll(checkTypes,item.checkValue);
        }else{
            if(item.checkValue){
                var allCheck=true;
                for(var i=1;i<checkTypes.length;i++){
                    if(!checkTypes[i].checked){
                        allCheck=false;
                        break;
                    }
                }
                if(allCheck){
                    checkTypes[0].checked=true;
                }
            }else{
                checkTypes[0].checked=false;
            }
        }
    };
    $scope.checkServiceTypes = function(item){
        var checkName=item.sysValue;
        var checkTypes = $(".checkServiceType");
        if(checkName=="-1"){
            $scope.toCheckAll(checkTypes,item.checkValue);
        }else{
            if(item.checkValue){
                var allCheck=true;
                for(var i=1;i<checkTypes.length;i++){
                    if(!checkTypes[i].checked){
                        allCheck=false;
                        break;
                    }
                }
                if(allCheck){
                    checkTypes[0].checked=true;
                }
            }else{
                checkTypes[0].checked=false;
            }
        }
    };

    $scope.toCheckAll=function (checkTypes,checkAllStatus) {
        for(var i=0;i<checkTypes.length;i++){
            checkTypes[i].checked=checkAllStatus;
        }
    }

    //新增
    $scope.submittingMode=false;
    $scope.teamAdd=function () {
        if($scope.submittingMode){
            return;
        }
        //校验
        $scope.submittingMode=true;
        $scope.subInfo=angular.copy($scope.addInfo);
        $scope.subInfo.functionNumber=$stateParams.functionNumber;

        if($scope.subInfo.rateType=="1"){
            $scope.subInfo.singleNumAmount=$scope.subInfo.rateInput;
        }else if($scope.subInfo.rateType=="2"){
            $scope.subInfo.rate=$scope.subInfo.rateInput;
        }

        var merchantTypes = $(".checkMerchantType");
        var merchantType = "";
        for(var i=0;i<merchantTypes.length;i++){
            if(merchantTypes[i].checked){
                if(merchantType!=""){
                    merchantType = merchantType + "," +merchantTypes[i].name;
                }else{
                    merchantType = merchantTypes[i].name;
                }
            }
        }
        if(merchantType==""){
            $scope.submittingMode=false;
            $scope.notice("商户类型不能为空");
            return;
        }
        $scope.subInfo.merchantType=merchantType;

        var orderTypes = $(".checkOrderType");
        var orderType = "";
        //if(orderTypes[0].checked){
         //   orderType="-1";
        //}else{
            for(var i=0;i<orderTypes.length;i++){
                if(orderTypes[i].checked){
                    if(orderType!=""){
                        orderType = orderType + "," +orderTypes[i].name;
                    }else{
                        orderType = orderTypes[i].name;
                    }
                }
            }
        //}
        if(orderType==""){
            $scope.submittingMode=false;
            $scope.notice("收费订单不能为空");
            return;
        }

        $scope.subInfo.orderType=orderType;

        var serviceTypes = $(".checkServiceType");
        var serviceType = "";
        //if(serviceTypes[0].checked){
        //    serviceType="-1";
        //}else{
            for(var i=0;i<serviceTypes.length;i++){
                if(serviceTypes[i].checked){
                    if(serviceType!=""){
                        serviceType = serviceType + "," +serviceTypes[i].name;
                    }else{
                        serviceType = serviceTypes[i].name;
                    }
                }
            }
       // }

        if(serviceType==""){
            $scope.submittingMode=false;
            $scope.notice("收费交易方式不能为空");
            return;
        }

        $scope.subInfo.serviceType=serviceType;


        var data={
            info:angular.toJson($scope.subInfo),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        var commitUrl=null;
        if($scope.commitType==1){
            commitUrl="functionManager/saveFunctionTeam";
        }else{
            commitUrl="functionManager/updateFunctionTeam";
        }

        $http.post(commitUrl,data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.query();
                    $scope.notice(data.msg);
                    $scope.teamAddModalHide();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submittingMode=false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submittingMode=false;
            });
    };
    //删除
    $scope.deleteTeamInfo=function(id){
        SweetAlert.swal({
                title: "确认删除?",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("functionManager/deleteSpecialFunctionTeam","functionNumber=073&id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.query();
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                        });

                }
            });
    };

    $scope.openSwitch=function(row){
        if(row.entity.status){
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
                    if(row.entity.status==true){
                        row.entity.status=1;
                    } else if(row.entity.status==false){
                        row.entity.status=0;
                    }
                    var data={"status":row.entity.status,"id":row.entity.id};
                    $http.post("functionManager/updateVasRateStatus.do",angular.toJson(data))
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
                            }else{
                                if(row.entity.status==true){
                                    row.entity.status = false;
                                } else {
                                    row.entity.status = true;
                                }
                                $scope.notice("操作失败！");
                            }
                        })
                        .error(function(data){
                            if(row.entity.status==true){
                                row.entity.status = false;
                            } else {
                                row.entity.status = true;
                            }
                            $scope.notice("服务器异常")
                        });
                } else {
                    if(row.entity.status==true){
                        row.entity.status = false;
                    } else {
                        row.entity.status = true;
                    }
                }
            });

    };


});