/**
 * SN号回拨记录
 */
angular.module('inspinia',['angularFileUpload']).controller("terminalBackQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document,FileUploader) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.statuses=[{text:"全部",value:null},{text:"等待接收",value:0},{text:"回拨成功",value:1},{text:"拒绝接收",value:2},{text:"已取消",value:3}];
    $scope.orderNo="";

    //clear
    $scope.clear=function(){
        $scope.info={orderNo:"",userCode:"",receiveUserCode:"",oneUserCode:"",receiveUserType:"",status:null,
            backStartTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            backEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            receiveStartTime:"",receiveEndTime:""};
    }
    $scope.clear();
    
    $scope.servicesGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'orderNo',displayName: '回拨单号',width:200},
            {field: 'count',displayName: '回拨数量',width:180},
            {field: 'userCode',displayName: '回拨盟主编号',width:180},
            {field: 'receiveUserCode',displayName: '接收盟主编号',width:180},
            {field: 'receiveUserType',displayName: '接收盟主类型',width:240,cellFilter:"formatDropping:" +  angular.toJson($scope.userTypeSelect)},
            {field: 'oneUserCode',displayName: '所属机构编号',width:180},
            {field: 'status',displayName: '回拨状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.statuses)},
            {field: 'createTime',displayName: '回拨日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'lastUpdateTime',displayName: '处理日期',width:180,
                cellTemplate:'<div ng-show="row.entity.status==1||row.entity.status==2||row.entity.status==3" style="padding: 8px 5px;">' +
                '{{row.entity.lastUpdateTime|date:"yyyy-MM-dd HH:mm:ss"}}</div>'},
            {field: 'action',displayName: '操作',width:150,pinnedRight:true,editable:true,cellTemplate:
                '<a class="lh30" ng-click="grid.appScope.snModel(row.entity.orderNo)">SN列表</a>'}
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
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("terminalBack/queryTerminalBackList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.servicesGrid.totalItems = data.page.totalCount;
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

    $scope.snModel=function (orderNo) {
        $("#snModel").modal("show");
        $scope.orderNo=orderNo;
        $scope.querySN();
    }

    $scope.cancelSNModel=function () {
        $('#snModel').modal('hide');
        $scope.orderNo="";
    }

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
                    location.href="terminalBack/exportTerminalBack?param="+angular.toJson($scope.info);
                }
            });
    }

    $scope.snGrid = {
        data: 'snList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10, 20,50,100],	  //切换每页记录数
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'sn',displayName: '回拨SN号',width:200}
        ]
    };
    $scope.querySN=function () {
        $http.post("terminalBack/queryTerminalBackSN","orderNo="+$scope.orderNo,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.snList=data.list;
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.notice(data.msg);
            });
    };

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});

