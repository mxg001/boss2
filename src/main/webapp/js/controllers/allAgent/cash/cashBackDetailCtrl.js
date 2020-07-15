/**
 * 超级盟主欢乐返现
 */
angular.module('inspinia').controller('cashBackDetailCtrl',function($scope,$http,$stateParams,i18nService,SweetAlert){
    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.types = [{text:'全部',value:''},{text:'欢乐返-循环送',value:'008'},{text:'欢乐返',value:'009'}];
    $scope.typeNos=[{text:'全部',value:''}];
    $scope.activityTypeNames=[];
    $scope.entryStatuses = [{text:'全部',value:''},{text:'未入账',value:'0'},{text:'已入账',value:'1'}];
    //是否校验时间
    $scope.resetForm=function(){
        $scope.info = {activeOrder:"", entryStatus:"", userName:"", userCode:"", brandCode:"", oneUserName:"",
            oneUserCode:"",activityCode:"",activityTypeNo:"",
            activityTimeStart:moment(new Date().getTime() - 24 * 3600 * 1000).format('YYYY-MM-DD'+' 00:00:00'),
            activityTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD'+' 23:59:59')};
    }
    $scope.resetForm();

    isVerifyTime = 1;//校验：1，不校验：0

    keyChange=function(){
        if ($scope.info.activeOrder || $scope.info.userName || $scope.info.userCode
            || $scope.info.oneUserName || $scope.info.oneUserCode || $scope.info.merchantNo || $scope.info.oneAgentNo) {
            isVerifyTime = 0;
        } else {
            isVerifyTime = 1;
        }
    }

    setBeginTime=function(setTime){
        $scope.info.startTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }
    setEndTime=function(setTime){
        $scope.info.endTime = moment(setTime).format("YYYY-MM-DD HH:mm:ss");
    }

    //查询
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        /*if (!($scope.info.activeOrder || $scope.info.userName || $scope.info.userCode
            || $scope.info.oneUserName || $scope.info.oneUserCode)) {
            if(!($scope.info.startTime && $scope.info.endTime)){
                $scope.notice("入账时间不能为空");
                return;
            }
            var stime = new Date($scope.info.startTime).getTime();
            var etime = new Date($scope.info.endTime).getTime();
            if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
                $scope.notice("入账时间范围不能超过31天");
                return;
            }
        }*/
        $scope.submitting = true;
        $scope.loadImg = true;
        $http.post('cashBackAllAgent/getCashBackDetailAllAgent',"info="+angular.toJson($scope.info)+"&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+
            $scope.paginationOptions.pageSize,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(msg){
                if(msg.status){
                    $scope.cashBackGrid.data = msg.page.result;
                    $scope.cashBackGrid.totalItems = msg.page.totalCount;
                }
                $scope.loadImg = false;
                $scope.submitting = false;
            })
    }
//	$scope.query();
    $scope.cashBackGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        onRegisterApi: function(gridApi) {
            $scope.cashBackGridApi = gridApi;
            $scope.cashBackGridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    function initGrid(){
        $scope.cashBackGrid.columnDefs = [                           //表格数据
            {field: 'activeOrder',displayName: '激活订单号',pinnable: false,width: 180,sortable: false},
            {field: 'merchantNo',displayName: '商户编号',pinnable: false,width: 180,sortable: false},
            {field: 'userName',displayName: '盟主姓名',pinnable: false,width: 180,sortable: false},
            {field: 'userCode',displayName: '盟主编号',pinnable: false,width: 180,sortable: false},
            {field: 'brandCode',displayName: '所属品牌',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +  angular.toJson($scope.allAgentOem)},
            {field: 'oneAgentNo',displayName: '一级代理商编号',pinnable: false,width: 180,sortable: false},
            {field: 'oneUserName',displayName: '所属机构名称',pinnable: false,width: 180,sortable: false},
            {field: 'oneUserCode',displayName: '所属机构编号',pinnable: false,width: 180,sortable: false},
            {field: 'activityCode',displayName: '欢乐返类型',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +  angular.toJson($scope.types)},
            {field: 'activityTypeNo',displayName: '欢乐返子类型',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +  angular.toJson($scope.activityTypeNames)},
            {field: 'transAmount',displayName: '交易金额(元)',pinnable: false,width: 180,sortable: false},
            {field: 'cashBackAmount',displayName: '返盟主金额(元)',pinnable: false,width: 180,sortable: false},
            {field: 'entryStatus',displayName: '返现入账状态',pinnable: false,width: 180,sortable: false,cellFilter:"formatDropping:" +  angular.toJson($scope.entryStatuses)},
            {field: 'activityTime',displayName: '激活日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'entryTime',displayName: '入账日期',pinnable: false,width: 180,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'remark',displayName: '备注',pinnable: false,width: 180,sortable: false}
        ];
    }

    $scope.checkActivityCode = function(activityCode){
        $scope.info.activityTypeNo='';
        $scope.typeNos=[{text:'全部',value:''}];
        if(activityCode=="008"){
            $scope.typeNos=$scope.typeNos1;
        }else if(activityCode=="009"){
            $scope.typeNos=$scope.typeNos2;
        }
    };
    $scope.checkActivityCodeAll = function(){
        $scope.typeNos1=[{text:'全部',value:''}];
        $http.post("activity/queryByactivityTypeNoList","008").success(function (data) {
            if(data.status){
                for(var i=0; i<data.info.length; i++){
                    $scope.typeNos1.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                    $scope.activityTypeNames.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                }
            }
            $scope.typeNos2=[{text:'全部',value:''}];
            $http.post("activity/queryByactivityTypeNoList","009").success(function (data) {
                if(data.status){
                    for(var i=0; i<data.info.length; i++){
                        $scope.typeNos2.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                        $scope.activityTypeNames.push({value:data.info[i].activityTypeNo,text:data.info[i].activityTypeName});
                    }
                }
                initGrid();
            })
        })

    };
    $scope.checkActivityCodeAll();

    $scope.export = function () {
        /*if (!($scope.info.activeOrder || $scope.info.userName || $scope.info.userCode
            || $scope.info.oneUserName || $scope.info.oneUserCode)) {
            if(!($scope.info.startTime && $scope.info.endTime)){
                $scope.notice("入账时间不能为空");
                return;
            }
            var stime = new Date($scope.info.startTime).getTime();
            var etime = new Date($scope.info.endTime).getTime();
            if ((etime - stime) > (31 * 24 * 60 * 60 * 1000)) {
                $scope.notice("入账时间范围不能超过31天");
                return;
            }
        }*/
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
                    location.href="cashBackAllAgent/exportCashBackAllAgent?param="+angular.toJson($scope.info);
                }
            });

    }
});