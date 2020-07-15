/**
 * 盟主列表
 */
angular.module('inspinia').controller('userAllAgentQueryCtrl',function($scope,$http,i18nService,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.userTypeSelect = [{text:"全部",value:""},{text:"机构",value:1},{text:"大盟主",value:2},{text:"盟主",value:3}];
    $scope.userTypeStr=angular.toJson($scope.userTypeSelect);

    $scope.idCardNoStateSelect = [{text:"未完成认证",value:0},{text:"已完成认证",value:1}];
    $scope.idCardNoStateStr=angular.toJson($scope.idCardNoStateSelect);

    $scope.idCardNoStateCopySelect = [{text:"全部",value:""},{text:"未完成认证",value:0},{text:"已完成认证",value:1}];

    $scope.gradeSelect = [{text:"全部",value:""},{text:"普通盟主",value:"0"},{text:"黄金盟主",value:"1"},
        {text:"铂金盟主",value:"2"},{text:"黑金盟主",value:"3"},{text:"钻石盟主",value:"4"}];

    $scope.grade1Select = [{text:"普通盟主",value:"0"},{text:"黄金盟主",value:"1"},
        {text:"铂金盟主",value:"2"},{text:"黑金盟主",value:"3"},{text:"钻石盟主",value:"4"}];
    $scope.grade1Str=angular.toJson($scope.grade1Select);

    //清空
    $scope.clear=function(){
        $scope.info={userCode:"",realName:"",mobile:"",brandCode:"",userType:"",
            parentId:"",twoUserCode:"",oneUserCode:"",idCardNo:"",grade:"",nickName:"",
            createTimeBegin:moment(new Date().getTime()-6*24*60*60*1000).format('YYYY-MM-DD')+' 00:00:00',
            createTimeEnd:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',
            transStartTime:"",transEndTime:""
        };
    };
    $scope.clear();

    $scope.clearCount=function () {
        $scope.countSet={allyOneCount:0,allyTwoCount:0,allyCount:0,businessCount:0};
    };
    $scope.clearCount();

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
            { field: 'userCode',displayName:'盟主编号',width:180},
            { field: 'selectAgentNo',displayName:'代理商编号',width:180},
            { field: 'realName',displayName:'盟主姓名',width:180},
            { field: 'nickName',displayName:'昵称',width:180},
            { field: 'sumMer',displayName:'直营商户(家)',width:180},
            { field: 'sumUser',displayName:'发展直属盟主(名)',width:180},
            { field: 'sumActivationMer',displayName:'盟友商户激活数',width:180},
            { field: 'sumMerTransShare',displayName:'直属商户交易量(元)',width:180},
            { field: 'sumMerUserTransShare',displayName:'直属盟友团队交易量(元)',width:180},
            { field: 'mobile',displayName:'注册手机',width:180},
            { field: 'grade',displayName:'盟主身份',width:180,cellFilter:"formatDropping:" +  $scope.grade1Str },
            { field: 'gradeStr',displayName:'标准分润比例',width:180},
            { field: 'vipShareRatio',displayName:'VIP分润比例(万分之)',width:180},
            { field: 'shareRatio',displayName:'荣耀奖金分润比例',width:180},
            { field: 'brandName',displayName:'所属品牌',width:180},
            { field: 'parentId',displayName:'上级盟主编号',width:180 },
            { field: 'parentRealName',displayName:'上级盟主姓名',width:180 },
            { field: 'twoUserCode',displayName:'大盟主编号',width:180 },
            { field: 'oneUserCode',displayName:'机构编号',width:180 },
            { field: 'idCardNo',displayName:'身份证号',width:180 },
            { field: 'userType',displayName:'用户类型',width:180,cellFilter:"formatDropping:" +  $scope.userTypeStr },
            { field: 'idCardNoState',displayName:'是否认证',width:180,cellFilter:"formatDropping:" +  $scope.idCardNoStateStr },
            { field: 'createTime',displayName:'注册日期',cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"',width:180},
            { field: 'id',displayName:'操作',width:300,pinnedRight:true, cellTemplate:
            '<div class="lh30">'+
            '<a target="_blank" ui-sref="allAgent.allyManageDetail({id:row.entity.id})">详情</a> ' +
            '<a target="_blank" ng-show="row.entity.userType==\'3\'" ui-sref="allAgent.userAllAgentEdit({id:row.entity.id})">编辑</a> ' +
            '<a target="_blank" ui-sref="allAgent.userAllAgentDivided({userCode:row.entity.userCode})">分润比例调整记录</a> ' +
            '<a target="_blank" ng-show="row.entity.userType==\'3\'" ng-click="grid.appScope.resetPassword(row.entity.userCode)">重置密码</a> ' +
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
        $http.post("userAllAgent/selectAll","info="+angular.toJson($scope.info)+"&pageNo="+
            $scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.page.result;
                    $scope.userGrid.totalItems = data.page.totalCount;
                    $scope.countSet=data.countSet;
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
                    location.href = "userAllAgent/importDetail?info=" + encodeURI(angular.toJson($scope.info));
                }
            });
    };
    $scope.exportDividedAdjustDetailInfo = function () {
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
                    location.href = "userAllAgent/importDividedAdjustDetail?info=" + encodeURI(angular.toJson($scope.info));
                }
            });
    };

    $scope.resetPassword = function (userCode) {
        SweetAlert.swal({
                title: "点击确定后将会把该盟主的登录密码重置为当前绑定手机号码的后6位，是否继续操作？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("userAllAgent/resetPassword","userCode="+userCode,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});