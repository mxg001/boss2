/**
 * 新增通道
 */
angular.module('inspinia').controller('addChannelCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$location){

	//数据源
	$scope.info={repayChannel:{},excludeCardList:[]};
    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.statusSelect = [{text:"开启",value:'1'},{text:"关闭",value:'2'}];
    $scope.excludeCard = {};

    //提交
    $scope.addChannel = function(){
        if(isBlank($scope.info.repayChannel.channelCode) || isBlank($scope.info.repayChannel.channelName) ||
            isBlank($scope.info.repayChannel.channelStatus) ||isBlank($scope.info.repayChannel.allowBeginTime) ||
            isBlank($scope.info.repayChannel.allowEndTime) ||isBlank($scope.info.repayChannel.allowQuickMinAmount) ||
            isBlank($scope.info.repayChannel.allowQuickMaxAmount) ||isBlank($scope.info.repayChannel.allowSplitMinute)){
            $scope.notice("必要参数不能为空");
            return;
        }
        if(hasIllegalChar($scope.info.repayChannel.channelCode) || hasIllegalChar($scope.info.repayChannel.channelName)){
            $scope.notice("参数含有非法字符");
            return;
        }
        if(!isPositiveInteger($scope.info.repayChannel.allowBeginTime) || !isPositiveInteger($scope.info.repayChannel.allowEndTime)){
            $scope.notice("还款起止时间必须为整数");
            return;
        }
        if(parseInt($scope.info.repayChannel.allowBeginTime) >= parseInt($scope.info.repayChannel.allowEndTime)){
            $scope.notice("还款结束时间必须大于开始时间");
            return;
        }
        if(!isPositiveInteger($scope.info.repayChannel.allowQuickMinAmount) || !isPositiveInteger($scope.info.repayChannel.allowQuickMaxAmount)){
            $scope.notice("单笔交易金额必须为整数");
            return;
        }
        if(parseInt($scope.info.repayChannel.allowQuickMinAmount) >= parseInt($scope.info.repayChannel.allowQuickMaxAmount)){
            $scope.notice("单笔交易金额范围填写有误");
            return;
        }
        if(!isPositiveInteger($scope.info.repayChannel.allowSplitMinute)){
            $scope.notice("计划间隔分钟数必须为整数");
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
                    $http.post('channel/addChannel', $scope.info).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("提交成功");
                                //$scope.info={};
                                $location.url('creditRepay/channel');
                            }else{
                                $scope.notice(data.msg);
                            }
                        });
                }
            });
        }


    //加入不支持列表
    $scope.addExcludeCard = function () {
        $scope.excludeCard.channelCode = $scope.info.repayChannel.channelCode;
        if(isBlank($scope.excludeCard.channelCode)){
            $scope.notice("请先填写通道编码");
            return;
        }
        if(isBlank($scope.excludeCard.bankCode) || isBlank($scope.excludeCard.bankName)){
            $scope.notice("必要参数不能为空");
            return;
        }
        if(hasIllegalChar($scope.excludeCard.bankCode) || hasIllegalChar($scope.excludeCard.bankName)){
            $scope.notice("参数含有非法字符");
            return;
        }
        $scope.info.excludeCardList.push({bankCode:$scope.excludeCard.bankCode,bankName:$scope.excludeCard.bankName,channelCode:$scope.excludeCard.channelCode});
        $scope.excludeCard={};
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
                    $location.url('creditRepay/channel');
                }
            });
    }

    //删除选项行
    $scope.deleteTableRow = function (index) {
        $scope.info.excludeCardList.splice(index,1);
    }

    //参数非空判断
    isBlank = function (param) {
        if(param=="" || param==null ){
            return true;
        }else{
            return false;
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

    //是否含有非法字符
    hasIllegalChar = function (s) {
        var pat=new RegExp("[^a-zA-Z0-9\_\u4e00-\u9fa5]","i");
        if(pat.test(s)){
            return true;
        }
        return false;
    }


});