/**
 * 添加公告
 * 注释的代码为后期需要配合前端实现的模块
 * 1.代理商搜索下拉框
 * 2.根据该下拉框动态显示业务产品表
 */
angular.module('inspinia',['angularFileUpload']).controller("addNoticeCtrl", function($scope, $http, $state, $stateParams,FileUploader) {
    //数据源
    $scope.sysType=[{text:"商户",value:'1'},{text:"代理商",value:'2'}];
    $scope.agentBusiness = [{text:"全部",value:'0'},{text:"指定一级代理商下（包括下级发展）的商户",value:'1'}];
    $scope.noticeInfo = {title:null,content:null,attachment:null,sysType:'1',agentNo:'0',
        bpId:null,receiveType:null,link:null,oemType:null,validBeginTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 00:00:00',validEndTime:moment(new Date().getTime()).format('YYYY-MM-DD')+' 23:59:59',winNoticeStatus:-1,showStatus:"0"};
    $scope.baseInfo = {agentBusiness:'0',team:'0',isAll:'1'};
    //用来决定公告接受对象为代理商时的两个标志位
    $scope.isAll = [{text:'所有代理商',value:'1'},{text:'所有一级代理商',value:'2'}];
    $scope.agent = [];			//所有的一级代理商
    $scope.customerData = [];	//业务产品
    $scope.allProduct = [];		//缓存所有的业务产品
    $scope.allProduct2 = [];		//缓存所有的业务产品
    var uploaderFileFlag = true;		//是否可以提交数据，默认是可以提交的，当有文件准备上传时，为false，上传完成后置为true
    $scope.fileFlagHide = true;	//“附件”，新增公告时，隐藏“点击下载”
    $scope.imgFlagHide = true;
    $scope.titleImgFlagHide = true;
    $scope.oemTypes=[];//缓存所有指定组织
    //所有的业务产品
    $http.get('notice/addNotice.do').success(function(msg){
        $scope.customerData = msg.allProduct;
        $scope.allProduct = msg.allProduct;
        $scope.allProduct2 = msg.allProduct;
        $scope.oemTypes=msg.oemTypes;

        $scope.agent.push({
            value : '0',
            text : '全部'
        });
        for(var i=0; i<msg.allAgent.length; i++){
            $scope.agent.push({value:msg.allAgent[i].agentNo,text:msg.allAgent[i].agentName});
        }
    });

    $scope.oemListResult=false;
    //超级盟主品牌列表
    $scope.oemListes=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemListes.push({brandCode:list[i].brandCode,brandName:list[i].brandName});
                    }
                }
            }
        });

    //业务产品与代理商之间的切换
    $scope.getAgent = function(){
        if($scope.baseInfo.agentBusiness == '0'){
            $scope.noticeInfo.agentNo = '0';
            $scope.customerData = $scope.allProduct;
        }
    };

//  根据代理商ID,返回其包含的所有业务产品	notice.agentNo 对应 agent_info里面的agent_no
    $scope.getBpList = function(){
        $http.get('agentInfo/selectProductById/' + $scope.noticeInfo.agentNo).success(function(data){
            $scope.customerData = [];
            $scope.customerData = data;
            $scope.allProduct2 = data;

        }).error(function(){
        });
    };

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
            if(item.size>500*1024){
                return false;
            }
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
    uploaderImg.onAfterAddingFile = function(fileItem) {
        uploaderImgFlag = false;
    }
    uploaderImg.removeFromQueue = function(value){
        uploaderImgFlag = true;
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }

    // 上传附件,定义控制器路径
    var uploaderFileFlag = true;
    var uploaderFile = $scope.uploaderFile = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderFile.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderFile.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderFile.onAfterAddingFile = function(fileItem) {
        uploaderFileFlag = false;
    }
    uploaderFile.removeFromQueue = function(value){
        uploaderFileFlag = true;
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    }

    /***
     * 上传图片标题-start
     */
    var uploaderTitleImgFlag = "false";
    var uploaderTitleImg = $scope.uploaderTitleImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: true,  //上传后删除文件
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderTitleImg.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            if(item.size>500*1024){
                return false;
            }
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderTitleImg.filters.push({
        name: 'imageFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderTitleImg.onAfterAddingFile = function(fileItem) {
        uploaderTitleImgFlag = "uploading";
    };

    uploaderTitleImg.removeFromQueue = function(value){
        uploaderTitleImgFlag = "true";
        var index = this.getIndexOfItem(value);
        var item = this.queue[index];
        if (item.isUploading) item.cancel();
        this.queue.splice(index, 1);
        item._destroy();
        this.progress = this._getTotalProgress();
    };
    /***
     * 上传图片标题-end
     */

    //商户业务产品表格
    $scope.productsGrid = { // 配置表格
        data : 'customerData',
        enableHorizontalScrollbar : 0, // 去掉滚动条
        enableVerticalScrollbar : 1, // 去掉滚动条
        columnDefs : [ // 表格数据
            {field : 'teamName',displayName : '所属组织'},
            {field : 'bpName',displayName : '业务产品名称'},
            {field : 'remark',displayName : '业务产品说明'}
        ],
        onRegisterApi : function(gridApi) {
            $scope.gridApiProduct = gridApi;
        }
    };

    //提交图片
    $scope.submit = function(){
        $scope.submitting = true;
        //1.没有等待上传的
        if(uploaderImgFlag && uploaderFileFlag){
            $scope.submitData();
        }
        //2.有消息图片
        if(!uploaderImgFlag && uploaderFileFlag){
            uploaderImg.uploadAll();// 上传消息图片
            uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.noticeInfo.messageImg= response.url;
                    $scope.submitData();
                }
            }
        }
        //3.消息图片没有，附件有
        if(uploaderImgFlag && !uploaderFileFlag){
            //有上传消息图片等待上传
            uploaderFile.uploadAll();// 上传附件
            uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.noticeInfo.attachment = response.url;
                    $scope.submitData();
                }
            }
        }
        //4.消息图片有，附件有
        if(!uploaderImgFlag && !uploaderFileFlag){
            uploaderImg.uploadAll();// 上传消息图片
            uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                if (response.url != null) { // 回调参数url
                    $scope.noticeInfo.messageImg= response.url;
                    uploaderFile.uploadAll();// 上传附件
                    uploaderFile.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
                        if (response.url != null) { // 回调参数url
                            $scope.noticeInfo.attachment = response.url;
                            $scope.submitData();
                        }
                    }
                }
            }
        }
    }

    $scope.oemTypeCheck= function(){
        $scope.oemListResult=false;
        $("input:checkbox[name='oemtypecheck']:checked").each(function() { // 遍历多选框
            if($(this).val()==11){
                $scope.oemListResult=true;
            }
        });
    }
    $scope.oemListCheckAll = function(oemListCheckFlag){
        if(oemListCheckFlag){
            angular.forEach($scope.oemListes,function(data){
                data.checkedState = 1;
            })
        } else {
            angular.forEach($scope.oemListes,function(data){
                data.checkedState = 0;
            })
        }
    }
    //提交
    $scope.submitData = function(){
        if(uploaderTitleImgFlag==="uploading"){
            console.log("开始上传图片标题");
            uploaderTitleImg.uploadAll();//开始上传图片标题
            uploaderTitleImg.onSuccessItem = function(fileItem, response, status, headers) {
                console.info('onSuccessItem', fileItem, response, status, headers);
                $scope.noticeInfo.titleImg = response.url;
                uploaderTitleImgFlag = "true";
                console.log("图片标题上传完成");
                $scope.submitData();
            };
            return;
        }
        $scope.submitting = true;
        $scope.noticeInfo.content= $('.note-editable').html();
        if(typeof($scope.noticeInfo.content) == undefined || typeof($scope.noticeInfo.content) === "undefined" || $scope.noticeInfo.content == ""){
            $scope.notice("通告内容不能为空");
            $scope.submitting = false;
            return;
        }
        var obj;
        var data;
        if($scope.noticeInfo.sysType=="1"){
            obj = $scope.gridApiProduct;
            data = {"notice":$scope.noticeInfo,"baseInfo":$scope.baseInfo,"products":obj.selection.getSelectedRows()};
        }else{
            obj = $scope.gridApiProduct2;
            var str="";
            $("input:checkbox[name='oemtypecheck']:checked").each(function() { // 遍历多选框
                console.log($(this).val());  // 每一个被选中项的值
                if(""==str){
                    str=str+$(this).val();
                }else{
                    str=str+","+$(this).val();
                }
            });
            if(""==str){
                $scope.notice("指定组织至少选择一个");
                $scope.submitting = false;
                return;
            }
            $scope.noticeInfo.oemType=str;
            str=""
            $("input:checkbox[name='oemlistcheck']:checked").each(function() { // 遍历多选框
                console.log($(this).val());  // 每一个被选中项的值
                if(""==str){
                    str=str+$(this).val();
                }else{
                    str=str+","+$(this).val();
                }
            });
            $scope.noticeInfo.oemList=str;
            data = {"notice":$scope.noticeInfo,"baseInfo":$scope.baseInfo};
        }

        if($scope.noticeInfo.showStatus=="1"){
            if(!($scope.noticeInfo.validBeginTime&& $scope.noticeInfo.validEndTime)){
                $scope.notice("有效时间不能为空");
                $scope.submitting = false;
                return;
            }
            if($scope.noticeInfo.validBeginTime> $scope.noticeInfo.validEndTime){
                $scope.notice("开始时间不能大于结束时间");
                $scope.submitting = false;
                return;
            }
        }

        $http.post("notice/saveNotice.do",angular.toJson(data))
            .success(function(msg){
                if(msg.status){
                    $scope.notice(msg.msg);
                    $state.transitionTo('sys.addNotice',null,{reload:true});
                    $scope.submitting = false;
                } else {
                    $scope.notice(msg.msg);
                    $scope.submitting = false;
                }
            }).error(function(){
            $scope.submitting = false;
        });
    }

});
