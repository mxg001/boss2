angular.module('inspinia',['uiSwitch','infinity.angular-chosen']).controller('agentVasShareRuleQueryCtrl',function($scope,$http,$state,$filter,$stateParams,$compile,$uibModal,$timeout,$log,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.initInfo={hasSubInt:1,agentLevelInt:-1,profitSwitch:"",fromType:"1",id:$stateParams.id,vasServiceNo:$stateParams.vasServiceNo,teamId:$stateParams.teamId,teamEntryId:$stateParams.teamEntryId};
    $scope.queryInfo=angular.copy($scope.initInfo);
    $scope.info = {};
    $scope.profitSwitchStr=[{text:"全部",value:""},{text:"是",value:"1"},{text:"否",value:"0"}];//
    $scope.statusSelect=[{text:"未生效",value:0},{text:"生效",value:1},{text:"已失效",value:2}];//
    $scope.statusStr=angular.toJson($scope.statusSelect);
    $scope.gridOptions = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量//配置表格
        columnDefs:[                        //表格数据
            {field: 'id', displayName: '序号',width:80 },
            { field: 'agentName',displayName:'代理商名称',width:160 },
            { field: 'agentNo',displayName:'代理商编号',width:160 },
            { field: 'agentLevel',displayName:'代理商级别',width:160 },
            {field: 'profitSwitch', displayName: '分润开关',width:90,cellTemplate:
                '<div class="lh30">'+
                '<span ><switch disabled="row.entity.agentLevel!=1" class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.profitSwitch" ng-change="grid.appScope.openSwitch(row)" /></span>' +
                '</div>'
            },
            { field: 'costRate',displayName:'代理商分润',width:130,cellTemplate:
                '<div  class="lh30" ng-switch on="$eval(\'row.entity.rateType\')">'
                +'<div ng-switch-when="1">'
                +'<div ng-show="row.entity.perFixCost!=null">{{$eval(\'row.entity.perFixCost\')}}元</div>'
                +'</div>'
                +'<div ng-switch-when="2">'
                +'<div ng-show="row.entity.costRate!=null">{{$eval(\'row.entity.costRate\')}}%</div>'
                +'</div>'
                +'</div>'},
            { field: 'agentRate',displayName:'直属分润',width:130,cellTemplate:
                '<div  class="lh30" ng-switch on="$eval(\'row.entity.rateType\')">'
                +'<div ng-switch-when="1">'
                +'<div ng-show="row.entity.agentPerFixCost!=null">{{$eval(\'row.entity.agentPerFixCost\')}}元</div>'
                +'</div>'
                +'<div ng-switch-when="2">'
                +'<div ng-show="row.entity.agentCostRate!=null">{{$eval(\'row.entity.agentCostRate\')}}%</div>'
                +'</div>'
                +'</div>'},
            { field: 'shareProfitPercent',displayName:'分润比例',width:120,cellTemplate:
                    '<div ng-show="row.entity.shareProfitPercent!=null" class="lh30">{{$eval(\'row.entity.shareProfitPercent\')}}%</div>'
            },
            { field: 'parentAgentName',displayName:'上级代理商名称',width:160 },
            { field: 'parentId',displayName:'上级代理商编号',width:160 },
            { field: 'oneLevelId',displayName:'一级代理商编号',width:160 },
            { field: 'id',displayName:'操作',pinnedRight:true,width:200,cellTemplate:
                '<div class="lh30">'+
                '<a ng-show="row.entity.agentLevel==1" ng-click="grid.appScope.editModalShow(row.entity.id)">修改</a> ' +
                '|<a ng-click="grid.appScope.recordModalShow(row.entity.id)">历史记录</a> ' +
                '</div>'
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
        $scope.queryInfo.agentLevel=$scope.queryInfo.agentLevelInt+"";
        $scope.queryInfo.hasSub=$scope.queryInfo.hasSubInt+"";
         $http.post("vasInfo/vasShareRuleQuery","info="+angular.toJson($scope.queryInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.gridOptions.data = data.page.result;
            $scope.gridOptions.totalItems = data.page.totalCount;
        })
    }
    $scope.query();


    $scope.recordGridOptions = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,                //分页数量//配置表格
        columnDefs:[                        //表格数据
            {field: 'id', displayName: '序号',width:80 },
            { field: 'vasServiceName',displayName:'增值服务名称',width:160 },
            { field: 'teamName',displayName:'组织',width:130 },
            { field: 'teamEntryName',displayName:'子组织',width:130 },
            { field: 'rate',displayName:'费率',width:130,cellTemplate:
                '<div  class="lh30" ng-switch on="$eval(\'row.entity.rateType\')">'
                +'<div ng-switch-when="1">'
                +'<div ng-show="row.entity.singleNumAmount!=null">{{$eval(\'row.entity.singleNumAmount\')}}元</div>'
                +'</div>'
                +'<div ng-switch-when="2">'
                +'<div ng-show="row.entity.rate!=null">{{$eval(\'row.entity.rate\')}}%</div>'
                +'</div>'
                +'</div>'},
            { field: 'defaultRate',displayName:'代理商分润',width:130,cellTemplate:
                '<div  class="lh30" ng-switch on="$eval(\'row.entity.rateType\')">'
                +'<div ng-switch-when="1">'
                +'<div ng-show="row.entity.perFixCost!=null">{{$eval(\'row.entity.perFixCost\')}}元</div>'
                +'</div>'
                +'<div ng-switch-when="2">'
                +'<div ng-show="row.entity.costRate!=null">{{$eval(\'row.entity.costRate\')}}%</div>'
                +'</div>'
                +'</div>'},
            { field: 'defaultShareProfitPercent',displayName:'分润比例',width:120,cellTemplate:
                    '<div ng-show="row.entity.shareProfitPercent!=null" class="lh30">{{$eval(\'row.entity.shareProfitPercent\')}}%</div>'
            },
            { field: 'createTime',displayName:'时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'effectiveStatus',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'operator',displayName:'操作人',width:180}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.recordQuery();
            });
        }
    };

    $scope.agentList= [];
    //获取代理商
    $http.post("agentInfo/selectAllInfo")
        .success(function(msg){
            for(var i=0; i<msg.length; i++){
                $scope.agentList.push({value:msg[i].agentNo,text:msg[i].agentNo + " " + msg[i].agentName});
            }

        });
    //条件查询代理商
    $scope.getStates =getStates;
    var oldValue="";
    var timeout="";
    function getStates(value) {
        $scope.agentt = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
                function(){
                    $http.post('agentInfo/selectAllInfo','item=' + value,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .then(function (response) {
                            for(var i=0; i<response.data.length; i++){
                                $scope.agentt.push({value:response.data[i].agentNo,text:response.data[i].agentNo + " " + response.data[i].agentName});
                            }
                            $scope.agentList = $scope.agentt;
                            oldValue = value;
                        });
                },800);
        }
    };

    $scope.clear = function () {
        $scope.queryInfo=angular.copy($scope.initInfo);
    }

    $scope.editModalHide = function(){
        $('#editModal').modal('hide');
    };
    $scope.recordModalHide = function(){
        $('#recordModal').modal('hide');
    };


    //历史记录
    var recordId=null;
    $scope.recordModalShow = function(id){
        $scope.paginationOptions.pageNo=1;
        recordId=id;
        $scope.recordQuery();
        $("#recordModal").modal("show");
    };

    $scope.recordQuery = function () {
        $scope.recordInfo={id:recordId};
        $http.post("vasInfo/vasShareRuleTaskQuery","info="+angular.toJson($scope.recordInfo)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
            $scope.recordGridOptions.data = data.page.result;
            $scope.recordGridOptions.totalItems = data.page.totalCount;
        })
    }



    //修改
    $scope.editModalShow = function(id){
        $http.post("vasInfo/getVasShareRule","id="+id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.info.id=id;
                    $scope.info.vasId=$stateParams.id;
                    $scope.info.remark=data.info.remark;
                    $scope.info.rateType=data.info.rateType;
                    $scope.info.vasServiceNo=data.info.vasServiceNo;
                    $scope.info.teamId=data.info.teamId;
                    $scope.info.teamEntryId=data.info.teamEntryId;
                    if(data.info.rateType=='1'){
                        $scope.info.rateInput=data.info.perFixCost;
                        $scope.info.rateTypeName="元";
                    }else if(data.info.rateType=='2'){
                        $scope.info.rateInput=data.info.costRate;
                        $scope.info.rateTypeName="%";
                    }
                    $scope.info.shareProfitPercent=data.info.shareProfitPercent;

                    $("#editModal").modal("show");
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    $scope.ruleUpdate=function () {
        SweetAlert.swal({
                title: "是否确定数据无误并保存?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $scope.ruleUpdateCfm();
                }
            });
    }

    //修改
    $scope.submittingMode=false;
    $scope.ruleUpdateCfm=function () {
        if($scope.submittingMode){
            return;
        }
        //校验
        $scope.submittingMode=true;
        $scope.subInfo=angular.copy($scope.info);

        if($scope.info.rateType=="1"){
            $scope.subInfo.perFixCost=$scope.info.rateInput;
        }else if($scope.info.rateType=="2"){
            $scope.subInfo.costRate=$scope.info.rateInput;
        }

        var data={
            info:angular.toJson($scope.subInfo),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        var commitUrl="vasInfo/updateAgentVasShareRule";

        $http.post(commitUrl,data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.query();
                    $scope.info={};
                    $scope.notice(data.msg);
                    $scope.editModalHide();
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



    $scope.openSwitch=function(row){
        if(row.entity.agentLevel!=1){
            return;
        }
        if(row.entity.profitSwitch){
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
                    if(row.entity.profitSwitch==true){
                        row.entity.profitSwitch=1;
                    } else if(row.entity.profitSwitch==false){
                        row.entity.profitSwitch=0;
                    }
                    var data={vasId:$stateParams.id,"profitSwitch":row.entity.profitSwitch,"id":row.entity.id,"agentNo":row.entity.agentNo,"vasServiceNo":row.entity.vasServiceNo,"teamId":row.entity.teamId,"teamEntryId":row.entity.teamEntryId};
                    $http.post("vasInfo/updateVasShareRuleSwitch.do",angular.toJson(data))
                        .success(function(data){
                            if(data.status){
                                $scope.notice("操作成功！");
                            }else{
                                if(row.entity.profitSwitch==true){
                                    row.entity.profitSwitch = false;
                                } else {
                                    row.entity.profitSwitch = true;
                                }
                                $scope.notice(data.msg);
                            }
                        })
                        .error(function(data){
                            if(row.entity.profitSwitch==true){
                                row.entity.profitSwitch = false;
                            } else {
                                row.entity.profitSwitch = true;
                            }
                            $scope.notice("服务器异常")
                        });
                } else {
                    if(row.entity.profitSwitch==true){
                        row.entity.profitSwitch = false;
                    } else {
                        row.entity.profitSwitch = true;
                    }
                }
            });

    };

    // 导出
    $scope.exportInfo = function () {
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
                    $scope.queryInfo.agentLevel=$scope.queryInfo.agentLevelInt+"";
                    $scope.queryInfo.hasSub=$scope.queryInfo.hasSubInt+"";
                    location.href = "vasInfo/exportVasShareRuleInfo?info="+angular.toJson($scope.queryInfo);
                }
            });
    };


})








