
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workTypeDetailCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){

    //数据源
    i18nService.setCurrentLang('zh-cn');
    $scope.replyTypeSelect = [{text:"请选择",value:""},{text:"一级和所属代理商均可回复",value:1},{text:"仅有一级代理商回复",value:2},{text:"仅有所属代理商回复",value:3}];
    $scope.endReplyDaysSelect = [{text:"请选择",value:""}];//截止回复天数
    $scope.deptSelect = angular.copy(initData.DEPT_LIST);
    $scope.deptSelect.unshift({text:"请选择",value:""});
    $scope.flowInfo = {endReplyDays:"",deptNo:"",flowDesc:""};
    $scope.dealProcessArr = [];
    $scope.info = {replyType:"",agentShow:"0",dealProcess:"",workFileInfos:[],workFlowNodeList:[]};
    $scope.imgInfo = {fileName:"",fileUrl:"",fileType:""};
    $scope.imgList = [];

    $scope.initEndReplyDays = function(){
        var arr = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,30];
        for(var i in arr){
            $scope.endReplyDaysSelect.push({text:"接收工单"+arr[i]+"个工作日内(含当天)回复",value:arr[i]})
        }
    }
    $scope.initEndReplyDays();

    $http({
        url: 'workType/getWorkTypeById?id='+$stateParams.id,
        method: 'get',
    }).success(function (result) {
        if(result.status){
            $scope.info = result.data;
            //回显流程
            if($scope.info.workFlowNodeList!=null && $scope.info.workFlowNodeList.length>0){
                for(var i in $scope.info.workFlowNodeList){
                    var node = $scope.info.workFlowNodeList[i];
                    $scope.dealProcessArr.push({index:$scope.dealProcessArr.length,
                        deptName:node.deptName,
                        entity:{deptNo:node.deptNo,
                            flowDesc:node.flowDesc,
                            endReplyDays:node.endReplyDays}
                    });
                }
            }
            //回显图片
            if($scope.info.workFileInfos!=null && $scope.info.workFileInfos.length>0){
                for(var i in $scope.info.workFileInfos){
                    var file = $scope.info.workFileInfos[i];
                    $scope.imgList.push({index:$scope.imgList.length,
                        entity:angular.copy(file)
                    });
                }
            }
        }else{
            $scope.notice(result.msg);
        }
    });


    //添加流程
    $scope.addFlow = function(){
        //如果是代理商 需要判断流程中是否存在了代理商  存在则不能添加
        if($scope.flowInfo.deptNo=="999"){
            if($scope.info.workFlowNodeList!=null && $scope.info.workFlowNodeList.length>0){
                for(var i in $scope.info.workFlowNodeList){
                    if($scope.info.workFlowNodeList[i].deptNo=="999"){
                        $scope.notice("一个处理流程仅能添加一个代理商");
                        return;
                    }
                }
            }
        }

        if($scope.flowInfo.index>=0){//修改
            $scope.info.workFlowNodeList[$scope.flowInfo.index] = angular.copy($scope.flowInfo);
            //回显部门名称
            for(var i in $scope.deptSelect){
                if($scope.deptSelect[i].value==$scope.flowInfo.deptNo){
                    $scope.dealProcessArr[$scope.flowInfo.index] = {index:$scope.flowInfo.index,
                        deptName:$scope.deptSelect[i].text,
                        entity:{deptNo:$scope.flowInfo.deptNo,
                            flowDesc:$scope.flowInfo.flowDesc,
                            endReplyDays:$scope.flowInfo.endReplyDays}
                    };
                }
            }
        }else{//新增
            $scope.info.workFlowNodeList[$scope.info.workFlowNodeList.length] = angular.copy($scope.flowInfo);
            //回显部门名称
            for(var i in $scope.deptSelect){
                if($scope.deptSelect[i].value==$scope.flowInfo.deptNo){
                    $scope.dealProcessArr.push({index:$scope.dealProcessArr.length,
                        deptName:$scope.deptSelect[i].text,
                        entity:{deptNo:$scope.flowInfo.deptNo,
                            flowDesc:$scope.flowInfo.flowDesc,
                            endReplyDays:$scope.flowInfo.endReplyDays}
                    });
                }
            }
        }

        $scope.flowInfo = {endReplyDays:"",deptNo:"",flowDesc:""};
        $scope.hideAddFlowModal();
    }

    $scope.editFlowInfo = function(index){
        $scope.flowInfo = angular.copy($scope.dealProcessArr[index].entity);
        $scope.flowInfo.index = index;
        $scope.showAddFlowModal()
    }

    //添加图片
    $scope.addImg = function(){
        if($scope.imgInfo.fileUrl==null || $scope.imgInfo.fileUrl==""){
            $scope.notice("请上传图片");
            return;
        }
        if($scope.imgList.length>=15){
            $scope.notice("最多只能请上传15张图片");
            return;
        }
        $scope.imgList.push({index:$scope.imgList.length,
            entity:angular.copy($scope.imgInfo)
        });
        $scope.info.workFileInfos.push(angular.copy($scope.imgInfo));

        //重置图片添加弹窗
        $scope.imgInfo.fileName = "";
        $scope.uploaderImg.queue = [];
        $scope.hideAddImgModal();
    }

    //删除图片
    $scope.delImg = function(index){
        $scope.imgList.splice(index,1);
        $scope.info.workFileInfos.splice(index,1);
    }


    $scope.showAddFlowModal = function(){
        $('#flowModal').modal('show');
    }
    $scope.hideAddFlowModal = function(){
        $('#flowModal').modal('hide');
    }

    $scope.showAddImgModal = function(){
        $('#imgModal').modal('show');
    }
    $scope.hideAddImgModal = function(){
        $('#imgModal').modal('hide');
    }



    $scope.add= function(){

        if($scope.dealProcessArr!=null && $scope.dealProcessArr.length>0){
            $scope.info.dealProcess = "";
            for(var i in $scope.dealProcessArr){
                $scope.info.dealProcess += $scope.dealProcessArr[i].entity.deptNo+"-";
            }
            $scope.info.dealProcess = $scope.info.dealProcess.substr(0,$scope.info.dealProcess.lastIndexOf("-"));
        }

        if($scope.info.dealProcess==null || $scope.info.dealProcess==""){
            $scope.notice("处理流程不能为空！");
            return;
        }
        $scope.submitting = true;
        $http({
            url: 'workType/edit',
            method: 'POST',
            data: $scope.info
        }).success(function (result) {
            if(result.status){
                $scope.addInfo = null;
                $scope.submitting = false;
                $state.transitionTo('workOrder.workOrderType',null,{reload:false});
                $scope.notice(result.msg);
            }else{
                $scope.submitting = false;
                $scope.notice(result.msg);
            }

        });
    }

    //上传图片,定义控制器路径
    $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUploadReturnUrl',
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
            if(item.size>10*1024*1024){
                $scope.notice("上传图片太大了!");
                return false;
            }
            return this.queue.length < 1;
        }
    });

    $scope.uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.imgInfo.fileName = fileItem.file.name
            $scope.imgInfo.realUrl = response.imgUrl;
            $scope.imgInfo.fileUrl = response.url;
            $scope.imgInfo.fileType = fileItem.file.type;
        }
    };


    /**
     *富文本框按钮控制
     */
    $scope.summeroptions = {
        toolbar: [
            ['style', ['bold', 'italic', 'underline','clear']],
            ['fontface', ['fontname']],
            ['textsize', ['fontsize']],
            ['fontclr', ['color']],
            ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
            ['height', ['height']],
            ['insert', ['hr']],
            // ['insert', ['link','picture','video','hr']],
            ['view', ['codeview']]
        ]
    };
})
.filter('trustHtml', function ($sce) {
  return function (input) {
      return $sce.trustAsHtml(input);
  }
});


