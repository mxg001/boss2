/**
 * 欢乐返子类型详情
 */
angular.module('inspinia').controller('happyReturnDetailCtrl',function($scope,$http,$stateParams){



        $http.post("activity/queryByActivityHardwareType",$stateParams.id).success(function (data) {
            if(data.status){
                $scope.happyReturnType = data.param;
            }
        })


});

