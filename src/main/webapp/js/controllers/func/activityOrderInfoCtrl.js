angular.module('inspinia').controller('activityOrderInfoCtrl',function($scope,$http,$state,$stateParams,SweetAlert,$compile,$uibModal,i18nService){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.loadImg = false;
    $scope.infoDetail={};
    $scope.remarkFlag=0 ;
    $scope.total = {};
    $scope.settlementMethods=[{text:'T1',value:'1'},{text:'T0',value:'0'}];

    $http.post("activityOrder/actOrderInfo","id="+$stateParams.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
        console.log(data)
        $scope.infoDetail=data.infoDetail;
        $scope.couponInfo=data.couponInfo;
        $scope.settleInfo=data.settleInfo;

    })
    $scope.remarkChange = function () {
        $scope.remarkFlag=1 ;
    }
    $scope.remarkCancel = function () {
        $scope.remarkFlag=0 ;
    }

    $scope.remarkUpdate = function () {
        SweetAlert.swal({
                title: "确认修改？",
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "提交",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("activityOrder/actOrderRemarkUpdate","remark="+$scope.infoDetail.remark+"&id="+$stateParams.id,{headers: {'Content-Type': 'application/x-www-form-urlencoded'}}).success(function (data) {
                        if(data>0){
                            $scope.remarkFlag=0;
                        }else {
                            location.reload();
                        }
                    }).error(function () {
                        location.reload();
                    })
                }else {
                    $scope.remarkFlag=0;
                }
            });
    }




})








