/**
 * 商品编辑、详情
 */
angular.module('inspinia',['angularFileUpload']).controller('goodAllAgentEditCtrl',function($scope,$http,i18nService,FileUploader,SweetAlert,$stateParams,$state){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.addInfo={brandCode:"",isMulti:0};
    $scope.shipWayes = [{text:"请选择商品类型",value:""},{text:"机具类",value:1},{text:"物料类",value:2}];

    //组织列表
    $scope.oemList=[];
    $http.post("awardParam/getOemList",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.oemList.push({value:"",text:"全部"});
                var list=data.list;
                if(list!=null&&list.length>0){
                    for(var i=0; i<list.length; i++){
                        $scope.oemList.push({value:list[i].brandCode,text:list[i].brandName});
                    }
                }
            }
        });
    $scope.addInfo.groupCodeOld = "";
    $scope.goodsGroupList = [];
    $scope.goodsGroupQuery = function () {
        var brandCode = $scope.addInfo.brandCode;
        if (brandCode === "" || typeof(brandCode) === "undefined") {
            $scope.addInfo.brandCode = "";
            return;
        }
        $scope.goodsGroupList = [];
        $scope.addInfo.groupCode = "";
        $http.post("goodAllAgent/goodsGroupQuery", "info=" + brandCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function (data) {
                if (data.status) {
                    var list = data.list;
                    if (list != null && list.length > 0) {
                        for (var i = 0; i < list.length; i++) {
                            $scope.goodsGroupList.push({value: list[i].group_code, text: list[i].group_name});
                        }
                        if($scope.addInfo.groupCodeOld===""){
                            $scope.addInfo.groupCode = $scope.goodsGroupList[0].value;
                        }else{
                            $scope.addInfo.groupCode = $scope.addInfo.groupCodeOld;
                            $scope.addInfo.groupCodeOld = "";
                        }
                    }else{
                        $scope.notice("请为当前品牌创建商品分类!");
                    }
                }
            });
    };
    $scope.imgUrl="";
    $scope.imgUrl2="";
    $scope.imgUrl3="";
    $scope.imgsUrl=[];
    $scope.imgsMap={};
    $scope.getGood=function () {
        $http.post("goodAllAgent/getGood","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.formatResult=data.goodsPrice;
                    $scope.addInfo=data.good;
                    $scope.addInfo.groupCodeOld = $scope.addInfo.groupCode;
                    $scope.goodsGroupQuery();
                    $scope.imgUrl=data.good.img;
                    $scope.imgUrl2=data.good.img2;
                    $scope.imgUrl3=data.good.img3;
                    $scope.imgsUrl=data.imgsUrl;
                    $scope.imgsMap=data.imgsMap;
                }
            });
    };
    $scope.getGood();

    $scope.deleteImg=function (sta) {
        var title="";
        if(sta==2){
            title="确认删除图2?";
        }else if(sta==3){
            title="确认删除图3?";
        }
        SweetAlert.swal({
                title:title,
                showCancelButton: true,
                confirmButtonColor: "#DD6B55",
                confirmButtonText: "确认",
                cancelButtonText: "取消",
                closeOnConfirm: true,
                closeOnCancel: true
            },
            function (isConfirm) {
                if (isConfirm) {
                    $http.post("goodAllAgent/deleteGoodImg","id="+$stateParams.id+"&status="+sta,
                        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
                        .success(function(data){
                            if(data.status){
                                $scope.notice(data.msg);
                            }else{
                                $scope.notice(data.msg);
                            }
                            $scope.getGood();
                        });
                }
            });

    };

    $scope.submitting = false;
    //保存
    $scope.saveProduct=function () {
        if($scope.submitting){
            return;
        }
        if("uploading"===$scope.completeAllImgs){
            $scope.notice("还有图片未上传完成,请稍候再试!");
            return;
        }
        $scope.addInfo.detailImgs = "";
        $("#imageUL li").each(function () {
            var yunName = $(this).attr("yun_name");
            if(yunName==="undefined" || typeof (yunName) ==="undefined"){
            }else{
                $scope.addInfo.detailImgs += yunName+",";
            }
        });
        // var status=false;
        // if($scope.addInfo.detailImgs.split("undefined").length>1){
        //     status=true;
        // }
        // if(status){
        //     $scope.notice("还有图片未上传完成,请稍候再试!");
        //     return;
        // }
        if(!$scope.formatResult.length>0){
            $scope.notice("商品规格不能为空!");
            return;
        }
        $scope.submitting = true;
        $scope.subInfo=angular.copy($scope.addInfo);
        if($scope.uploaderImg1.queue.length<1){
            $scope.subInfo.img=null;
        }
        if($scope.uploaderImg2.queue.length<1){
            $scope.subInfo.img2=null;
        }
        if($scope.uploaderImg3.queue.length<1){
            $scope.subInfo.img3=null;
        }
        $scope.subInfo.goodsDesc=null;
        var data={
            info:angular.toJson($scope.subInfo),
            formatList:angular.toJson($scope.formatResult),
            deleteinfo:angular.toJson($scope.deleteResult),
            desc:$scope.addInfo.goodsDesc
        };
        var postCfg = {
            headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("goodAllAgent/saveGood",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.goodManage',null,{reload:true});
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

    //图2
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
            $scope.addInfo.img2= response.url;
        }
    };

    //图3
    var uploaderImg3 = $scope.uploaderImg3 = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，只能上传一个
    uploaderImg3.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < 1;
        }
    });
    //过滤格式
    uploaderImg3.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });
    uploaderImg3.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.addInfo.img3= response.url;
        }
    };

    $scope.uploaderImgListMax = 20;
    //批量上传
    var uploaderImgList = $scope.uploaderImgList = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: $scope.uploaderImgListMax,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:true,     //文件加入队列之后自动上传，默认是false
        headers : {'X-CSRF-TOKEN' : $scope.csrfData}
    });
    //过滤长度，限制最多上传个数
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item, options) {
            return this.queue.length < $scope.uploaderImgListMax - $("li[oldImage='oldImage']").length;
        }
    });
    //过滤格式
    uploaderImgList.filters.push({
        name: 'imageFilter',
        fn: function(item,options) {
            var type = '|' + item.type.slice(item.type.lastIndexOf('/') + 1) + '|';
            return '|jpg|png|jpeg|bmp|gif|'.indexOf(type) !== -1;
        }
    });

    uploaderImgList.onBeforeUploadItem = function(fileItem) {// 上传前的回调函数，在里面执行提交
        $scope.completeAllImgs = "uploading";
        console.log("图片开始上传[onBeforeUploadItem]");
        console.log("queue.length["+this.queue.length+"];uploaderImgListMax["+$scope.uploaderImgListMax+"]");
    };

    uploaderImgList.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            var itemIndex = this.getIndexOfItem(fileItem);
            $("#imageUL li[newImage='newImage']").eq(itemIndex).attr("yun_name",response.url);
            console.log("图片上传完成一项[onSuccessItem]" + response.url);
        }
    };

    $scope.completeAllImgs = "start";
    uploaderImgList.onBeforeUpload = function(){
        console.log("图片开始上传[onBeforeUpload]");
        $scope.completeAllImgs = "uploading";
    };
    uploaderImgList.onCompleteAll = function(){
        console.log("所有图片上传完成[onCompleteAll]");
        $scope.completeAllImgs = "end";
    };

    $scope.removeOldImg = function (id) {
        $("#" + id).remove();
        console.log($("li[oldImage='oldImage']").length);
    };


    $scope.modeState=true;
    $scope.baseInfo={};
    $scope.formatResult=[];
    $scope.deleteResult=[];
    $scope.isSize=true;

    $scope.formatGrid={                           //配置表格
        data: 'formatResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'price',displayName:'销售价(元)',width:120},
            { field: 'cost',displayName:'成本价(元)',width:120},
            { field: 'agio',displayName:'差价(元)',width:120},
            { field: 'color',displayName:'颜色',width:120},
            { field: 'size',displayName:'尺码',width:120},
            { field: 'id',displayName:'操作',width:200,pinnedRight:true, cellTemplate:
                '<div class="lh30">'+
                '<a ng-click="grid.appScope.editData(0,row.entity)"">添加尺码值 </a> ' +
                '<a ng-click="grid.appScope.editData(1,row.entity)"">编辑 </a> ' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.formatGridApi = gridApi;
        }
    };

    $scope.addModal = function(){
        $scope.modeState=true;
        $scope.isSize=true;
        $scope.baseInfo={};
        $('#addModal').modal('show');
    };
    $scope.cancel = function(){
        $scope.baseInfo={};
        $('#addModal').modal('hide');
    };
    $scope.saveModal = function(mode){
        if($scope.baseInfo!=null){
            dateList=$scope.formatResult;
            if(mode=="add"){
                if($scope.checkDate(dateList,$scope.baseInfo,null)){
                    dateList.push({
                        price:$scope.baseInfo.price,
                        cost:$scope.baseInfo.cost,
                        agio:$scope.baseInfo.agio,
                        color:$scope.baseInfo.color,
                        size:$scope.baseInfo.size
                    });
                    $scope.cancel();
                }

            }else if(mode=="edit"){
                var dateList;
                var item=$scope.oldBaseInfo;
                if($scope.checkDate(dateList,$scope.baseInfo,item)) {
                    for (var j = 0; j < dateList.length; j++) {
                        var dateItem = dateList[j];
                        if (item.price == dateItem.price
                            && item.cost == dateItem.cost
                            && item.agio == dateItem.agio
                            && item.color == dateItem.color
                            && item.size == dateItem.size) {

                            dateItem.price = $scope.baseInfo.price;
                            dateItem.cost = $scope.baseInfo.cost;
                            dateItem.agio = $scope.baseInfo.agio;
                            dateItem.color = $scope.baseInfo.color;
                            dateItem.size = $scope.baseInfo.size;
                        }
                    }
                    $scope.cancel();
                }
            }

        }
    };
    $scope.checkDate = function(dateList,info,oldInfo){
        if(Number(info.price)<0&&Number(info.price)!=-1){
            $scope.notice("销售价不能小于0!");
            return false;
        }
        if(Number(info.cost)<0&&Number(info.cost)!=-1){
            $scope.notice("成本价不能小于0!");
            return false;
        }
        if(dateList!=null&&dateList.length>0){
            for(var i=0;i<dateList.length;i++){
                var item=dateList[i];
                if(oldInfo!=null){
                    if(item.price==oldInfo.price
                        &&item.cost==oldInfo.cost
                        &&item.agio==oldInfo.agio
                        &&item.color==oldInfo.color
                        &&item.size==oldInfo.size){
                        continue;
                    }
                }
                if(item.price==info.price
                    &&item.cost==info.cost
                    &&item.agio==info.agio
                    &&item.color==info.color
                    &&item.size==info.size){
                    $scope.notice("该商品规格已存在,添加失败!");
                    return false;
                }
            }
        }
        return true;
    };

    $scope.editData = function(sta,entry){
        $scope.baseInfo={};
        $scope.oldBaseInfo={};
        $scope.oldBaseInfo = angular.copy(entry);
        $scope.baseInfo=angular.copy(entry);
        if(sta==0){
            $scope.isSize=false;
            $scope.modeState=true;
            $scope.baseInfo.size="";
        }else{
            $scope.isSize=true;
            $scope.modeState=false;
        }
        $('#addModal').modal('show');
    };

    $scope.delteData = function(){
        var selectList;
        var dateList;
        selectList = $scope.formatGridApi.selection.getSelectedRows();
        dateList=$scope.formatResult;

        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要删除的规格数据!");
            return false;
        }
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                var item=selectList[i];
                for(var j=0;j<dateList.length;j++){
                    var dateItem=dateList[j];
                    if(item.price==dateItem.price
                        &&item.cost==dateItem.cost
                        &&item.agio==dateItem.agio
                        &&item.color==dateItem.color
                        &&item.size==dateItem.size){
                        dateList.splice(j, 1);
                        $scope.deleteResult.push(dateItem);
                    }
                }
            }
        }
    };
});