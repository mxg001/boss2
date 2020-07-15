/**
 * 排行榜配置新增
 */
angular.module('inspinia',['angularFileUpload']).controller('rankConfigAddCtrl',function($scope,$http,i18nService,$state,FileUploader){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.rankMaxSelect = [{text:"请选择...",value:null},
        {text:"1",value:1},{text:"2",value:2},{text:"3",value:3},{text:"4",value:4},{text:"5",value:5},
        {text:"6",value:6},{text:"7",value:7},{text:"8",value:8},{text:"9",value:9},{text:"10",value:10},
        {text:"11",value:11},{text:"12",value:12},{text:"13",value:13},{text:"14",value:14},{text:"15",value:15},
        {text:"16",value:16},{text:"17",value:17},{text:"18",value:18},{text:"19",value:19},{text:"20",value:20},
        {text:"21",value:21},{text:"22",value:22},{text:"23",value:23},{text:"24",value:24},{text:"25",value:25},
        {text:"26",value:26},{text:"27",value:27},{text:"28",value:28},{text:"29",value:29},{text:"30",value:30},
    ];
    $scope.rankMaxStr=angular.toJson($scope.rankMaxSelect);
    $scope.addInfo={rankMax:null,oneJoin:1,oneShow:1,twoJoin:1,twoShow:1};
    $scope.rankAmountResult=[];

    $scope.rankAmountGrid={                           //配置表格
        data: 'rankAmountResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'name',displayName:'名次',width:140},
            { field: 'rankAmount',displayName:'奖金(元)',width:180,cellTemplate:
            '<div style="margin-top:7px;margin-left: 20px;">' +
            '<input type="number" ng-model="row.entity.rankAmount">' +
            '</div>'
            },
            { field: 'reachNum',displayName:'达标激活商户数',width:180,cellTemplate:
            '<div style="margin-top:7px;margin-left: 20px;">' +
            '<input type="number" ng-model="row.entity.reachNum">' +
            '</div>'
            },
            { field: 'reachAmount',displayName:'未达标奖金(元)',width:220,cellTemplate:
            '<div style="margin-top:7px;margin-left: 20px;">' +
            '<input type="number" ng-model="row.entity.reachAmount">' +
            '</div>'
            }
        ],
        onRegisterApi: function(gridApi) {
            $scope.rankAmountGridApi = gridApi;
        }
    };
    $scope.rankMaxChange=function () {
        if($scope.addInfo.rankMax!=null&&Number($scope.addInfo.rankMax)>0){
            var max=Number($scope.addInfo.rankMax);
            if($scope.rankAmountResult!=null&&$scope.rankAmountResult.length>0){
                var size=$scope.rankAmountResult.length;
                if(size<max){//新增
                    for(var i=size+1;i<=max;i++){
                        var name="第"+i+"名奖金";
                        $scope.rankAmountResult.push({name:name,rank:i,rankAmount:null,reachNum:null,reachAmount:null});
                    }
                }else if(size>max){//去除
                    for(var i=size-1;i>=max;i--){
                        $scope.rankAmountResult.splice(i,1);
                    }
                }
            }else{
                for(var i=0;i<max;i++){
                    var name="第"+(i+1)+"名奖金";
                    $scope.rankAmountResult.push({name:name,rank:(i+1),rankAmount:null,reachNum:null,reachAmount:null});
                }
            }
        }else{
            $scope.rankAmountResult=[];
        }
    };

    $scope.oneShowState=false;
    $scope.twoShowState=false;
    $scope.initState=false;
    //机构显示事件
    $scope.oneJoinState=function () {
        if($scope.addInfo.oneJoin==1){
            $scope.oneShowState=false;
        }else{
            $scope.oneShowState=true;
        }
        if(!$scope.initState){
            $scope.addInfo.oneShow=1;
        }
    };


    //大盟主显示事件
    $scope.twoJoinState=function () {
        if($scope.addInfo.twoJoin==1){
            $scope.twoShowState=false;
        }else{
            $scope.twoShowState=true;
        }
        if(!$scope.initState){
            $scope.addInfo.twoShow=1;
        }
    };
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
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.brandCodeList != null){
                for(var i=0;i<$scope.brandCodeList.length;i++){
                    if(row.entity.brandCode==$scope.brandCodeList[i]){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
            }
        }
    };

    $scope.getRankConfig=function () {
        $http.post("rankConfig/getRankConfig",
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    if(data.info!=null){
                        $scope.addInfo=data.info;
                        $scope.addInfo.startTime=$scope.addInfo.startTime==null?null:moment($scope.addInfo.startTime).format('YYYY-MM-DD HH:mm:ss');
                        $scope.addInfo.endTime=$scope.addInfo.endTime==null?null:moment($scope.addInfo.endTime).format('YYYY-MM-DD HH:mm:ss');
                        $scope.rankAmountResult=$scope.addInfo.rankRewardList;
                        if($scope.addInfo.rankRewardList!=null&&$scope.addInfo.rankRewardList.length>0){
                            for(var i=0;i<$scope.rankAmountResult.length;i++){
                                var item=$scope.rankAmountResult[i];
                                item.name="第"+(i+1)+"名奖金";
                            }
                        }
                        $scope.brandCodeList=$scope.addInfo.brandCodeList;
                        $scope.imgUrl=$scope.addInfo.rankImg;
                    }
                    $scope.getOemList();
                    $scope.initState=true;
                    $scope.oneJoinState();
                    $scope.twoJoinState();
                    $scope.initState=false;
                }
            });
    };
    $scope.getRankConfig();

    //上传图片,定义控制器路径
    var uploaderImg = $scope.uploaderImg = new FileUploader({
        url: 'upload/fileUpload.do',
        queueLimit: 1,   //文件个数
        removeAfterUpload: false,  //上传后删除文件
        autoUpload:false,     //文件加入队列之后自动上传，默认是false
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
            $scope.commitInfoInterface(response.url);
        }
    };

    $scope.submitting = false;
    //新增
    $scope.commitInfo = function(){
        if($scope.addInfo.rankMax!=null&&Number($scope.addInfo.rankMax)>0){
            if($scope.rankAmountResult!=null&&$scope.rankAmountResult.length>0){
                for(var i=0;i<$scope.rankAmountResult.length;i++){
                    var item=$scope.rankAmountResult[i];
                    if(item.rankAmount==null||item.rankAmount==""){
                        $scope.notice(item.name+"的奖金不能为空!");
                        return;
                    }
                    if(Number(item.rankAmount)<=0){
                        $scope.notice(item.name+"的奖金需大于0!");
                        return;
                    }
                    if(item.reachNum!=null&&item.reachNum!=""){
                        if(Number(item.reachNum)<=0){
                            $scope.notice(item.name+"的达标激活商户数需大于0!");
                            return;
                        }else{
                            if(item.reachAmount==null||item.reachAmount==""){
                                $scope.notice(item.name+"的未达标奖金不能为空!");
                                return;
                            }
                            if(Number(item.reachAmount)<=0){
                                $scope.notice(item.name+"的未达标奖金需大于0!");
                                return;
                            }
                        }
                    }
                }
            }else{
                $scope.notice("各个名次奖金不能为空!");
                return;
            }
        }
        $scope.addInfo.RankRewardList=$scope.rankAmountResult;

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
        $scope.addInfo.brandCodeSet=ids.substring(0,ids.length-1);

        if($scope.addInfo.startTime==null||$scope.addInfo.startTime==""
            ||$scope.addInfo.endTime==null||$scope.addInfo.endTime==""){
            $scope.notice("上/下线时间不能为空!");
            return;
        }

        if($scope.addInfo.id==null||$scope.addInfo.id==""||Number($scope.addInfo.id)<1){
            if(uploaderImg.queue.length<1){
                $scope.notice("封面图片不能为空!");
                return;
            }
        }
        if($scope.submitting){
            return;
        }
        $scope.submitting = true;
        if(uploaderImg.queue.length<1){
            $scope.commitInfoInterface(null);
        }else{
            uploaderImg.uploadAll();// 上传消息图片
        }
    };
    $scope.commitInfoInterface =function (url) {
        $scope.addInfo.rankImg=url;
        //转换特殊数据
        $scope.subAddinfo=angular.copy($scope.addInfo);
        $scope.subAddinfo.content=null;
        if($scope.subAddinfo.rankUrl!=null){
            $scope.subAddinfo.rankUrl=encodeURIComponent($scope.subAddinfo.rankUrl);
        }
        var data={
            info:angular.toJson($scope.subAddinfo),
            content:$scope.addInfo.rankRule
        };
        var postCfg = {
            headers: { 'Content-Type':'application/x-www-form-urlencoded' },
            transformRequest: function (data) {
                return $.param(data);
            }
        };
        $http.post("rankConfig/rankConfigAdd",data,postCfg)
            .success(function(data){
                $scope.submitting = false;
                if(data.status){
                    $scope.notice(data.msg);
                    $state.transitionTo('allAgent.rankConfigAdd',null,{reload:true});
                }else{
                    $scope.notice(data.msg);
                }
            })
            .error(function(data){
                $scope.submitting = false;
                $scope.notice(data.msg);
            });
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