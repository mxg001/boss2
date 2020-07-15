/**
 * 产品列表
 */
angular.module('inspinia',['uiSwitch']).controller('exchangeActivateBannerQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.applyTypeSelect = [{text:"全部",value:""},{text:"公众号+App",value:"1"},{text:"公众号",value:"2"},{text:"App",value:"3"}];
    $scope.applyTypeStr=angular.toJson($scope.applyTypeSelect);

    $scope.statusSelect = [{text:"全部",value:""},{text:"开启",value:"1"},{text:"关闭",value:"0"}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.oemList=[];
    //组织列表
    $http.post("exchangeActivateOem/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].oemNo,text:list[i].oemName});
                    }
                }
            }else{
                $scope.notice(data.msg);
            }
        });

    //清空
    $scope.clear=function(){
        $scope.info={oemNo:"",applyType:"",payTimeBegin:"",bannerName:"",id:"",status:""};
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
            { field: 'id',displayName:'ID',width:180},
            { field: 'oemName',displayName:'组织',width:180 },
            { field: 'applyType',displayName:'banner位置',width:120,cellFilter:"formatDropping:" +  $scope.applyTypeStr },
            { field: 'bannerName',displayName:'banner名称',width:180 },
            { field: 'upTime',displayName:'上线时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'downTime',displayName:'下线时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'showNo',displayName:'权重',width:180 },
            {field: 'status',displayName: '状态',width: 180,
                cellTemplate:
                '<span ng-show="grid.appScope.hasPermit(\'exchangeActivateBanner.closeBanner\')"><switch class="switch switch-s" ng-model="row.entity.status" ng-true-value="1" ng-false-value="0" ng-change="grid.appScope.closeBanner(row.entity)" /></span>'
                +'<span ng-show="!grid.appScope.hasPermit(\'exchangeActivateBanner.closeBanner\')"> <span ng-show="row.entity.status==1">开启</span><span ng-show="row.entity.status==0">关闭</span></span>'

            },
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="exchangeActivate.bannerDetail({id:row.entity.id})">详情</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateBanner.updateBanner\')" target="_blank" ui-sref="exchangeActivate.bannerEdit({id:row.entity.id})"> | 修改</a> ' +
                '<a ng-show="grid.appScope.hasPermit(\'exchangeActivateBanner.deleteBanner\')&&row.entity.status==0" ng-click="grid.appScope.deleteBanner(row.entity.id)""> | 删除</a> ' +
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
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("exchangeActivateBanner/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    if($scope.result!=null&&$scope.result.length>0){
                        for(var i=0;i<$scope.result.length;i++){
                            if($scope.result[i].status=="1"){
                                $scope.result[i].status=true;
                            }else if($scope.result[i].status=="0"){
                                $scope.result[i].status=false;
                            }
                        }
                    }
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

    $scope.deleteBanner=function(id){
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
                    $http.post("exchangeActivateBanner/deleteBanner","id="+id,
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
    $scope.closeBanner=function(entity){
        var title="";
        var sta="0";
        if(entity.status==false){
            title="确认关闭?"
        }else{
            title="确认开启?"
            sta="1";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("exchangeActivateBanner/closeBanner","id="+entity.id+"&state="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                                $scope.query();
                            }else{
                                $scope.notice(data.msg);
                                if(entity.status==false){
                                    entity.status=true;
                                }else{
                                    entity.status=false;
                                }
                            }
                        })
                        .error(function(data){
                            $scope.notice(data.msg);
                            if(entity.status==false){
                                entity.status=true;
                            }else{
                                entity.status=false;
                            }
                        });
                }
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