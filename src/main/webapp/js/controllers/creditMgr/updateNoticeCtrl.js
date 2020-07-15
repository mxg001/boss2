/**
 * 修改公告
 */
angular.module('inspinia',['angularFileUpload']).controller("cmUpdateNoticeCtrl", function($scope, $http, $state, $stateParams,FileUploader) {

	//获取所有组织
 	$http.post("cmUserManage/selectOrgAllInfo")
 		.success(function(msg){
 			$scope.orgInfoList = msg;
 			angular.forEach($scope.orgInfoList, function(item){
 				item.checkStatus = 0;
 			});
 			$scope.getBaseInfo();
 		}).error(function(){
 			$scope.notice("获取组织信息异常");
 		});

    $scope.getBaseInfo = function(){
        $http({
            url:"cmNotice/queryNoticeById?id=" + $stateParams.id,
            method:"GET"
        }).success(function(data){
            $scope.baseInfo = data.info;
            if ($scope.baseInfo.sendTime) {
            	$scope.baseInfo.sendTimeStr = moment(new Date($scope.baseInfo.sendTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
            }
            if($scope.baseInfo.orgId != null){
            	if($scope.baseInfo.orgId == -1){
            		$scope.orgAll = 1;
				} else {
					$scope.orgAll = 0;
                    angular.forEach($scope.orgInfoList, function(item){
                        if($scope.baseInfo.orgId.indexOf(item.orgId)>-1){
                            item.checkStatus = 1;
                        }
                    });
				}
			} else {
				$scope.notice("获取信息异常");
			}
        });
	}

    //上传图片,定义控制器路径
    var uploaderImgFlag = true;
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
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    //全选组织
    $scope.changeOrgAll = function(){
        angular.forEach($scope.orgInfoList, function(item){
            item.checkStatus = $scope.orgAll;
        });
    }

    //清除图片
    $scope.removeImg = function(){
        $scope.baseInfo.iconUrl = null;
    }

    //提交
    $scope.submit = function(type){
        $scope.baseInfo.orgId = "";
        if($scope.orgAll == 1){
            $scope.baseInfo.orgId = -1;//表示全部
        } else {
            angular.forEach($scope.orgInfoList, function(item){
                if(item.checkStatus==1){
                    $scope.baseInfo.orgId = $scope.baseInfo.orgId + item.orgId + ",";
                }
            });
        }
        if(!$scope.baseInfo.orgId){
            $scope.notice("下发组织不能为空");
            return;
        }
        $scope.submitting = true;
        $scope.baseInfo.submitType = type;
        if(uploaderImg.queue.length > 0){
            uploaderImg.uploadAll();
            uploaderImg.onSuccessItem = function(fileItem, response, status, headers) {
                $scope.baseInfo.iconName = response.url;
                $scope.submitData();
            };
        }else{
        	$scope.baseInfo.iconName = "";
            $scope.submitData();
        }
    }

    $scope.submitData = function(){
        $http({
            url: "cmNotice/updateNotice",
            method: "POST",
            data: $scope.baseInfo
        }).success(function(data){
            $scope.submitting = false;
            $scope.notice(data.msg);
            if(data.status){
                $state.transitionTo('creditMgr.systemNotice',null,{reload:true});
            }
        })
    }

});
