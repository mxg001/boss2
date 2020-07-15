/**
 * 编辑通道
 */
angular.module('inspinia').controller('channelEditCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$location){

	//数据源
    $scope.info={repayChannel:{},excludeCardList:[]};
    $scope.paginationOptions = {pageNo : 1,pageSize : 10};
    $scope.statusSelect = [{text:"开启",value:'1'},{text:"关闭",value:'0'}];
    $scope.excludeCard = {};
    $scope.excludeCardList = [];
    $scope.channelCode = "";
    $scope.sendData={condition:'',channelCode:''};

    //回显
    $scope.findOne =function () {
        $http.get('channel/selectById?id='+$stateParams.id)
            .success(function(data) {
                if(data.status){
                    $scope.info = data.channel;
                    //$scope.excludeCardList = data.excludeCardList;
                }else{
                    $scope.notice(data.msg);
                }
            });
    }
    $scope.findOne();

    //提交
    $scope.update = function(){
        if(isBlank($scope.info.repayChannel.channelCode) || isBlank($scope.info.repayChannel.channelName) ||
            isBlank($scope.info.repayChannel.channelStatus) ||isBlank($scope.info.repayChannel.allowBeginTime) ||
            isBlank($scope.info.repayChannel.allowEndTime) ||isBlank($scope.info.repayChannel.allowQuickMinAmount) ||
            isBlank($scope.info.repayChannel.allowQuickMaxAmount) ||isBlank($scope.info.repayChannel.allowSplitMinute)){
            $scope.notice("必要参数不能为空");
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
            $scope.notice("计划间隔分钟数必须为正整数");
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
                    $http.post('channel/updateChannel', $scope.info).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("修改成功");
                                //$scope.findOne();
                                $location.url('creditRepay/channel');
                            }else{
                                $scope.notice("修改失败");
                            }
                        });
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
                    $location.url('creditRepay/channel');
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
    

    //选中的id集合
    $scope.selectIds = [];
    $scope.updateSelection = function ($event, id){
        if(isBlank(id)){
            $scope.notice("未添加的银行卡不能删除");
            return;
        }
        if($event.target.checked){
            $scope.selectIds.push(id);
        }else {
            var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);

        }
    }

    //查询不支持列表
    $scope.queryExcludeCard = function () {
        $scope.param={condition:$scope.sendData.condition,channelCode:$scope.info.repayChannel.channelCode};
        $http.post('channel/queryExcludeCard', $scope.param).success(
            function (data) {
                if(data.status){
                    $scope.info.excludeCardList = data.excludeCardList;
                    $scope.selectIds=[];
                }else {
                    $scope.notice("查询失败");
                }
            });
    }

    //从不支持列表删除
    $scope.removeCard = function () {
        if($scope.selectIds.length==0){
            $scope.notice("请选择要删除的银行卡");
            return;
        }
        if($scope.selectIds.length > 1){
            $scope.notice("一次只能删除一张银行卡");
            return;
        }
        SweetAlert.swal({
                title: "是否删除，删除后不可恢复",
                type: "warning",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true },
            function (isConfirm) {
                if (isConfirm) {
                    $http.get('channel/removeCard?ids='+$scope.selectIds).success(
                        function (data) {
                            if(data.status){
                                $scope.notice("删除成功");
                                $scope.selectIds=[];
                                $scope.findOne();
                            }else {
                                $scope.notice("删除失败");
                            }
                        });
                }
            });
         }

    //删除选项行
    $scope.deleteTableRow = function (index) {
        $scope.info.excludeCardList.splice(index,1);
    }

    //清空查询条件
    $scope.reset = function () {
        $scope.sendData.condition='';
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