/**
 * 抽奖信息查询
 */
angular.module('inspinia').controller('luckDrawOrderQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:-1},{text:"未中奖",value:1},{text:"已中奖",value:2},
        {text:"已发奖",value:3}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    //清空
    $scope.clear=function(){
        $scope.info={awardName:"",status:-1,awardsConfigId:"",
            playTimeBegin:moment(new Date().getTime()-30*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            playTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    $scope.prizeList=[];
    //获取奖项配置表
    $scope.getPrizeList=function () {
        $scope.prizeList=[{value:"",text:"全部"}];
        $http.post("prizeConfigure/getPrizeList","funcCode=8",
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.prizeList.push({value:list[i].id,text:list[i].awardDesc});
                        }
                    }
                }
            });
    };
    $scope.getPrizeList();

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'id',displayName:'ID',width:80},
            { field: 'seq',displayName:'奖品码',width:180 },
            { field: 'awardDesc',displayName:'奖项说明',width:180 },
            { field: 'awardName',displayName:'奖品名称',width:180 },
            { field: 'activityName',displayName:'活动名称',width:180 },
            { field: 'status',displayName:'状态',width:120,cellFilter:"formatDropping:" +  $scope.statusStr },
            { field: 'mobilephone',displayName:'手机号',width:180 },
            { field: 'userCode',displayName:'用户ID',width:180 },
            { field: 'userName',displayName:'用户名称',width:180 },
            { field: 'playTime',displayName:'抽奖时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:180,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a target="_blank" ui-sref="activity.luckDrawOrderDetail({id:row.entity.id})">详情</a> ' +
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
        $http.post("luckDrawOrder/selectAllList","info="+angular.toJson($scope.info)+"&pageNo="+
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
                    $scope.exportInfoClick("luckDrawOrder/importDetail",{"info":angular.toJson($scope.info)});
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