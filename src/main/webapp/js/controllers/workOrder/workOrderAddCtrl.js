
angular.module('inspinia',['infinity.angular-chosen','angularFileUpload']).controller('workOrderAddCtrl',function($scope,$http,$state,$stateParams,i18nService,$timeout,FileUploader){
	  //数据源
	  i18nService.setCurrentLang('zh-cn');
    $scope.endReplyDaysSelect = [{text:"请选择",value:""}];//截止回复天数
    $scope.deptSelect = angular.copy(initData.DEPT_LIST);
    $scope.deptSelect.unshift({text:"请选择",value:""});
    $scope.info = {workTypeId:"",receiveAgentNode:"",urgeStatus:"",endReplyDays:"",workContent:"",workFileInfos:[]};
    $scope.nameSelect = [];
    $scope.urgeStatusSelect = [{text:"请选择",value:""},{text:"是",value:"1"},{text:"否",value:"0"}]

    $scope.initEndReplyDays = function(){
        var arr = [1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,30];
        for(var i in arr){
            $scope.endReplyDaysSelect.push({text:"接收工单"+arr[i]+"个工作日内(含当天)回复",value:arr[i]})
        }
    }
    $scope.initEndReplyDays();

    //获取本人负责的工单类型
    $http.get('workType/getMyDutyType')
      .success(function(result) {
          if(!result.status){
              return;
          }
          if(result.data==null || result.data.length==0) {
              $scope.nameSelect.push({value: "", text: "全部"});
          }else{
              $scope.nameSelect.push({value: "", text: "全部"});
              for(var i=0; i<result.data.length; i++){
                  $scope.nameSelect.push({value:result.data[i].id,text: result.data[i].name,dealProcessName:result.data[i].dealProcessName});
              }
          }
      });

    //回显流程
    $scope.showDealProcess = function(){
        if($scope.nameSelect!=null && $scope.nameSelect.length>0){
            for(var i=0; i<$scope.nameSelect.length; i++){
                if($scope.info.workTypeId == $scope.nameSelect[i].value){
                    $scope.info.dealProcessName = $scope.nameSelect[i].dealProcessName;
                }
            }
        }

    }

    //代理商
    $scope.agentList=[{value:"",text:"全部"}];

    $http.post("agentInfo/selectAllInfo")
      .success(function(msg){
          //响应成功
          for(var i=0; i<msg.length; i++){
              $scope.agentList.push({value:msg[i].agentNode,text:msg[i].agentNo + " " + msg[i].agentName});
          }
      });

    //异步获取直属代理商
    var oldValue="";
    var timeout="";
    $scope.getAgentList = function(value) {
        $scope.agentt = [];
        var newValue=value;
        if(newValue != oldValue){
            if (timeout) $timeout.cancel(timeout);
            timeout = $timeout(
              function(){
                  $http.post('agentInfo/selectAllInfo','item=' + value,
                    {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                    .then(function (response) {
                        if(response.data.length==0) {
                            $scope.agentt.push({value: "", text: "全部"});
                        }else{
                            $scope.agentt.push({value: "", text: "全部"});
                            for(var i=0; i<response.data.length; i++){
                                $scope.agentt.push({value:response.data[i].agentNode,text:response.data[i].agentNo + " " + response.data[i].agentName});
                            }
                        }
                        $scope.agentList = $scope.agentt;
                        oldValue = value;
                    });
              },800);
        }
    };



    $scope.showAddImgModal = function(){
        $('#imgModal').modal('show');
    }
    $scope.hideAddImgModal = function(){
        $('#imgModal').modal('hide');
    }


    $scope.add= function(){
        $scope.submitting = true;
        $http({
            url: 'workOrder/add',
            method: 'POST',
            data: $scope.info
        }).success(function (result) {
            if(result.status){
                $scope.submitting = false;
                $state.transitionTo('workOrder.workOrderManager',null,{reload:false});
                $scope.notice(result.msg);
            }else{
                $scope.submitting = false;
                $scope.notice(result.msg);
            }
        });
    }


    

    //上传,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'upload/fileUploadAll.do',
        queueLimit: 15,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 15;
        }
    });
    //过滤格式
    uploader.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            if(item.size>10*1024*1024){
                return false;
            }
            return true;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };

    //新增info
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        $scope.tall="";
        uploader.onSuccessItem = function(fileItem,response, status, headers) {
            // 上传成功后的回调函数，在里面执行提交
            if (response.url != null) { // 回调参数url
                $scope.info.workFileInfos.push({fileName:fileItem.file.name,fileUrl:response.url,fileType:fileItem.file.type});
            }
        };
        uploader.onCompleteAll = function() {
            $scope.add();
        };
        if($scope.uploader.queue.length<1){
            $scope.add();
        }else{
            $scope.uploader.uploadAll();
        }
    };
    

});


