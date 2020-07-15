/**
 * 发货机具
 */
angular.module('inspinia').controller('machineBuyOrderShipMachineCtrl',function($scope,$http,i18nService,$state,$stateParams,$q){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.info={transportCompany:"",sn:"",snText:""};
    $scope.terInfo={type:"-1",snStart:"",snEnd:""};
    $scope.goodCode=$stateParams.goodCode;
    $scope.good={};
    $scope.SNResult=[];

    var rowList = [];
    var promises = [];
    $scope.hardTypeList = [];
    var hpPromise=$q.defer();
    promises.push(hpPromise.promise);
    $http.get('hardwareProduct/selectAllInfo.do')
        .success(function(result){
            if(!result)
                return;
            $scope.termianlTypes=result;
            $scope.termianlTypes.splice(0,0,{hpId:"-1",typeName:"全部"});
            angular.forEach(result,function(data){
                $scope.hardTypeList.push({text:data.typeName,value:""+data.hpId});
            });
            hpPromise.resolve();
            delete hpPromise;
        })
    $q.all(promises).then(function(){
        // 获取数据完成了
        promises = [];
        initGrid();
        initSNResultGrid();
    });

    $http.post("goodAllAgent/getGoodsCode","goodsCode="+$stateParams.goodCode,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.good=data.good;
            }
        });

    $scope.SNResultGrid = {
        data: 'SNResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.SNResultGridApi = gridApi;
        },
    };
    function initSNResultGrid(){
        $scope.SNResultGrid.columnDefs = [                           //表格数据
            {field: 'id', displayName: '序号',width: 80},
            {field: 'sn', displayName: 'SN号',width: 130},
            {field: 'type',displayName:'硬件产品种类',width:130,cellFilter:"formatDropping:"+angular.toJson($scope.hardTypeList)},
            {field: 'activityTypeName', displayName: '机具活动类型',width: 130},
            {field: 'action',displayName: '操作',width:80,cellTemplate:
                    '<a class="lh30" ng-click="grid.appScope.delteData(row.entity)">移除</a> '}]
    }

    $scope.addList = function(rowList){
        if(rowList!=null){
            for(var i in rowList){
                if(rowList[i]!=null&&rowList[i]!=""){
                    if($scope.checkData($scope.SNResult,rowList[i],null)){
                        $scope.SNResult.push({
                            id:rowList[i].id,
                            sn:rowList[i].sn,
                            type:rowList[i].type,
                            activityTypeName:rowList[i].activityTypeName
                        });
                        $scope.cancel();
                    }
                }
            }
        }
    };

    $scope.addData = function(row){
        if(row!=null&&row!=""){
            if($scope.checkData($scope.SNResult,row,null)){
                $scope.SNResult.push({
                    id:row.id,
                    sn:row.sn,
                    type:row.type,
                    activityTypeName:row.activityTypeName
                });
                $scope.cancel();
            }
        }
    };

    $scope.checkData = function(dataList,info,oldInfo){
        if(dataList!=null&&dataList.length>0){
            for(var i=0;i<dataList.length;i++){
                var item=dataList[i];
                if(oldInfo!=null){
                    if(item.sn==oldInfo.sn){
                        continue;
                    }
                }
                if(item.sn==info.sn){
                    return false;
                }
            }
        }
        return true;
    };

    $scope.delteData = function(row){
        if(row!=null&&row!=""){
            for(var j=0;j<$scope.SNResult.length;j++){
                var dateItem=$scope.SNResult[j];
                if(row.sn==dateItem.sn){
                    $scope.SNResult.splice(j, 1);
                }
            }
            for(var j=0;j<$scope.result.length;j++){
                var dateItem=$scope.result[j];
                if(row.sn==dateItem.sn){
                    $scope.gridApi.selection.unSelectRow($scope.result[j]);
                }
            }
        }
    };

    $scope.SNGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100,500],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            //全选
            $scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function (rows) {
                if(rows[0].isSelected){
                    $scope.testRow = rows[0].entity;
                    for(var i=0;i<rows.length;i++){
                        rowList[rows[i].entity.id]=rows[i].entity;
                    }
                    $scope.addList(rowList);
                }else{
                    rowList={};
                    for(var i=0;i<rows.length;i++){
                        $scope.delteData(rows[i].entity);
                    }
                }
            })
            //单选
            $scope.gridApi.selection.on.rowSelectionChanged($scope,function (row) {
                if(row.isSelected){
                    $scope.testRow = row.entity;
                    rowList[row.entity.id]=row.entity;
                    $scope.addData(row.entity);
                }else{
                    delete rowList[row.entity.id];
                    $scope.delteData(row.entity);
                }
            })
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.SNResult != null && $scope.SNResult.length>0){
                for(var i=0;i<$scope.SNResult.length;i++){
                    if(row.entity.sn==$scope.SNResult[i].sn){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };
    function initGrid(){
        $scope.SNGrid.columnDefs = [                           //表格数据
            {field: 'id', displayName: '序号',width: 80},
            {field: 'sn', displayName: 'SN号',width: 130},
            {field: 'type',displayName:'硬件产品种类',width:130,cellFilter:"formatDropping:"+angular.toJson($scope.hardTypeList)},
            {field: 'activityTypeName', displayName: '机具活动类型',width: 130}
        ];
    }

    $scope.query=function(){
        rowList = [];
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $scope.terInfo.orderNo=$stateParams.orderNo;
        $http.post("machineBuyOrder/querySNList","terInfo="+angular.toJson($scope.terInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.SNGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };

    //发货机具查询窗口
    $scope.snModel = function(){
        $("#snModel").modal("show");
    }

    $scope.clear=function(){
        $scope.terInfo={type:"-1",snStart:"",snEnd:""};
    }
    //返回
    $scope.cancelSNModel=function(){
        if($scope.info.sn==null||$scope.info.sn==""){
            $scope.result=[];
            $scope.SNResult=[];
            $scope.terInfo={type:"-1",snStart:"",snEnd:""};
        }
        $('#snModel').modal('hide');
    }
    //确认
    $scope.confirmSNModel=function(){
        $('#snModel').modal('hide');
        $scope.info.sn="";
        if($scope.SNResult!=null&&$scope.SNResult.length>0){
            for(var i in $scope.SNResult){
                $scope.info.sn+=$scope.SNResult[i].sn+",";
            }
        }
        if($scope.info.sn!=""){
            $scope.info.sn=$scope.info.sn.substr(0,$scope.info.sn.length-1)
            $scope.info.snText='已选择'+$scope.info.sn.split(",").length+'台机具';
        }else{
            $scope.info.snText="";
        }

    }
    $scope.confirmSend=function(){
        $scope.info.orderNo=$stateParams.orderNo;
        if($scope.info.transportCompany==undefined || $scope.info.transportCompany.trim().length==0){
            $scope.notice("请选择快递公司");
            return ;
        }
        if($scope.info.postNo==undefined || $scope.info.postNo.trim().length==0){
            $scope.notice("物流单号不能为空");
            return ;
        }
        if($scope.good!=null&&$scope.good.shipWay==1){
            if($scope.info.snText==undefined || $scope.info.snText.trim().length==0){
                $scope.notice("请选择需要发货的机具");
                return ;
            }
            if($scope.info.sn.split(",").length!=$stateParams.num){
                $scope.notice("当前已选择"+$scope.info.sn.split(",").length+"台，机具台数与订单订购数量不符，请确认选择的机具台数。");
                return ;
            }
        }
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("machineBuyOrder/sendMachineBuyOrderSN","info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.machineBuyOrder',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };
});