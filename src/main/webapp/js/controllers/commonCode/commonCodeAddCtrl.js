/**
 * 超级nfc，激活码管理
 */
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('commonCodeAddCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	//数据源
	i18nService.setCurrentLang('zh-cn');

	$scope.addInfo = {agentNo:""};
    //动态显示代理商名称
    $scope.queryAgentInfo = function(){
        $http({
            url: 'commonCode/queryAgentName?agentNo='+$scope.addInfo.agentNo,
            method: 'POST',
        }).success(function (data) {
            if(data.result){
                $scope.addInfo.agentName = data.data.agentName;
            }else{
                $scope.addInfo.agentName = null;
            }

        });
    }


  //添加通用码
  $scope.submitCommonCode = function(){
      $scope.submitting = true;

      if($scope.addInfo.commonCodeUrl==null || $scope.addInfo.commonCodeUrl==""){
          $scope.notice("通用码样式不能为空!");
          return;
      }
      $http({
          url: 'commonCode/addCommonCode',
          method: 'POST',
          data: $scope.addInfo
      }).success(function (data) {
          if(data.result){
              $scope.addInfo = null;
              $scope.submitting = false;
              $state.transitionTo('superNfc.commonCode',null,{reload:false});
          }
          $scope.notice(data.msg);
      });
  }

    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤格式
    $scope.uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.name.slice(item.name.lastIndexOf('.') + 1) + '|';
            return '|jpg|png|'.indexOf(type) !== -1;
        }
    });
    //过滤长度，只能上传一个
    $scope.uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>2*1024*1024){
                $scope.notice("上传图片太大了!");
                return false;
            }
            return this.queue.length < 1;
        }
    });

    $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.commonCodeUrl = response.url;
        }
    };

});


