/**
 * 广告新增
 */
angular.module('inspinia').controller('exchangeActivateNoticeAddCtrl',function($scope,$http,i18nService,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'oemNo',displayName:'组织编码',width:200},
            { field: 'oemName',displayName:'组织名称',width:200}
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    //组织列表
    $http.post("exchangeActivateOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.result=data.list;
            }
        });

    $scope.submitting = false;
    //新增banner
    $scope.addNotice = function(){
        if($scope.submitting){
            return;
        }
        if($scope.addInfo.upTime==null||$scope.addInfo.upTime==""
            ||$scope.addInfo.downTime==null||$scope.addInfo.downTime==""){
            $scope.notice("上/下线时间不能为空!");
            return;
        }
        var selectList = $scope.gridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("下发组织不能为空!");
            return;
        }
        var ids="";
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                ids = ids + selectList[i].oemNo +",";
            }
        }
        if(ids==""){
            $scope.notice("下发组织不能为空!");
            return;
        }
        $scope.submitting = true;
        $scope.addInfo.oemNoSet=ids.substring(0,ids.length-1);

        $http.post("exchangeActivateNotice/addNotice",
            "info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.notice',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };
});