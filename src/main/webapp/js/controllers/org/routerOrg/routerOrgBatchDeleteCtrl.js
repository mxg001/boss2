/**
 * 集群中收单商户/批量删除
 */
angular.module('inspinia',['angularFileUpload']).controller('routerOrgBatchDeleteCtrl',function($scope,$http,i18nService,SweetAlert,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    //上传图片,定义控制器路径
    var uploader = $scope.uploader = new FileUploader({
        url: 'routerOrg/routerOrgBatchDelete',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploader.filters.push({
        name: 'isFile',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    $scope.clearItems = function(){  //重新选择文件时，清空队列，达到覆盖文件的效果
        uploader.clearQueue();
    };

    $scope.importMsgShow = function(){
        setTimeout(function(){
            $('#importMsg').modal('show');
        },500)
    };
    $scope.importMsgHide = function(){
        $('#importMsg').modal('hide');
    };

    $scope.submitting = false;
    $scope.deleteText="";
    $scope.delNo="";
    $scope.msg="";
    $scope.errorList=[];
    $scope.isShow=true;
    $scope.acqMerBatchColseimport=function(){
        if($scope.submitting){
            return
        }
        $scope.submitting = true;
        uploader.uploadAll();//上传
        uploader.onSuccessItem = function(fileItem, response, status, headers) {//上传成功后的回调函数，在里面执行提交
            $scope.delNo=response.delNo;
            $scope.msg=response.msg;
            $scope.errorList=response.errorList;
            if($scope.delNo==null){
                //处理返回数据
                $scope.mgsInfo=$scope.msg+"\n";
                var errorList=$scope.errorList;
                if(errorList!=null&&errorList.length>0){
                    for(var i=0;i<errorList.length;i++){
                        $scope.mgsInfo=$scope.mgsInfo+errorList[i].msg+"\n";
                    }
                }
                $scope.importMsgShow();
                $scope.submitting = false;
            }
            $http.post("routerOrg/deleteImportDelCount",$scope.delNo)
                .success(function(data){
                    $scope.isShow=true;
                    $scope.deleteText="";
                    for(var i=0;i<data.length;i++){
                        if((data[i].acqMerCount-1)===0){
                            $scope.deleteText+=''+data[i].group_code+''+data[i].group_name+',收单商户数量为0;\r\n';
                        }
                    }
                    if($scope.deleteText==""){
                        $scope.isShow=false;
                    }
                    $("#showModel").modal("show");
                });

        };
    };

    $scope.deleteOp=function(){
        $http.post("routerOrg/deleteImportDelBatch",$scope.delNo)
            .success(function(datas){
                $("#showModel").modal("hide");
                //处理返回数据
                $scope.mgsInfo=$scope.msg+"\n";
                var errorList=$scope.errorList;
                if(errorList!=null&&errorList.length>0){
                    for(var i=0;i<errorList.length;i++){
                        $scope.mgsInfo=$scope.mgsInfo+errorList[i].msg+"\n";
                    }
                }
                $scope.importMsgShow();
                $scope.submitting = false;
            });
    }

    $scope.closeShowModel=function(){
        $("#showModel").modal("hide");
        $http.post("routerOrg/deleteAcqMerImportDel",$scope.delNo)
            .success(function(datas){
            });
    }

});