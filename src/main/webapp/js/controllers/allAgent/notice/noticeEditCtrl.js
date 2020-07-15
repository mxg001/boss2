/**
 * 广告新增
 */
angular.module('inspinia',['angularFileUpload']).controller('noticeAllAgentEditCtrl',function($scope,$http,i18nService,$state,$stateParams,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.types= [{text:"普通公告",value:1},{text:"首页活动弹层",value:2}];
    $scope.addInfo={};

    $scope.getNotice=function () {
        $http.post("noticeAllAgent/getNotice","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.notice;
                    $scope.addInfo.upTime=$scope.addInfo.upTime==null?null:moment($scope.addInfo.upTime).format('YYYY-MM-DD HH:mm:ss');
                    $scope.addInfo.downTime=$scope.addInfo.downTime==null?null:moment($scope.addInfo.downTime).format('YYYY-MM-DD HH:mm:ss');
                    $scope.oemNoList=data.notice.oemNoList;
                    $scope.orgList=data.notice.orgList;
                    $scope.getOemList();
                }
            });
    };
    $scope.getNotice();

    //组织列表
    $scope.getOemList=function () {
        $http.post("awardParam/getOemList",
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.oemResult=data.list;
                }
            });
    };

    $scope.oemGrid={                           //配置表格
        data: 'oemResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'brandCode',displayName:'品牌编码',width:200},
            { field: 'brandName',displayName:'品牌名称',width:200}
        ],
        onRegisterApi: function(gridApi) {
            $scope.oemGridApi = gridApi;
            $scope.oemGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                //行选中事件
                $scope.getOrgList();
            });
            $scope.oemGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                //全选事件
                $scope.getOrgList();
            });
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.oemNoList != null){
                for(var i=0;i<$scope.oemNoList.length;i++){
                    if(row.entity.brandCode==$scope.oemNoList[i]){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };

    $scope.orgGrid={                           //配置表格
        data: 'orgResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'userCode',displayName:'用户编号',width:180},
            { field: 'oneAgentNo',displayName:'v2代理商编号',width:180},
            { field: 'oneAgentName',displayName:'v2代理商名称',width:180},
        ],
        onRegisterApi: function(gridApi) {
            $scope.orgGridApi = gridApi;
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.orgList != null){
                for(var i=0;i<$scope.orgList.length;i++){
                    if(row.entity.userCode==$scope.orgList[i]){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };

    $scope.getOrgList=function () {
        var selectList = $scope.oemGridApi.selection.getSelectedRows();
        if(selectList!=null&&selectList.length>0){
            var ids="";
            if(selectList!=null&&selectList.length>0){
                for(var i=0;i<selectList.length;i++){
                    ids = ids + "'"+selectList[i].brandCode +"',";
                }
            }
            ids=ids.substring(0,ids.length-1);
            //获取用户机构列表
            $http.post("userAllAgent/getOrgList",
                "brandCodes="+ids+"&userType=1",
                {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                .success(function(data){
                    if(data.status){
                        $scope.orgResult=data.list;
                    }
                });
        }else{
            $scope.orgResult=[];
        }
    };


    $scope.submitting = false;
    //新增banner
    $scope.addNotice = function(){
        var num=/^(\+)?\d+(\.\d+)?$/;
        if($scope.submitting){
            return;
        }

        if(($scope.addInfo.content==null||$scope.addInfo.content.trim().length==0)&&($scope.addInfo.linkUrl==null||$scope.addInfo.linkUrl.trim().length==0)){
            $scope.notice("公告内容或链接不能为空!");
            return;
        }

        if($scope.addInfo.imgUrl==null){
            if(uploaderImg1.queue.length<1){
                $scope.notice("封面图片不能为空!");
                return;
            }
            if($scope.addInfo.img==null){
                $scope.notice("封面图片不能为空!");
                return;
            }
        }
        if($scope.addInfo.type==2){
            if($scope.addInfo.homeImgUrl==null){
                if(uploaderImg2.queue.length<1){
                    $scope.notice("首页弹层图片不能为空!");
                    return;
                }
                if($scope.addInfo.homeImg==null){
                    $scope.notice("首页弹层图片不能为空!");
                    return;
                }
            }
            if($scope.addInfo.priority==null||$scope.addInfo.priority.toString().trim().length==0){
                $scope.notice("权重不能为空!");
                return;
            }
            if(isNaN($scope.addInfo.priority)){
                $scope.notice("权重必须是数字!");
                return;
            }
            if(!num.test($scope.addInfo.priority)){
                $scope.notice("权重不能为负数!");
                return;
            }
        }

        var selectList = $scope.oemGridApi.selection.getSelectedRows();
        if(selectList==null||selectList.length==0){
            $scope.notice("下发组织不能为空!");
            return;
        }
        var ids="";
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                ids = ids + selectList[i].brandCode +",";
            }
        }
        if(ids==""){
            $scope.notice("下发组织不能为空!");
            return;
        }
        $scope.addInfo.oemNoSet=ids.substring(0,ids.length-1);

        var orgSelectList = $scope.orgGridApi.selection.getSelectedRows();
        var orgs="";
        if(orgSelectList!=null&&orgSelectList.length>0){
            for(var i=0;i<orgSelectList.length;i++){
                orgs = orgs + orgSelectList[i].userCode +",";
            }
            $scope.addInfo.orgSet=orgs.substring(0,orgs.length-1);
        }else{
            $scope.addInfo.orgSet=-1;
        }
        $scope.submitting = true;

        $scope.subAddinfo=angular.copy($scope.addInfo);
        $scope.subAddinfo.content=null;

        var data={
            info:angular.toJson($scope.subAddinfo),
            content:$scope.addInfo.content
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("noticeAllAgent/updateNotice",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.notice',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    };

    //图1
    //上传图片,定义控制器路径
    var uploaderImg1 = $scope.uploaderImg1 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg1.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg1.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.img= response.url;
        }
    };

    //上传图片,定义控制器路径
    var uploaderImg2 = $scope.uploaderImg2 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg2.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg2.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg2.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.homeImg= response.url;
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
});