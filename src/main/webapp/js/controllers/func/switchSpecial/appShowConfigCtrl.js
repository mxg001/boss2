/**
 * APP前端显示配置
 */
angular.module('inspinia',['uiSwitch','angularFileUpload']).controller('appShowConfigCtrl',function($scope,$http,i18nService,SweetAlert,$stateParams,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);

    $scope.functionModularSelect =angular.copy($scope.functionModularTypeList);
    $scope.functionModularStr=angular.toJson($scope.functionModularSelect);

    $scope.userGrid={                           //配置表格
        data: 'result',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'functionName',displayName:'名称',width:120},
            { field: 'functionModular',displayName:'模块',width:120,cellFilter:"formatDropping:" +  $scope.functionModularStr },
            { field: 'showName',displayName:'下发名称',width:200,cellTemplate:
                '<div class="lh30">'+
                    '<input style="margin-left:10px;width: 180px;height:30px" ng-model="row.entity.showName" type="text" />'+
                '</div>'
            },
            { field: 'sort',displayName:'排序',width:120,cellTemplate:
                    '<div class="lh30">'+
                    '<input style="margin-left:10px;width: 100px;height:30px" ng-model="row.entity.sort" type="text" />'+
                    '</div>'
            },
            {field: 'isShow', displayName: '是否显示',width:120,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.isShow"/></span>'
            },
            {field: 'isRecommend', displayName: '是否推荐',width:120,cellTemplate:
                    '<span ><switch class="switch switch-s" ng-true-value="1" ng-false-value="0" ng-model="row.entity.isRecommend"/></span>'
            },
            { field: 'imgUrl',displayName:'推荐图片',width:180,
                cellTemplate:
                    '<div ng-show="row.entity.imgUrl!=null"> ' +
                        '<img style="width:70px;height:35px;" ng-click="grid.appScope.imgModeShow(row.entity.id)" ng-src="{{row.entity.imgUrl}}"/>' +
                    '</div>'+
                    '<div ng-show="row.entity.imgUrl==null"> ' +
                        '<span class="lh30" style="" ng-click="grid.appScope.imgModeShow(row.entity.id)" >上传</span>'+
                    '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.gridApi = gridApi;
        }
    };
    $scope.query=function(){
        if ($scope.loadImg) {
            return;
        }
        $scope.loadImg = true;
        $http.post("functionManager/getAppShowList","fmcId="+$stateParams.fmcId,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.result=data.list;
                }else{
                    $scope.notice(data.msg);
                }
                $scope.loadImg = false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.loadImg = false;
            });
    };
    $scope.query();


    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUploadReturnUrl',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>1*1024*1024){
                $scope.notice("图片大小不能超过1M");
                return false;
            }
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
    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            if($scope.result!=null&&$scope.result.length>0){
                for(var i=0;i<$scope.result.length;i++) {
                    var item = $scope.result[i];
                    if(item.id==$scope.addImgInfo.id){
                        item.imgUrl=response.imgUrl;
                        item.recommendIcon=response.url;
                        $scope.notice("保存图片成功");
                        $scope.imgModeHide();
                        return;
                    }
                }
            }
        }
        $scope.notice("保存图片失败");
    };

    $scope.imgModeShow = function(id){
        $scope.addImgInfo={id:id};
        $('#imgMode').modal('show');
    };

    $scope.imgModeHide = function(){
        $('#imgMode').modal('hide');
    };

    $scope.imgaddInfo=function(){
        if(uploaderImg.queue.length<1) {
            $scope.notice("上传图片不能为空!");
            return;
        }
        //有图片，取新图片
        uploaderImg.uploadAll();// 上传消息图片
    };

    $scope.submitting=false;
    // 保存提交
    $scope.submitInfo= function () {
        if($scope.submitting){
            return;
        }
        $scope.result;
        //校验
        if($scope.result!=null&&$scope.result.length>0){
            for(var i=0;i<$scope.result.length;i++){
                var item=$scope.result[i];
                if(item.showName==null||item.showName==""){
                    $scope.notice("第"+(i+1)+"行的下发名称不能为空!");
                    return;
                }
                if(item.sort==null||item.sort===""){
                    $scope.notice("第"+(i+1)+"行的排序不能为空!");
                    return;
                }else {
                    var regName = /^\d*$/;
                    if (!regName.test(item.sort)) {
                        $scope.notice("第" + (i + 1) + "行的排序格式不正确!");
                        return;
                    }
                }
            }
        }else{
            return;
        }
        $scope.submitting=true;
        // //数据转换
        $scope.addList=angular.copy($scope.result);
        for(var i=0;i<$scope.addList.length;i++){
            var item=$scope.addList[i];
            if(item.isShow==true){
                item.isShow=1;
            }else{
                item.isShow=0;
            }
            if(item.isRecommend==true){
                item.isRecommend=1;
            }else{
                item.isRecommend=0;
            }
            item.fmcId=$stateParams.fmcId;
            item.imgUrl=null;
        }
        var data={
            fmcId:$stateParams.fmcId,
            addList:angular.toJson($scope.addList)
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("functionManager/saveAppShowList",data,postCfg)
            .success(function(data){
                if(data.status){
                    $scope.query();
                    $scope.notice(data.msg);
                }else{
                    $scope.notice(data.msg);
                }
                $scope.submitting=false;
            })
            .error(function(data){
                $scope.notice(data.msg);
                $scope.submitting=false;
            });
    };
});