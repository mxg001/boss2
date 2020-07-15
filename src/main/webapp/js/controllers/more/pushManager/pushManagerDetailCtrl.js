/**
 * 公告详情
 */
angular.module('inspinia').controller('msgDetailCtrl',function($scope,$http,$state,$stateParams){
  $scope.pushObjArr = [];//推送对象
  $scope.pushObjArrSelected = [];//修改的时候回显以选择的推送对象
  $scope.mobileTypeArr = [{text:"全部",value:null},{text:"ios",value:2},{text:"android",value:1}];//移动端类型 默认选中全部
  $scope.pushObjCheckAllFlag = false;
  $scope.showDateSelectedFlag = false;
  $scope.baseInfo = {"targetUser":"0","mobileType":null,"dingshiOrNow":"0"};
  $scope.showPushFlag = false;

  if($stateParams.isPush==1){//如果当前操作是推送 显示推送按钮
    $scope.showPushFlag = true;
  }else{
    $scope.showPushFlag = false;
  }

  $scope.showDateSelected = function(showDateSelectedFlag){
    $scope.showDateSelectedFlag = !showDateSelectedFlag;
  }
  $scope.toPush = function(){
    $http.post("pushManager/checkCanPush","pushId="+$stateParams.id,
      {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(data){
        if(data.status){
          $scope.pushSubmit();
        }else{
          $scope.notice(data.msg);
        }
      });


  }
  $scope.pushSubmit = function(){
    $http.post("pushManager/toPush","pushId="+$stateParams.id,
      {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(data){
        if(data.status){
          $scope.notice(data.msg);
          $scope.submitting = false;
          $state.transitionTo('sys.pushManager',null,{reload:true});
        }else{
          $scope.notice(data.msg);
        }
      });
  }

  $scope.getAppInfo = function(){
    $http.post("pushManager/getAppInfo")
      .success(function(data){
        if(data.status){
          var appInfos = data.appInfos;
          for(var i=0; i<appInfos.length; i++){
            $scope.pushObjArr.push({value:appInfos[i].sys_value,text:appInfos[i].sys_name,isChecked:$.inArray(appInfos[i].sys_value, $scope.pushObjArrSelected)!=-1});
          }
        }else{
          $scope.notice(data.msg);
        }

      });
  }

  $scope.getPushManagerById = function(){
    $http.post("pushManager/getPushManagerById","id="+$stateParams.id,
      {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
      .success(function(data){
        if(data.status){
          $scope.baseInfo=data.baseInfo;
          $scope.pushObjArrSelected = data.baseInfo.pushObj.split(",");
          $scope.baseInfo.pushTime = moment(new Date($scope.baseInfo.pushTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
          if(data.baseInfo.dingshiOrNow==1){
            $scope.showDateSelectedFlag = true;
          }
          $scope.getAppInfo();
        }else{
          $scope.notice(data.msg);
        }
      });
  }

  $scope.getPushManagerById();






  $scope.pushObjCheckAll = function(pushObjCheckAllFlag){
    if(pushObjCheckAllFlag){
      angular.forEach($scope.pushObjArr,function(data){
        data.isChecked = 1;
      })
    } else {
      angular.forEach($scope.pushObjArr,function(data){
        data.isChecked = 0;
      })
    }
  }
});