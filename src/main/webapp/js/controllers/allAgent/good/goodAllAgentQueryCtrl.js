/**
 * 商品列表
 */
angular.module('inspinia').controller('goodAllAgentQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.statusSelect = [{text:"全部",value:""},{text:"下架",value:0},{text:"上架",value:1}];
    $scope.statusStr=angular.toJson($scope.statusSelect);

    $scope.status1Select = [{text:"下架",value:0},{text:"上架",value:1}];
    $scope.status1Str=angular.toJson($scope.status1Select);

    $scope.isMultiSelect = [{text:"否",value:0},{text:"是",value:1}];
    $scope.isMultiStr=angular.toJson($scope.isMultiSelect);

    $scope.list_type = [{value:"",text:"全部"},{value:"white",text:"白名单"},{value:"normal",text:"普通"}];
    $scope.shipWayes = [{text:"全部",value:""},{text:"机具类",value:1},{text:"物料类",value:2}];
    $scope.shipperes = [{text:"全部",value:null},{text:"不指定发货方",value:0},{text:"推荐人",value:1},{text:"机构",value:2},{text:"平台",value:3}];
    $scope.groupCodes=[];

    //清空
    $scope.clear=function(){
        $scope.info={status:"",brandCode:"",shipWay:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59'
        };
    };
    $scope.clear();

    //组织列表
    $scope.oemList=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].brandCode,text:list[i].brandName});
                    }
                }
            }
        });

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'goodsCode',displayName:'商品编号',width:180},
            { field: 'img',displayName:'商品主图',width:180,
                cellTemplate:'' +
                    '<div ng-show="row.entity.img!=null"> ' +
                    '<a href="{{row.entity.img}}" fancybox rel="group">' +
                    '<img style="width:70px;height:35px;" ng-src="{{row.entity.img}}"/>' +
                    '</a>' +
                    '</div>'
            },
            { field: 'goodsName',displayName:'商品标题',width:180},
            { field: 'price',displayName:'销售价(元)',width:180},
            /*{ field: 'cost',displayName:'成本价(元)',width:180,cellFilter:"currency:''" },
            { field: 'agio',displayName:'差价(元)',width:180,cellFilter:"currency:''" },
            { field: 'color',displayName:'颜色',width:180},
            { field: 'size',displayName:'尺码',width:180},*/
            { field: 'shipper',displayName:'发货方',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.shipperes)},
            { field: 'shipWay',displayName:'商品类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.shipWayes)},
            { field: 'groupName',displayName:'商品分类',width:180},
            { field: 'status',displayName:'状态',width:180,cellFilter:"formatDropping:" +  $scope.status1Str },
            { field: 'listType',displayName:'名单状态',width:180,cellFilter:"formatDropping:" + angular.toJson($scope.list_type) },
            { field: 'brandName',displayName:'所属品牌',width:180},
            { field: 'minimum',displayName:'起购量(台)',width:180 },
            { field: 'isMulti',displayName:'按起购量倍数下单',width:180,cellFilter:"formatDropping:" +  $scope.isMultiStr },
            { field: 'createTime',displayName:'创建时间',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'updateTime',displayName:'最后修改日期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:220,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="allAgent.goodDetail({id:row.entity.id})">详情</a> ' +
            '<a target="_blank" ng-show="grid.appScope.hasPermit(\'goodAllAgent.updateGood\')&&row.entity.status==0" ng-click="grid.appScope.updateGood(row.entity.id,1)" > | 上架</a> ' +
            '<a target="_blank" ng-show="grid.appScope.hasPermit(\'goodAllAgent.updateGood\')&&row.entity.status==1" ng-click="grid.appScope.updateGood(row.entity.id,0)" > | 下架</a> ' +
            '<a target="_blank" ng-show="grid.appScope.hasPermit(\'goodAllAgent.saveGood\')&&row.entity.status==0" ui-sref="allAgent.goodEdit({id:row.entity.id})"> | 修改</a> ' +
            '<a ng-show="row.entity.listType==\'normal\'" ng-click="grid.appScope.updateListType({id:row.entity.id,goods_code:row.entity.goodsCode,list_type:\'white\'})"> | 加入白名单</a> ' +
            // '<a ng-show="row.entity.listType==\'normal\'" ng-click="grid.appScope.updateListType({id:row.entity.id,goods_code:row.entity.goodsCode,list_type:\'black\'})"> | 加入黑名单</a> ' +
            '<a ng-show="row.entity.listType!=\'normal\'" ng-click="grid.appScope.updateListType({id:row.entity.id,goods_code:row.entity.goodsCode,list_type:\'normal\'})"> | 移除名单</a> ' +
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
        $http.post("goodAllAgent/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
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
    // 上下架
    $scope.updateGood = function (id,sta) {
        var title;
        if(sta==0){
            title="下架商品后代理将无法再从物料商城中看到该商品,是否继续操作?"
        }else{
            title="上架商品后代理即可从物料商品中申购该商品,是否继续操作？"
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("goodAllAgent/updateGood","id="+id+"&status="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.query();
                        });
                }
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
                    location.href = "userAllAgent/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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
    $scope.updateListType = function (goodsInfo) {
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;

        var url =  "goodAllAgent/updateListType";
        console.log(angular.toJson(goodsInfo));
        $http.post(url, "info=" + angular.toJson(goodsInfo),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
                $scope.query();
            })
            .error(function (data) {
                $scope.notice(data.msg);
                $scope.loadImg = false;
                $("#showModel").modal("hide");
            });
    };

    $scope.info.listType = "";
    $scope.goodsGroupList = [{value:"",text:"全部"}];
    $scope.info.groupCode = "";
    $http.post("goodAllAgent/goodsGroupQuery","info=",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function (data) {
            if (data.status) {
                var list = data.list;
                if (list != null && list.length > 0) {
                    for (var i = 0; i < list.length; i++) {
                        $scope.goodsGroupList.push({value: list[i].group_code, text: list[i].group_name+' - '+list[i].brand_name});
                    }
                }else{
                    $scope.notice("请为当前品牌创建商品分类!");
                }
            }
        });
});