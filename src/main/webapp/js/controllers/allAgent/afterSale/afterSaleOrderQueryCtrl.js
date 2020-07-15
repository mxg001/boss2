/**
 * 申购售后订单
 */
angular.module('inspinia',['angularFileUpload']).controller("afterSaleOrderQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document,FileUploader) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.statuses=[{text:"全部",value:null},{text:"待机构处理",value:0},{text:"待平台处理",value:1},{text:"已处理",value:2},{text:"已取消",value:3}];
    $scope.pageCount={waitPlatformCount:0,waitAgencyCount:0,agencyMoreThreeCount:0,agencyMoreSevenCount:0};
    $scope.baseInfo={id:"",dealDesc:"",dealImg:""};
    $scope.shipWayes = [{text:"全部",value:""},{text:"机具类",value:1},{text:"物料类",value:2}];
    $scope.ascriptiones = [{text:"全部",value:""},{text:"机构",value:1},{text:"平台",value:2}];
    $scope.handleres = [{text:"全部",value:""},{text:"机构处理",value:1},{text:"平台处理",value:2}];

    //clear
    $scope.clear=function(){
        $scope.info={orderNo:"",payOrder:"",saleType:"",status:null,shipWay:"",ascription:"",handler:"",
            applyStartTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            applyEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            dealStartTime:"",dealEndTime:""};
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
            {field: 'orderNo',displayName: '售后编号',width:200},
            {field: 'payOrder',displayName: '关联订单编号',width:180},
            {field: 'shipWay',displayName: '商品类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.shipWayes)},
            {field: 'saleType',displayName: '售后类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.saleTypes)},
            {field: 'applyDesc',displayName: '售后说明',width:180,
                cellTemplate:'' +
                '<div ng-show="row.entity.applyDesc!=null" style="margin-top:7px;"> ' +
                '<a target="_blank" ng-click="grid.appScope.getTextes(\'售后说明\',row.entity.applyDesc)">{{row.entity.applyDesc}}</a> ' +
                '</div>'
            },
            {field: 'applyImgs',displayName: '图片凭证',width:240,
                cellTemplate:'<div style="text-align:center;">' +
                '<div style="overflow:hidden; display:inline-block;" ng-show="row.entity.applyImg!=null&&row.entity.applyImg!=\'\'" >' +
                '<div ng-repeat="item in row.entity.applyImg.split(\',\')" style="float: left;">' +
                '<a href="{{item}}" fancybox rel="group">' +
                '<img style="width:70px;height:35px;" ng-src="{{item}}"/>' +
                '</a>'+
                '</div></div></div>'},
            {field: 'status',displayName: '售后状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.statuses)},
            {field: 'ascription',displayName: '售后归属',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.ascriptiones)},
            {field: 'handler',displayName: '处理人',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.handleres)},
            {field: 'dealDesc1',displayName: '机构处理结果',width:180,
                cellTemplate:'<div ng-show="row.entity.handler==1&&row.entity.status==2">' +
                '<div ng-show="row.entity.dealDesc!=null" style="margin-top:7px;"> ' +
                '<a target="_blank" ng-click="grid.appScope.getTextes(\'机构处理结果\',row.entity.dealDesc)">{{row.entity.dealDesc}}</a> ' +
                '</div>' +
                '</div>'},
            {field: 'dealImg1',displayName: '机构处理图片凭证',width:240,
                cellTemplate:'<div ng-show="row.entity.handler==1&&row.entity.status==2&&row.entity.dealImg!=null&&row.entity.dealImg!=\'\'" style="text-align:center;">' +
                '<div style="overflow:hidden; display:inline-block;">' +
                '<div ng-repeat="item in row.entity.dealImg.split(\',\')" style="float: left;">' +
                '<a href="{{item}}" fancybox rel="group">' +
                '<img style="width:70px;height:35px;" ng-src="{{item}}"/>' +
                '</a>'+
                '</div></div></div>'},
            {field: 'dealDesc2',displayName: '平台处理结果',width:180,
                cellTemplate:'<div ng-show="row.entity.handler==2&&row.entity.status==2">' +
                '<div ng-show="row.entity.dealDesc!=null" style="margin-top:7px;"> ' +
                '<a target="_blank" ng-click="grid.appScope.getTextes(\'平台处理结果\',row.entity.dealDesc)">{{row.entity.dealDesc}}</a> ' +
                '</div>' +
                '</div>'},
            {field: 'dealImg2',displayName: '平台处理图片凭证',width:240,
                cellTemplate:'<div ng-show="row.entity.handler==2&&row.entity.status==2&&row.entity.dealImg!=null&&row.entity.dealImg!=\'\'" style="text-align:center;">' +
                '<div style="overflow:hidden; display:inline-block;">' +
                '<div ng-repeat="item in row.entity.dealImg.split(\',\')" style="float: left;">' +
                '<a href="{{item}}" fancybox rel="group">' +
                '<img style="width:70px;height:35px;" ng-src="{{item}}"/>' +
                '</a>'+
                '</div></div></div>'},
            {field: 'applyTime',displayName: '提交日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'dealTime',displayName: '处理日期',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'action',displayName: '操作',width:150,pinnedRight:true,editable:true,cellTemplate:
                '<a class="lh30" ng-show="grid.appScope.hasPermit(\'afterSaleOrder.processAfterSaleOrder\')&&(row.entity.status==\'0\'||row.entity.status==\'1\')" ng-click="grid.appScope.processModel(row.entity.id)">立即处理</a>'}
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
        $http.post("afterSaleOrder/queryAfterSaleOrderList","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.pageCount=data.pageCount;
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

    $scope.processModel=function (id) {
        $("#processModel").modal("show");
        $scope.baseInfo={id:"",dealDesc:"",dealImg:""};
        $scope.baseInfo.id=id;
    }

    $scope.cancelProcessModel=function () {
        $('#processModel').modal('hide');
        $scope.baseInfo={id:"",dealDesc:"",dealImg:""};
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
                    location.href="afterSaleOrder/exportAfterSaleOrder?param="+angular.toJson($scope.info);
                }
            });

    }

    $scope.getTextes = function (title,text) {
        SweetAlert.swal({
                title: title,
                text: text,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
            });

    }

    $scope.submitting = false;
    //保存
    $scope.submit=function () {
        if("uploading"===$scope.completeAllImgs){
            $scope.notice("还有图片未上传完成,请稍候再试!");
            return;
        }

        $scope.baseInfo.dealImg = "";
        $("#imageUL li").each(function () {
            var yunName = $(this).attr("yun_name");
            if(yunName==="undefined" || typeof (yunName) ==="undefined"){
            }else{
                $scope.baseInfo.dealImg += yunName+",";
            }
        });
        if($scope.baseInfo.dealImg!=null&&$scope.baseInfo.dealImg.trim().length>0){
            $scope.baseInfo.dealImg=$scope.baseInfo.dealImg.substr(0,$scope.baseInfo.dealImg.length-1);
        }
        if($scope.submitting){
            return;
        }
        if($scope.baseInfo.dealDesc==undefined || $scope.baseInfo.dealDesc.trim().length==0){
            $scope.notice("处理结果不能为空!");
            return;
        }
        $scope.submitting = true;
        var data={
            info:angular.toJson($scope.baseInfo),
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("afterSaleOrder/processAfterSaleOrder",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.query();
                    $scope.cancelProcessModel();
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    $scope.uploaderImgListMax = 3;
    //批量上传
    var uploaderImgList = $scope.uploaderImgList = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: $scope.uploaderImgListMax,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，限制最多上传个数
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 3;
        }
    });
    //过滤格式
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploaderImgList.onBeforeUploadItem = function(fileItem) {// 上传前的回调函数，在里面执行提交
        $scope.completeAllImgs = "uploading";
        console.log("图片开始上传[onBeforeUploadItem]");
        console.log("queue.length["+this.queue.length+"];uploaderImgListMax["+$scope.uploaderImgListMax+"]");
    };

    uploaderImgList.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            var itemIndex = this.getIndexOfItem(fileItem);
            $("#imageUL li").eq(itemIndex).attr("yun_name",response.url);
            console.log("图片上传完成一项[onSuccessItem]" + response.url);
        }
    };

    $scope.completeAllImgs = "start";
    uploaderImgList.onBeforeUpload = function(){
        console.log("图片开始上传[onBeforeUpload]");
        $scope.completeAllImgs = "uploading";
    };
    uploaderImgList.onCompleteAll = function(){
        console.log("所有图片上传完成[onCompleteAll]");
        $scope.completeAllImgs = "end";
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

