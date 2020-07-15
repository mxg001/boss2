/**
 * 预警事件
 */
angular.module('inspinia',['infinity.angular-chosen']).controller("warningEventsCtrl", function($scope, $http,$state,i18nService,$document,SweetAlert) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.info={};
    $scope.submitting=false;
    $scope.total=0;
    $scope.statusSelect=[{text:"全部",value:0},{text:"交易系统",value:1},{text:"出款系统",value:2},{text:"定时任务",value:3}];
    $scope.statusSelectStr=angular.toJson($scope.statusSelect);

    $scope.clear=function () {
        $scope.info={status:0,acqId:"",createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'};
    }
    $scope.clear();
    $scope.query=function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("warningEvents/allWarningEvents","info="+ angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.total=data.page.totalCount;
                    $scope.result=data.page.result;
                    $scope.warningEventsGrid.totalItems = data.page.totalCount;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            });
    }
    $scope.query();


    $scope.import=function () {
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
                    location.href="warningEvents/exportAllInfo?info="+encodeURI(angular.toJson($scope.info));
                }
            });
    }
    $http.post('groupService/acqOrgSelectBox.do'
    ).success(function(data){
        $scope.acqOrgs = data;
        $scope.acqOrgs.splice(0,0,{"acqName":"全部","id":""});
    }).error(function(){
    });

    $scope.warningEventsGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'eventNumber',displayName: '预警编号',pinnable: false,sortable: false,width:200},
            {field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            {field: 'status',displayName: '来源系统',pinnable: false,sortable: false,width:180,cellFilter:"formatDropping:"+$scope.statusSelectStr},
            {field: 'acqEnname',displayName: '收单机构名称',pinnable: false,sortable: false,width:180},
            {field: 'message',displayName: '预警内容',pinnable: false,sortable: false,width:1000}
        ],
        onRegisterApi: function(gridApi) {
            $scope.warningPeopleGridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
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
