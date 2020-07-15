/**
 * oem详情
 */
angular.module('inspinia').controller('awardParamDetailCtrl',function($scope,$http,i18nService,$state,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文

    $scope.levelSelect = [{text:"全部",value:""},
        {text:"Lv.1",value:1},{text:"Lv.2",value:2},{text:"Lv.3",value:3},{text:"Lv.4",value:4},{text:"Lv.5",value:5},
        {text:"Lv.6",value:6},{text:"Lv.7",value:7},{text:"Lv.8",value:8},{text:"Lv.9",value:9},{text:"Lv.10",value:10},
        {text:"Lv.11",value:11},{text:"Lv.12",value:12},{text:"Lv.13",value:13},{text:"Lv.14",value:14},{text:"Lv.15",value:15},
        {text:"Lv.16",value:16},{text:"Lv.17",value:17},{text:"Lv.18",value:18},{text:"Lv.19",value:19},{text:"Lv.20",value:20}
    ];
    $scope.levelStr=angular.toJson($scope.levelSelect);

    $scope.level2Select = [{text:"全部",value:""},
        {text:"1",value:1},{text:"2",value:2},{text:"3",value:3},{text:"4",value:4},{text:"5",value:5},
        {text:"6",value:6},{text:"7",value:7},{text:"8",value:8},{text:"9",value:9},{text:"10",value:10},
        {text:"11",value:11},{text:"12",value:12},{text:"13",value:13},{text:"14",value:14},{text:"15",value:15},
        {text:"16",value:16},{text:"17",value:17},{text:"18",value:18},{text:"19",value:19},{text:"20",value:20}
    ];
    $scope.level2Str=angular.toJson($scope.level2Select);

    //清空
    $scope.clear=function(){
        $scope.addInfo={brandCode:""};
        $scope.ladderInfo={};
        $scope.tradeResult=[];
        $scope.rewardResult=[];
        $scope.vipResult=[];
        $scope.modeState=true;
    };
    $scope.clear();

    //服务列表
    $scope.serviceList=[];
    $http.post("service/getServiceInfo",
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            $scope.serviceList.push({value:"",text:"全部"});
            var list=data;
            if(list!=null&&list.length>0){
                for(var i=0; i<list.length; i++){
                    $scope.serviceList.push({value:list[i].serviceId,text:list[i].serviceId+"("+list[i].serviceName+")"});
                }
            }
        });

    $http.post("awardParam/getAwardParam","id="+$stateParams.id,
        {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
        .success(function(data){
            if(data.status){
                $scope.addInfo=data.oem;
                if(data.oem.tradeList!=null){
                    $scope.tradeResult = data.oem.tradeList;
                }
                if(data.oem.tradeList!=null){
                    $scope.rewardResult = data.oem.crownList;
                }
                if(data.oem.vipList!=null){
                    $scope.vipResult = data.oem.vipList;
                }
            }
        });

    $scope.tradeGrid={                           //配置表格
        data: 'tradeResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'ladderGrade',displayName:'分润等级',width:100,cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'minNum',displayName:'阶梯开始金额(万元)',width:180},
            { field: 'maxNum',displayName:'阶梯结束金额(万元)',width:180},
            { field: 'val',displayName:'收益比例(万分)',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.tradeGridApi = gridApi;
        }
    };

    $scope.rewardGrid={                           //配置表格
        data: 'rewardResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'ladderGrade',displayName:'序号',width:100,cellFilter:"formatDropping:" +  $scope.level2Str},
            { field: 'minNum',displayName:'阶梯开始金额(亿元)',width:180},
            { field: 'maxNum',displayName:'阶梯结束金额(亿元)',width:180},
            { field: 'val',displayName:'收益比例(万分)',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.rewardGridApi = gridApi;
        }
    };

    $scope.vipGrid={                           //配置表格
        data: 'vipResult',
        useExternalPagination: true,		    //开启拓展名
        enableHorizontalScrollbar: true,        //横向滚动条
        enableVerticalScrollbar : true,  		//纵向滚动条
        columnDefs:[                           //表格数据
            { field: 'ladderGrade',displayName:'分润等级',width:100,cellFilter:"formatDropping:" +  $scope.levelStr},
            { field: 'minNum',displayName:'阶梯开始金额(万元)',width:180},
            { field: 'maxNum',displayName:'阶梯结束金额(万元)',width:180},
            { field: 'val',displayName:'收益比例(万分)',width:150}
        ],
        onRegisterApi: function(gridApi) {
            $scope.vipGridApi = gridApi;
        }
    };

    $scope.addModal = function(sta){
        $scope.ladderInfo={ladderGrade:""};
        $scope.initModel(sta);
        $scope.modeState=true;
        $('#addModal').modal('show');
    };
    $scope.initModel=function (sta) {
        if(sta==1){
            $scope.ladderInfo.type=1;
            $scope.unit="单位:万元";
            $scope.ladderGradeStr="分润等级";
            $scope.levelSelectStr=$scope.levelSelect;
            $scope.title="标准分润收益阶梯配置";
        }else if(sta==2){
            $scope.ladderInfo.type=2;
            $scope.unit="单位:亿元";
            $scope.ladderGradeStr="序号";
            $scope.levelSelectStr=$scope.level2Select;
            $scope.title="荣耀奖金收益阶梯配置";
        }else if(sta==3){
            $scope.ladderInfo.type=3;
            $scope.unit="单位:万元";
            $scope.ladderGradeStr="分润等级";
            $scope.levelSelectStr=$scope.levelSelect;
            $scope.title="VIP分润收益阶梯配置";
        }
    };
    $scope.cancel = function(){
        $('#addModal').modal('hide');
    };
    $scope.saveModal = function(mode){
       if($scope.ladderInfo!=null){
           if($scope.ladderInfo.type==1){
               dateList=$scope.tradeResult;
           }else if($scope.ladderInfo.type==2){
               dateList=$scope.rewardResult;
           }else if($scope.ladderInfo.type==3){
               dateList=$scope.vipResult;
           }
           if(mode=="add"){
               if($scope.checkDate(dateList,$scope.ladderInfo,null)){
                   dateList.push({
                       ladderGrade:$scope.ladderInfo.ladderGrade,
                       minNum:$scope.ladderInfo.minNum,
                       maxNum:$scope.ladderInfo.maxNum,
                       val:$scope.ladderInfo.val
                   });
                   $scope.cancel();
               }

           }else if(mode=="edit"){
               var dateList;
               var item=$scope.oldLadderInfo;
               if($scope.checkDate(dateList,$scope.ladderInfo,item)) {
                   for (var j = 0; j < dateList.length; j++) {
                       var dateItem = dateList[j];
                       if (item.ladderGrade == dateItem.ladderGrade
                           && item.minNum == dateItem.minNum
                           && item.maxNum == dateItem.maxNum
                           && item.val == dateItem.val) {

                           dateItem.ladderGrade = $scope.ladderInfo.ladderGrade;
                           dateItem.minNum = $scope.ladderInfo.minNum;
                           dateItem.maxNum = $scope.ladderInfo.maxNum;
                           dateItem.val = $scope.ladderInfo.val;
                       }
                   }
                   $scope.cancel();
               }
           }

       }
    };
    $scope.checkDate = function(dateList,info,oldInfo){
        if(Number(info.maxNum)<0&&Number(info.maxNum)!=-1){
            $scope.notice("阶梯结束金额不能小于0!");
            return false;
        }
        if(Number(info.minNum)>Number(info.maxNum)&&Number(info.maxNum)!=-1){
            $scope.notice("阶梯开始金额不能大于阶梯结束金额!");
            return false;
        }
        if(dateList!=null&&dateList.length>0){
            for(var i=0;i<dateList.length;i++){
                var item=dateList[i];
                if(oldInfo!=null){
                    if(item.ladderGrade==oldInfo.ladderGrade
                        &&item.minNum==oldInfo.minNum
                        &&item.maxNum==oldInfo.maxNum
                        &&item.val==oldInfo.val){
                        continue;
                    }
                }
                if(item.ladderGrade==info.ladderGrade){
                    if($scope.ladderInfo.type==1||$scope.ladderInfo.type==3){
                        $scope.notice("该分润等级已存在,添加失败!");
                    }else{
                        $scope.notice("该序号已存在,添加失败!");
                    }
                    return false;
                }
                if(Number(item.maxNum)!=-1){
                    if((Number(item.minNum)<Number(info.minNum) &&Number(info.minNum)<Number(item.maxNum))
                        ||(Number(item.minNum)<Number(info.maxNum) &&Number(info.maxNum)<Number(item.maxNum))
                    ){
                        $scope.notice("阶梯区间已存在,添加失败!");
                        return false;
                    }
                }else{
                    if(Number(item.minNum)<Number(info.minNum)){
                        $scope.notice("阶梯区间已存在,添加失败!");
                        return false;
                    }
                }
                //反包含校验
                if(Number(info.maxNum)!=-1){
                    if((Number(info.minNum)<Number(item.minNum) &&Number(item.minNum)<Number(info.maxNum))
                        ||(Number(info.minNum)<Number(item.maxNum) &&Number(item.maxNum)<Number(info.maxNum))
                    ){
                        $scope.notice("阶梯区间已存在,添加失败!");
                        return false;
                    }
                }else{
                    if(Number(info.minNum)<Number(item.minNum)){
                        $scope.notice("阶梯区间已存在,添加失败!");
                        return false;
                    }
                }
            }
        }
        return true;
    };

    $scope.editData = function(sta,entry){
        $scope.ladderInfo={};
        $scope.oldLadderInfo={};
        $scope.oldLadderInfo = angular.copy(entry);
        $scope.ladderInfo=angular.copy(entry);
        $scope.initModel(sta);
        $scope.modeState=false;
        $('#addModal').modal('show');
    };

    $scope.delteData = function(sta){
        var selectList;
        var dateList;
        if(sta==1){
            selectList = $scope.tradeGridApi.selection.getSelectedRows();
            dateList=$scope.tradeResult;
        }else if(sta==2){
            selectList = $scope.rewardGridApi.selection.getSelectedRows();
            dateList=$scope.rewardResult;
        }else if(sta==3){
            selectList = $scope.vipGridApi.selection.getSelectedRows();
            dateList=$scope.vipResult;
        }
        if(selectList==null||selectList.length==0){
            $scope.notice("请选中要删除的阶梯数据!");
            return false;
        }
        if(selectList!=null&&selectList.length>0){
            for(var i=0;i<selectList.length;i++){
                var item=selectList[i];
               for(var j=0;j<dateList.length;j++){
                   var dateItem=dateList[j];
                   if(item.ladderGrade==dateItem.ladderGrade
                       &&item.minNum==dateItem.minNum
                       &&item.maxNum==dateItem.maxNum
                       &&item.val==dateItem.val){
                       dateList.splice(j, 1);
                   }
               }
            }
        }
    };

    $scope.submitting = false;
});