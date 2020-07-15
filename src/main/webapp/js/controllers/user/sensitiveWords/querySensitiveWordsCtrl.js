
/**
 * 查询公告
 */
angular.module('inspinia').controller("querySensitiveWordsCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document) {
    //数据源
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.statusTypes=[{text:"全部",value:'0'},{text:"启用",value:'1'},{text:"禁用",value:'2'},{text:"删除",value:'3'}];
    $scope.baseInfo = {status:'0',sensitiveNo:null,keyWord:null};
    $scope.info={keyWord:null};
    $scope.statusTypesStr = angular.toJson($scope.statusTypes);

    $scope.queryFunc = function(){
        $scope.result=[];
        $http.post("sensitiveWords/selectByParam.do","baseInfo=" + angular.toJson($scope.baseInfo)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.result=data.result;
                $scope.sensitiveListGrid.totalItems = data.totalCount;
            });
    };

    $scope.queryFunc();
    $scope.sensitiveListGrid = {
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: [
            {field: 'sensitiveNo',displayName: '编号',pinnable: false,sortable: false},
            {field: 'keyWord',displayName: '关键字',pinnable: false,sortable: false},
            {field: 'createTime',displayName: '创建时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'modifyTime',displayName: '最后修改时间',width:180,pinnable: false,sortable: false,cellFilter:'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'id',displayName: '操作',pinnedRight:true,width:100,cellTemplate:
            '<div class="lh30">' +
            '<a ng-show="grid.appScope.hasPermit(\'sensitiveWords.deleteSensitiveWords\')" ng-click="grid.appScope.deleteSensitiveWords(row.entity.id)">删除</a>'+
            '</div>'}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.queryFunc();
            });
        }
    };
    //清空
    $scope.resetForm=function(){
        $scope.baseInfo = {status:'0',sensitiveNo:null,keyWord:null};
    };

    //删除公告
    $scope.deleteSensitiveWords = function(id){
        SweetAlert.swal({
                title: "确认删除?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.get('sensitiveWords/deleteSensitiveWords/'+id)
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.queryFunc();
                        }).error(function(msg){
                        $scope.notice(msg.msg);
                    });
                }
            });
    };

    //批量删除
    $scope.deleteSensitiveWordsAll = function(id){
        var selectRows = $scope.gridApi.selection.getSelectedRows();
        if(selectRows.length<=0){
            $scope.notice("批量删除最少选中一条");
            return;
        }
        var list = new Array();
        for(var i=0;i<selectRows.length;i++){
            list[i]=selectRows[i].id;
        }
        SweetAlert.swal({
                title: "确认删除所选中的数据?",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("sensitiveWords/batchDeleteSensitiveWords","swlist=" + angular.toJson(list),
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(msg){
                            $scope.notice(msg.msg);
                            $scope.queryFunc();
                        }).error(function(msg){
                            $scope.notice(msg.msg);
                    });

                }
            });
    };

    //打开新增框
    $scope.openDialog=function(){
        $('#addDialog').modal('show');
        $scope.info.keyWord=null;
    };

    //关闭新增框
    $scope.closeDialog=function(){
        $('#addDialog').modal('hide');
    };

    //保存敏感字符
    $scope.addSensitiveWords=function(){
        if($scope.info.keyWord==null||$scope.info.keyWord==""){
            $scope.notice("关键字不能为空!");
            return;
        }
        $scope.submitting = true;
        $http.post("sensitiveWords/saveSensitiveWords","baseInfo=" + angular.toJson($scope.info),
            {headers: {'Content-Type':'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.queryFunc();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.info.keyWord=null;
                $scope.closeDialog();
                $scope.submitting = false;
            });
    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.queryFunc();
            }
        })
    });
});
