/**
 * 奖项黑名单
 */
angular.module('inspinia').controller('prizeBlacklistQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.funcCode=$stateParams.funcCode;

    //清空
    $scope.clear=function(){
        $scope.info={awardConfigId:$stateParams.id,merchantNo:""};
    };
    $scope.clear();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:80},
            { field: 'merchantNo',displayName:'商户编号',width:180 },
            { field: 'merchantName',displayName:'商户名称',width:180 },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'creater',displayName:'创建人',width:180 },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-show="grid.appScope.hasPermit(\'prizeConfigure.deleteBlacklist\')" ng-click="grid.appScope.deleteBlacklist(row.entity.id)"">删除</a> ' +
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
    $scope.title="";
    $scope.getPrize=function(){
        $http.post("prizeConfigure/getPrize","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.prize=data.prize;
                    $scope.title=$scope.prize.awardDesc;
                }else{
                    $scope.notice(data.msg);
                }
            })
    };
    $scope.getPrize();

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("prizeConfigure/getBlacklist","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
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
    $scope.query();

    $scope.addModal = function(){
        $scope.addInfo={awardConfigId:$stateParams.id,merchantNo:""};
        $('#addInfoModal').modal('show');
    };
    $scope.hideModal= function(){
        $('#addInfoModal').modal('hide');
    };
    $scope.submitting = false;
    //开启
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $http.post("prizeConfigure/addBlacklist","info="+angular.toJson($scope.addInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.hideModal();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };



    // 删除
    $scope.deleteBlacklist = function (id) {
        SweetAlert.swal({
                title: "确认删除？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("prizeConfigure/deleteBlacklist","id="+id,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
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
    //获取商户信息
    $scope.merchantNoChange=function () {
      if($scope.addInfo.merchantNo!=null&&$scope.addInfo.merchantNo!=""){
          $http.post("prizeConfigure/getMerchantInfo","merchantNo="+$scope.addInfo.merchantNo,
              {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
              .success(function(data){
                  if(data.status){
                      $scope.addInfo.merchantName=data.info.merchantName;
                  }else{
                      $scope.notice(data.msg);
                  }
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