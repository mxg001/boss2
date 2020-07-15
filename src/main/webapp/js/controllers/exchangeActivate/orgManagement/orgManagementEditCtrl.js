/**
 * 机构编辑/详情
 */
angular.module('inspinia',['angularFileUpload']).controller('exchangeActivateOrgEditCtrl',function($scope,$http,i18nService,$stateParams,FileUploader,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.addinfo={};
    $http.post("exchangeActivateOrg/getOrgManagementDetail","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addinfo=data.org;
                $scope.listOrgDefault = data.listOrgDefault;
            }
        });

    $scope.submitting = false;
    //开启
    $scope.addBank = function(){
        if($scope.submitting){
            return;
        }
        if($scope.addinfo.finance==null||$scope.addinfo.finance==""){
            $scope.notice("是否具备金融属性不能为空");
            return;
        }
        $scope.submitting = true;
        if(uploaderImg.queue.length<1){
            $scope.addinfoSub = angular.copy($scope.addinfo);
            $scope.addinfoSub.orgLogo=null;
            $scope.saveOrg();
        }else{
            //有图片，取新图片
            uploaderImg.uploadAll();// 上传消息图片
            uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.addinfo.orgLogo= response.url;
                    $scope.addinfoSub = angular.copy($scope.addinfo);
                    $scope.saveOrg();
                }
            };
        }
    };
    $scope.saveOrg=function () {
        $http.post("exchangeActivateOrg/updateOrgManagement","info="+angular.toJson($scope.addinfoSub)+"&list="+angular.toJson($scope.listOrgDefault),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('exchangeActivate.orgManagement',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    }
    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
});