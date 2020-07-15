/**
 * 超级推商品查询
 */
angular.module('inspinia').controller("queryCjtGoods", function($scope, $http, i18nService,$document,SweetAlert) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusList = [{text:"全部",value:""},{text:"已上架",value:"1"},{text:"已下架",value:"0"}];
    $scope.whiteStatusList = [{text:"全部",value:""},{text:"普通",value:"0"},{text:"白名单",value:"1"}];
    $scope.goodOrderTypeSelect = [{text:"全部",value:null},{text:"付费购买",value:1},{text:"免费申领",value:2}];

    var getCjtHpList = function(){
        $http({
            method: "get",
            url: "cjtGoods/selectCjtHpList"
        }).success(function(result){
            if(result.status){
                $scope.cjtHpList = result.data;
                $scope.cjtHpListAll = angular.copy($scope.cjtHpList);
                $scope.cjtHpListAll.unshift({typeName:"全部",hpId:null});
            }
        });
    };
    getCjtHpList();

    //清空
    $scope.resetForm = function () {
        $scope.baseInfo = {status:"", whiteStatus:"",hpId:null,goodOrderType:null,
            createTimeStart:moment(new Date().getTime()-7*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    }
    $scope.resetForm();
    //查询
    $scope.query = function(){
        $scope.submitting = true;
        $scope.loadImg = true;
        $http({
            url:"cjtGoods/selectCjtGoodsPage?pageNo=" + $scope.paginationOptions.pageNo +  "&pageSize=" + $scope.paginationOptions.pageSize,
            method:"post",
            data:$scope.baseInfo
        }).success(function(result){
            $scope.submitting = false;
            $scope.loadImg = false;
            if (!result || !result.status){
                $scope.notice (result.msg);
                return;
            }
            $scope.cjtGoodsGrid.data = result.data.result;
            $scope.cjtGoodsGrid.totalItems = result.data.totalCount;
        }).error(function(){
            $scope.submitting = false;
            $scope.loadImg = false;
            $scope.notice("服务器异常");
        });
    };
    // $scope.query();

    $scope.columnDefs = [
                    {field: 'goodsCode',width:150,displayName: '商品编号'},
                    {field: 'mainImg',width:150,displayName: '商品主图',
                        cellTemplate:
                        '<a href="{{row.entity.mainImgUrl1}}" fancybox rel="group">'
                        +'<img width="140px" height="36px" ng-src="{{row.entity.mainImgUrl1}}"/>'
                        +'</a>'},
                    {field: 'goodsName',width:150,displayName: '商品标题'},
                    {field: 'typeName',width:150,displayName: '硬件产品类型'},
                    {field: 'price',width:150,displayName: '销售价(元)'},
                    {field: 'status',width:150,displayName: '上下架状态',cellFilter:"formatDropping:" + angular.toJson($scope.statusList)},
                    {field: 'whiteStatus',width:150,displayName: '名单状态',cellFilter:"formatDropping:" + angular.toJson($scope.whiteStatusList)},
                    {field: 'goodOrderType',width:150,displayName: '申购类型',cellFilter:"formatDropping:" + angular.toJson($scope.goodOrderTypeSelect)},
                    {field: 'createTimeStr',width:150,displayName: '创建时间'},
                    {field: 'lastUpdateTimeStr',width:150,displayName: '最后更新时间'},
                    {field: 'action',displayName: '操作',width: 200,pinnedRight:true,sortable: false,editable:true,cellTemplate:
                        '<a class="lh30" target="_blank" ui-sref="cjt.detailCjtGoods({id:row.entity.goodsCode})"> 详情</a>'
                        + '<a class="lh30" ng-show="row.entity.status==0&&grid.appScope.hasPermit(\'cjtGoods.updateStatus\')" '
                        + ' ng-click="grid.appScope.updateStatus(row.entity.goodsCode, 1)"> 上架</a>'
                        + '<a class="lh30" ng-show="row.entity.status==1&&grid.appScope.hasPermit(\'cjtGoods.updateStatus\')" '
                        + ' ng-click="grid.appScope.updateStatus(row.entity.goodsCode, 0)"> 下架</a>'
                        + '<a class="lh30" target="_blank" ng-show="row.entity.status==0&&grid.appScope.hasPermit(\'cjtGoods.updateCjtGoods\')" '
                        + 'ui-sref="cjt.updateCjtGoods({id:row.entity.goodsCode})"> 修改</a>'
                        + '<a class="lh30" ng-show="row.entity.whiteStatus==0&&grid.appScope.hasPermit(\'cjtGoods.updateWhiteStatus\')" '
                        + ' ng-click="grid.appScope.updateWhiteStatus(row.entity.goodsCode, 1)"> 加入白名单</a>'
                        + '<a class="lh30" ng-show="row.entity.whiteStatus==1&&grid.appScope.hasPermit(\'cjtGoods.updateWhiteStatus\')" '
                        + ' ng-click="grid.appScope.updateWhiteStatus(row.entity.goodsCode, 0)"> 移出白名单</a>'
                    }
    ];

    $scope.cjtGoodsGrid = {
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		  //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs: $scope.columnDefs,
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
            $scope.gridApi.pagination.on.paginationChanged ($scope, function(newPage, pageSize) {
                $scope.paginationOptions.pageNo = newPage;
                $scope.paginationOptions.pageSize = pageSize;
                $scope.query();
            });
        }
    };

    //修改上架开关
    $scope.updateStatus = function(goodsCode, status){
        var text = "上架该商品后，商户即可申领该商品，是否继续操作？";
        if(status == 0){
            text = "下架该商品后，商户不能申领该商品，是否继续操作？";
        }
        SweetAlert.swal({
                title: "温馨提示",
                text: text,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"cjtGoods/updateStatus",
                        method:"post",
                        data:{goodsCode:goodsCode, status:status}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    };

    //修改白名单状态
    $scope.updateWhiteStatus = function(goodsCode, whiteStatus){
        var text = "确定要加入白名单?";
        if(whiteStatus == 0){
            text = "确定要移除白名单?";
        }
        SweetAlert.swal({
                title: "",
                text: text,
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http({
                        url:"cjtGoods/updateWhiteStatus",
                        method:"post",
                        data:{goodsCode:goodsCode, whiteStatus:whiteStatus}
                    }).success(function(result){
                        $scope.notice(result.msg);
                        if(result.status){
                            $scope.query();
                        }
                    });
                }
            });
    };

    //页面绑定回车事件
    $document.bind ("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });

});