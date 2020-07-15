/**
 * 领地业务管理
 */
angular.module('inspinia').controller('manorManagerCtrl',function(SweetAlert,i18nService,$scope,$http,$state,$stateParams,$compile,$filter,$log,$uibModal){
i18nService.setCurrentLang('zh-cn');
$scope.info={};
$scope.submitting = false;

$scope.resetForm = function () {
    $scope.info = {};
}
$scope.resetForm();


$http.post('manor/manormanager').success(function(data){
	  if (!data.status){
          $scope.notice(data.msg);
          return;
      }
	$scope.info = data.data.info;
}).error(function(){
}); 

//提交
$scope.commit = function(){
	$http.post("manor/savemanormanager","info="+ angular.toJson($scope.info),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                	$scope.notice(data.msg);
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting = false;
            })
}

//取消
$scope.submitCancel=function(){
	$scope.resetForm();
}
});
