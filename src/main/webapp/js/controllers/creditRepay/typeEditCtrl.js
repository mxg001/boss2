/**
 * 订单类型详情查看
 */
angular.module('inspinia').controller('typeEditCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$location){

	//数据源
	$scope.info={};
    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.statusSelect = [{text:"关闭",value:'0'},{text:"开启",value:'1'}];
    //$scope.channels = [];
    $scope.channelsSelect = [];
    $scope.entity = {};

    $scope.query = function () {
        $http.get('repayType/queryTypeDetailById?id='+$stateParams.id)
            .success(function(data) {
                if(data.status){
                    $scope.repayType = data.repayType;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.query();

    //修改
    $scope.update = function(){
        var status = $scope.repayType.repayPlanInfo.status;
        var closeTip = $scope.repayType.repayPlanInfo.closeTip;
        var allowBeginTime = $scope.repayType.repayPlanInfo.allowBeginTime;
        var allowEndTime = $scope.repayType.repayPlanInfo.allowEndTime;
        var allowRepayMinAmount = $scope.repayType.repayPlanInfo.allowRepayMinAmount;
        var allowRepayMaxAmount = $scope.repayType.repayPlanInfo.allowRepayMaxAmount;
        var allowFirstMinAmount = $scope.repayType.repayPlanInfo.allowFirstMinAmount;
        var allowFirstMaxAmount = $scope.repayType.repayPlanInfo.allowFirstMaxAmount;
        var allowDayMinNum = $scope.repayType.repayPlanInfo.allowDayMinNum;
        var allowDayMaxNum = $scope.repayType.repayPlanInfo.allowDayMaxNum;
        if(status==null || status==""){
            $scope.notice("请选择服务开关");
            return;
        }
        if(status==0){
            if(closeTip==null || closeTip==""){
                $scope.notice("服务关闭时必须填写提示语");
                return;
            }
        }
        if(allowBeginTime%1!=0 || allowEndTime%1!=0){
            $scope.notice("服务允许时间必须为整数");
            return;
        }
        if(!isPositiveInteger(allowRepayMinAmount) || !isPositiveInteger(allowRepayMaxAmount)){
            $scope.notice("还款目标金额必须为正整数");
            return;
        }
        if(!isPositiveInteger(allowFirstMinAmount) || !isPositiveInteger(allowFirstMaxAmount)){
            $scope.notice("首笔交易金额必须为正整数");
            return;
        }
        if(!isPositiveInteger(allowDayMinNum) || !isPositiveInteger(allowDayMaxNum)){
            $scope.notice("每日还款笔数必须为正整数");
            return;
        }
        if(parseInt(allowRepayMinAmount)>=parseInt(allowRepayMaxAmount)
            || parseInt(allowFirstMinAmount)>=parseInt(allowFirstMaxAmount) || parseInt(allowDayMinNum)>parseInt(allowDayMaxNum)
            || parseInt(allowBeginTime) >= parseInt(allowEndTime)){
            $scope.notice("参数范围填写有误,请检查参数");
            return;
        }

        SweetAlert.swal({
                title: "是否提交",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post('repayType/update', $scope.repayType).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("修改成功");
                                $location.url('creditRepay/repayTypeManager');
                            }else {
                                $scope.notice(data.msg);
                                $scope.query();
                            }
                        }
                    );
                }
            });
    }

    //新增通道的通道下拉选项
    $scope.selectChannels = function () {
        $http.get('repayType/selectAllChannels').success(
            function (data) {
                if(data.status){
                    $scope.channelsSelect = data.channels;
                }else {
                    $scope.notice("查询通道失败");
                }
        });
    }

    //返回
    $scope.back = function () {
        SweetAlert.swal({
                title: "当前操作未提交，确认返回吗？",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $location.url('creditRepay/repayTypeManager');
                }
            });
    }

     //新增通道
     $scope.addChannel = function () {
        $http.get('channel/channelInfo?id='+$scope.entity.id).success(
            function (data) {
                $scope.repayType.repayChannelList.push({id:data.channel.id,channelName:data.channel.channelName,percent:0,repayType:$scope.repayType.repayPlanInfo.planType});
                $scope.cancel();
            }
        );
    }

    //删除选项行
    $scope.deleteTableRow = function (index) {
        $scope.repayType.repayChannelList.splice(index,1);
    }

    $scope.showModal = function () {
        $scope.selectChannels();
        $("#addChannel").modal("show");
    }

    $scope.cancel = function () {
        $("#addChannel").modal("hide");
        $scope.entity={};
    }

    //选中的id集合
    $scope.selectIds = [];
    $scope.updateSelection = function ($event, id){
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);

        }
    }

    //验证是否为正整数
    isPositiveInteger = function (s) {
        var re = /^[0-9]+$/;
        if(re.test(s)){
            return parseInt(s)>0;
        }
        return false;
    }

});