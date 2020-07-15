/**
 * 盟主分润明细
 */
angular.module('inspinia',['angularFileUpload']).controller("shareDetailQueryCtrl", function($scope, $http, $state, $stateParams,$filter,i18nService,SweetAlert,$document,FileUploader) {
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.shareTypes=[{text:"全部",value:""},{text:"固定收益",value:1},{text:"交易分润(标准)",value:2},{text:"管理津贴",value:3},
        {text:"成长津贴",value:4},{text:"王者奖金",value:5},{text:"荣耀奖金",value:6},{text:"机具分润",value:7},{text:"交易分润(VIP)",value:8}];
    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.accStatuses=[{text:"全部",value:""},{text:"未入账",value:"NOENTERACCOUNT"},{text:"已入账",value:"ENTERACCOUNTED"}]

    $scope.pageCount={shareAmountCount:0,accYesCount:0,accNoCount:0};

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

    //clear
    $scope.clear=function(){
        $scope.info={realName:"",userType:"",userCode:"",shareType:"",accStatus:"",brandCode:"",
            twoUserCode:"",oneUserCode:"",shareMonth:"",
            startTime:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            endTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            accStartTime:"",accEndTime:""};
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
            {field: 'shareAmount',displayName: '分润金额(元)',width:180},
            {field: 'shareType',displayName: '分润类别',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.shareTypes)},
            {field: 'teamTotalAmount',displayName: '当月团队总流水(元)',width:180},
            {field: 'totalAmount',displayName: '当月直营商户总流水(元)',width:180},
            {field: 'transAmount',displayName: '交易金额',width:180},
            {field: 'transNo',displayName: '交易订单号',width:180},
            {field: 'userType',displayName: '用户类别',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.userTypeSelect)},
            {field: 'realName',displayName: '用户名称',width:180},
            {field: 'userCode',displayName: '盟主编号',width:180},
            {field: 'shareLevel',displayName: '标准分润等级',width:180,
                cellTemplate:'<div ng-show="row.entity.shareLevel!=null" style="padding: 8px 5px;">' +
                'Lv.{{row.entity.shareLevel}}</div>'},
            {field: 'shareRatio',displayName: '标准分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.shareType==\'2\' && row.entity.shareRatio!=null" style="padding: 8px 5px;">' +
                '万分之{{row.entity.shareRatio}}</div>'},
            {field: 'shareRatio',displayName: 'VIP分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.shareType==\'8\' && row.entity.shareRatio!=null" style="padding: 8px 5px;">' +
                '万分之{{row.entity.shareRatio}}</div>'},
            {field: 'transShareRatio',displayName: '荣耀奖金分润比例',width:180,
                cellTemplate:'<div ng-show="row.entity.shareType==\'6\' && row.entity.shareRatio!=null" style="padding: 8px 5px;">' +
                '万分之{{row.entity.shareRatio}}</div>'},
            {field: 'brandName',displayName: '所属品牌',width:180},
            {field: 'twoUserCode',displayName: '所属大盟主编号',width:180},
            {field: 'oneUserCode',displayName: '所属机构编号',width:180},
            {field: 'shareMonth',displayName: '分润归属月份',width:180},
            {field: 'createTime',displayName: '分润创建时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
            {field: 'accStatus',displayName: '入账状态',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.accStatuses)},
            {field: 'accMessage',displayName: '入账信息',width:180},
            {field: 'accTime',displayName: '入账时间',width:180,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'}
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
        $http.post("shareDetail/queryShareDetailList","info="+angular.toJson($scope.info)+"&pageNo="+
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
                    location.href="shareDetail/exportShareDetail?param="+angular.toJson($scope.info);
                }
            });

    }

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});

