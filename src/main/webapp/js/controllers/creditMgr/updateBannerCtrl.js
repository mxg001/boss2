/**
 * 信用卡管家-修改banner
 */
angular.module('inspinia',['angularFileUpload','infinity.angular-chosen']).controller('updateBannerCtrl',function($scope, $http, $state,$stateParams,FileUploader){

    $scope.isDisabled = false;

    $scope.positionTypeSelect = [{text:"不限位置",value:0},{text:"H5",value:1}];

    $scope.ad = {};

    //获取所有组织
	$scope.org=[{value:"-1",text:"所有组织"}];
	$http.post("cmUserManage/selectOrgAllInfo")
		.success(function(msg){
			//响应成功
			for(var i=0; i<msg.length; i++){
				$scope.org.push({value:msg[i].orgId, text:msg[i].orgId + " " + msg[i].orgName});
			}
			query();
		});

	query = function () {
		$http({
	        url:"cmBanner/queryBannerById?id="+$stateParams.id,
	        method:"GET"
	    }).success(function(data){
	        if (data.status){
	            $scope.ad = data.info;
	            $scope.ad.beginTime = moment(new Date($scope.ad.beginTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
	            $scope.ad.endTime = moment(new Date($scope.ad.endTime).getTime()).format('YYYY-MM-DD HH:mm:ss');
	            $scope.ad.showNo = $scope.ad.showNo + "";
	        } else {
	        	$scope.notice(data.msg);
	        }
	    }).error(function () {
	        $scope.notice("服务器异常，请稍后再试");
	    });
	}

	$scope.updateBanner = function () {
		var data = {
			"id" : $stateParams.id,
			"positionType" : $scope.ad.positionType,
			"orgId":$scope.ad.orgId,
			"picName":$scope.ad.picName,
			"bannerTitle":$scope.ad.bannerTitle,
			"showNo":$scope.ad.showNo,
			"beginTime":moment(new Date($scope.ad.beginTime).getTime()),
			"endTime":moment(new Date($scope.ad.endTime).getTime()),
			"bannerUrl":$scope.ad.bannerUrl,
			"remark":$scope.ad.remark
		};
		$http.post("cmBanner/updateBanner",angular.toJson(data))
    		.success(function(msg){
    			$scope.notice(msg.msg);
    			if(msg.status){
    				$state.transitionTo('creditMgr.bannerMgr',null,{reload:true});
    			}else {
    				$scope.isDisabled = false;
    			}
    		}).error(function (msg) {
    			$scope.isDisabled = false;
    			$scope.notice('服务器异常,请稍后再试.');
    		});
	};

	//上传图片,定义控制器路径
	$scope.uploaderImg = new FileUploader({
		url: 'upload/fileUpload.do',
		queueLimit: 1,   //文件个数
		removeAfterUpload: true,  //上传后删除文件
		headers : {'X-CSRF-TOKEN' : $scope.csrfData}
	});
	//过滤格式
	$scope.uploaderImg.filters.push({
		name: 'imageFilter',
		fn: function(item /*{File|FileLikeObject}*/, options) {
			var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
			return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
		}
	});

	//1.如果有图片先上传图片，将url赋值给orgLogo
	//2.提交所有数据，新增组织
	$scope.submit = function(){
		$scope.submitting = true;
		if($scope.ad.bannerTitle==undefined || $scope.ad.bannerTitle.trim().length==0){
			$scope.notice("请输入标题");
			return ;
		}
		if($scope.ad.showNo==undefined || $scope.ad.showNo.trim().length==0){
			$scope.notice("请输入权重");
			return ;
		}

		var showno = $scope.ad.showNo;
		var merch = /^\d+$/;
		if(!showno.match(merch)){
			$scope.notice("权重输入正整数或0");
			return ;
		}

		if($scope.ad.beginTime==undefined || $scope.ad.beginTime.trim().length==0 || $scope.ad.endTime==undefined || $scope.ad.endTime.trim().length==0){
			$scope.notice("请选择上下线时间");
			return ;
		}

		$scope.isDisabled = true;
		if($scope.uploaderImg.queue.length == 0){
			//没有修改图片
			$scope.ad.picName = "";
			$scope.updateBanner();
		} else {
			//有修改图片
			$scope.uploaderImg.uploadAll();// 上传消息图片
			$scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
				if (response.url != null) { // 回调参数url
					$scope.ad.picName = response.url;
					$scope.updateBanner();
				} else {
					$scope.isDisabled = false;
					$scope.notice("上传图片失败");
					$scope.submitting = false;
				}
			};
		}
	};

});