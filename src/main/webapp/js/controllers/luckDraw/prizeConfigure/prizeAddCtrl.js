/**
 * 奖项新增
 */
angular.module('inspinia',['angularFileUpload']).controller('prizeAddCtrl',function($scope,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.funcCode=$stateParams.funcCode;

    $scope.awardTypeSelect = [{text:"全部",value:-1},{text:"鼓励金",value:1},{text:"超级积分",value:2},
        {text:"现金券",value:3},{text:"未中奖",value:4}];
    $scope.awardTypeStr=angular.toJson($scope.awardTypeSelect);

    $scope.addInfo={awardType:-1,money:""};
    $scope.conList=[];
    $scope.prizeDetailList=[];

    /* 提供下拉选择*/
    $scope.adGrid={                           //配置表格
        data: 'conList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'couponId',displayName:'券ID',width:120},
            { field: 'couponName',displayName:'券名称',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.conGridApi = gridApi;
            $scope.conGridApi.selection.on.rowSelectionChanged($scope,function(row,event){
                //行选中事件
                $scope.selectAd(row);
            });
            $scope.conGridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
                //全选事件
                $scope.selectAdAll(row);
            });
        }
    };
    /* 组内广告*/
    $scope.groupAdGrid={                           //配置表格
        data: 'prizeDetailList',
        paginationPageSize:10,                  //分页数量
        paginationPageSizes: [10,20,50,100],	//切换每页记录数
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'couponId',displayName:'券ID',width:120},
            { field: 'couponName',displayName:'券名称',width:150},
            { field: 'itemCount',displayName:'数量',width:120,cellTemplate:
                '<div style="margin-top:7px;">' +
                    '<input type="number" ng-model="row.entity.itemCount">' +
                '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.groupAdGridApi = gridApi;
        }
    };
    $scope.conSta=false;
    $scope.moneySta=false;
    $scope.awardTypeChange=function(){
        if($scope.addInfo.awardType=="1"){//鼓励金
            $scope.getCouponList("6");//鼓励金的券
            $scope.conSta=true;
            $scope.moneySta=false;
        }else if($scope.addInfo.awardType=="2"){//超级积分
            $scope.getCouponList("3");//充值返的券
            $scope.conSta=true;
            $scope.moneySta=false;
        }else if($scope.addInfo.awardType=="3"){//现金券
            $scope.conSta=false;
            $scope.moneySta=true;
            $scope.addInfo.money="";
        }else if($scope.addInfo.awardType=="4"){//未中奖
            $scope.conList=[];
            $scope.conSta=false;
            $scope.moneySta=false;
        }
    };
    $scope.awardTypeChange();
    //券列表
    $scope.getCouponList=function(funcCode){
        $http.post("prizeConfigure/getCouponList","funcCode="+funcCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.conList=[];
                    $scope.prizeDetailList=[];
                    var list=data.list;
                    if(list!=null&&list.length>0){
                        for(var i=0; i<list.length; i++){
                            $scope.conList.push({couponId:list[i].id,couponName:list[i].couponName});
                        }
                    }
                }
            });
    };

    $scope.selectAdAll=function(list){
        if(list!=null&&list.length>0){
            for(var i=0;i<list.length;i++){
                $scope.selectAd(list[i])
            }
        }
    };
    $scope.selectAd=function(row){
        if(row.isSelected){//勾选
            var entity=row.entity;
            if($scope.prizeDetailList.length>0){
                var sta=0;
                for(var j=0;j<$scope.prizeDetailList.length;j++){
                    if(entity.couponId==$scope.prizeDetailList[j].couponId){
                        sta=1;
                    }
                }
                if(sta==0){
                    $scope.prizeDetailList.push({couponId:entity.couponId,couponName:entity.couponName});
                }
            }else{
                $scope.prizeDetailList.push({couponId:entity.couponId,couponName:entity.couponName});
            }
        }else{
            //不勾选
            var entity=row.entity;
            if($scope.prizeDetailList.length>0){
                var sta=-1;
                for(var i=0;i<$scope.prizeDetailList.length;i++){
                    if(entity.couponId==$scope.prizeDetailList[i].couponId){
                        sta=i;
                        break;
                    }
                }
                if(sta!=-1){
                    $scope.prizeDetailList.splice(sta,1);
                }
            }
        }
    };



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

    uploaderImg.onSuccessItem = function(fileItem,response, status, headers) {// 上传成功后的回调函数，在里面执行提交
        if (response.url != null) { // 回调参数url
            $scope.saveInfo(response.url);
        }
    };

    $scope.submitting = false;
    //新增
    $scope.commitInfo = function(){
        if($scope.submitting){
            return;
        }
        if(uploaderImg.queue.length<1){
            $scope.notice("图片不能为空!");
            return;
        }
        if(Number($scope.addInfo.prob)<0||Number($scope.addInfo.prob)>=100){
            $scope.notice("中奖概率只能在大于等于零小于100之间!");
            return;
        }
        if($scope.addInfo.awardType=="1"||$scope.addInfo.awardType=="2"){
            if($scope.prizeDetailList!=null&&$scope.prizeDetailList.length>0) {
                for (var i = 0; i < $scope.prizeDetailList.length; i++) {
                    var item=$scope.prizeDetailList[i];
                    if(item.itemCount==null||item.itemCount==""){
                        $scope.notice("券["+item.couponId+"]:"+item.couponName+"数量未配置!");
                        return;
                    }
                }
                $scope.addInfo.prizeDetailList=$scope.prizeDetailList;
            }else{
                $scope.notice("奖品不能为空!");
                return;
            }
        }else{
            $scope.addInfo.prizeDetailList=null;
        }
        $scope.submitting = true;
        uploaderImg.uploadAll();// 上传消息图片
    };
    $scope.saveInfo=function(url){
        $scope.infoSub = angular.copy($scope.addInfo);
        $scope.infoSub.awardPic=url;
        $scope.infoSub.funcCode=$scope.funcCode;
        $http.post("prizeConfigure/addPrize",
            "info="+angular.toJson($scope.infoSub),
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('func.prizeConfigure',{funcCode:$scope.infoSub.funcCode},{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
    }
});