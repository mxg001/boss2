/**
 * 兑奖活动详情
 */
angular.module('inspinia',['uiSwitch']).controller('redemptionActivityManageCtrl',function(i18nService,$scope,$http,$state,$stateParams,$compile,$filter,SweetAlert,$document){
    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    //数据源
    $scope.awardTypes=[{text:"全部",value:null},{text:"鼓励金",value:1},{text:"超级积分",value:2},{text:"现金券",value:3}];
    //clear
    $scope.info={awardType:null,awardName:"",money:"",effectDays:""};
    $scope.submitting = false;

    $scope.servicesGrid = {                  //配置表格
        data: 'result',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                        //表格数据
            { field: 'id',displayName:'ID',width:60},
            { field: 'awardName',displayName:'名称',width:180},
            { field: 'money',displayName:'总价值(元)',width:180},
            { field: 'awardType',displayName:'类型',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.awardTypes)},
            { field: 'activityFirst',displayName:'优先级',width:180},
            { field: 'effectDays',displayName:'有效期(天)',width:180},
            { field: 'cancelVerificationCode',displayName:'用途',width:180,cellFilter:"formatDropping:" +  angular.toJson($scope.cancelVerificationList)},
            { field: 'action',displayName:'操作',width:150,pinnedRight:true,editable:true,cellTemplate:
                '<span><a  class="lh30" ng-click="grid.appScope.sevaButchModel(row.entity,\'view\')">详情</a><span>'+
                '<span><a  class="lh30" ng-click="grid.appScope.sevaButchModel(row.entity,\'edit\')"> | 修改</a><span>'+
                '<span><a  class="lh30" ng-click="grid.appScope.deleteRedemption(row.entity.id)"> | 删除</a><span>'
            }
        ]
    };

    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("redemption/queryRedemptionManageList","info="+angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.list;
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

    $scope.addButchModel=function () {
        $scope.isUpdate=false;
        $scope.isDetails=false;
        $scope.info={awardType:null,awardName:"",money:"",effectDays:"",activityFirst:"A"};
        $("#addButchModel").modal("show");
    }

    $scope.cancelAddButchModel=function () {
        $('#addButchModel').modal('hide');
    }

    $scope.isDetails=false;
    $scope.isUpdate=false;

    $scope.sevaButchModel=function (entity,view) {
        $scope.info=entity;
        if(view=='view'){
            $scope.isDetails=true;
            $scope.isUpdate=true;
        }
        if(view=='edit'){
            $scope.isDetails=true;
            $scope.isUpdate=false;
        }
        $("#addButchModel").modal("show");
    }

    $scope.addRedemption = function(){
        var isNum=/^[1-9]\d*$/;
        var isNum2=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.info.awardType==null || $scope.info.awardType===""){
            $scope.notice("类型不能为空!");
            return;
        }
        if($scope.info.awardName==null || $scope.info.awardName===""){
            $scope.notice("券名称不能为空!");
            return;
        }
        if($scope.info.money==null || $scope.info.money===""){
            $scope.notice("总价值不能为空!");
            return;
        }
        if(!isNum2.test($scope.info.money)){
            $scope.notice("总价值格式不正确!");
            return;
        }
        if($scope.info.awardType!=3){
            if($scope.info.effectDays==null || $scope.info.effectDays===""){
                $scope.notice("有效期不能为空!");
                return;
            }
            if(!isNum.test($scope.info.effectDays)){
                $scope.notice("有效期格式不正确!");
                return;
            }
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.info};
        $http.post("redemption/addRedemptionManage",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancelAddButchModel();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

    $scope.updateRedemption = function(){
        var isNum=/^[1-9]\d*$/;
        var isNum2=/^(([1-9][0-9]*)|(([0]\.\d{1,2}|[1-9][0-9]*\.\d{1,2})))$/;
        if($scope.info.awardType==null || $scope.info.awardType===""){
            $scope.notice("类型不能为空!");
            return;
        }
        if($scope.info.awardName==null || $scope.info.awardName===""){
            $scope.notice("券名称不能为空!");
            return;
        }
        if($scope.info.money==null || $scope.info.money===""){
            $scope.notice("总价值不能为空!");
            return;
        }
        if(!isNum2.test($scope.info.money)){
            $scope.notice("总价值格式不正确!");
            return;
        }
        if($scope.info.awardType!=3) {
            if ($scope.info.effectDays == null || $scope.info.effectDays === "") {
                $scope.notice("有效期不能为空!");
                return;
            }
            if (!isNum.test($scope.info.effectDays)) {
                $scope.notice("有效期格式不正确!");
                return;
            }
        }
        if ($scope.submitting == true) {
            return;
        }
        $scope.submitting = true;
        var data = {"info" : $scope.info};
        $http.post("redemption/updateRedemptionManage",angular.toJson(data))
            .success(function(data){
                if(data.status){
                    $scope.notice(data.msg);
                    $scope.cancelAddButchModel();
                    $scope.query();
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
            .error(function(){
                $scope.submitting = false;
            });
    }

    $scope.deleteRedemption=function(id){
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
                    $http.post("redemption/deleteRedemptionManage","id="+id,
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

    //页面绑定回车事件
    $document.bind("keypress", function(event) {
        $scope.$apply(function (){
            if(event.keyCode == 13){
                $scope.query();
            }
        })
    });
});

