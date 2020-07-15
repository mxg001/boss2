/**
 * 排行榜奖金发放明细
 */
angular.module('inspinia',['infinity.angular-chosen']).controller('rankListQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document,$timeout){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.entryStatusSelect = [{text:"全部",value:""},{text:"未入账",value:"0"},{text:"已入账",value:"1"}];
    $scope.entryStatusStr=angular.toJson($scope.entryStatusSelect);

    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.userTypeStr=angular.toJson($scope.userTypeSelect);

    //清空
    $scope.clear=function(){
        $scope.info={oemNo:"",entryStatus:"",countMonth:"",userCode:"",oneUserCode:""};
    };
    $scope.clear();

    $scope.userList=[];
    $scope.getUserSelect=function (str) {
        $http.post('userAllAgent/getUserByStr','str=' + str+'&level=0',
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.userList=[{value:"",text:"全部"}];
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.userList.push({value:list[i].userCode,text:list[i].userCode + " " + (list[i].realName==null?"":list[i].realName)});
                    }
                }
            });
    };
    $scope.getUserSelect("");
    //动态更新盟主
    $scope.getUserList =getUserList;
    var oldValue="";
    var timeout="";
    function getUserList(value) {
        var newValue=value;
        if(newValue != oldValue){
            if (timeout){
                $timeout.cancel(timeout);
            }
            timeout = $timeout(
                function(){
                    $scope.getUserSelect(value);
                },800);
        }
    };


    //获取一级机构用户编号
    $scope.orgUserList=[];
    $scope.getOrgUserListSelect=function (str) {
        $http.post('userAllAgent/getUserByStr','str=' + str+'&level=1',
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.orgUserList=[{value:"",text:"全部"}];
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.orgUserList.push({value:list[i].userCode,text:list[i].userCode + " " + (list[i].realName==null?"":list[i].realName)});
                    }
                }
            });
    };
    $scope.getOrgUserListSelect("");
    //动态更新盟主
    $scope.getOrgUserList =getOrgUserList;
    var oldValue="";
    var timeout="";
    function getOrgUserList(value) {
        var newValue=value;
        if(newValue != oldValue){
            if (timeout){
                $timeout.cancel(timeout);
            }
            timeout = $timeout(
                function(){
                    $scope.getOrgUserListSelect(value);
                },800);
        }
    };

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            {field: 'countMonth',displayName:'排行榜月份',width:180 },
            {field: 'rank',displayName:'排名',width:180 },
            {field: 'rewardAmount',displayName:'奖金(元)',width:180,cellFilter:"currency:''" },
            {field: 'realName',displayName:'盟主姓名',width:180 },
            {field: 'nickName',displayName:'盟主昵称',width:180 },
            {field: 'mobile',displayName:'盟主手机',width:180 },
            {field: 'userCode',displayName:'盟主编号',width:180 },
            {field: 'userType',displayName:'用户类型',width:180,cellFilter:"formatDropping:" +  $scope.userTypeStr },
            {field: 'oneUserCode',displayName:'所属机构编号',width:180 },
            {field: 'oneAgentNo',displayName:'一级代理商编号',width:180 },
            {field: 'entryStatus',displayName:'入账状态',width:180,cellFilter:"formatDropping:" +  $scope.entryStatusStr },
            {field: 'entryTime',displayName:'入账日期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            {field: 'entryRemark',displayName:'入账信息',width:400}
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
    $scope.loadImg = false;
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("rankList/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.sumMap=data.sumMap;
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
                    location.href = "rankList/importDetail?info=" + encodeURI(angular.toJson($scope.info));
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