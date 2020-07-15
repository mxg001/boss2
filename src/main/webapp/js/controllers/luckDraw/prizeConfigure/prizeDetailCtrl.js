/**
 * 奖项详情
 */
angular.module('inspinia',['angularFileUpload']).controller('prizeDetailCtrl',function($scope,$http,i18nService,$state,FileUploader,$stateParams){
    i18nService.setCurrentLang('zh-cn');  //设置语言为中文
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.funcCode=$stateParams.funcCode;

    $scope.awardTypeSelect = [{text:"全部",value:-1},{text:"鼓励金",value:1},{text:"超级积分",value:2},
        {text:"现金券",value:3},{text:"未中奖",value:4}];
    $scope.awardTypeStr=angular.toJson($scope.awardTypeSelect);

    $scope.addInfo={awardType:-1,money:""};
    $scope.conList=[];
    $scope.prizeDetailList=[];
    $scope.initialization=false;
    $scope.clearListsta=true;
    $scope.getprizeDetail=function(){
        $http.post("prizeConfigure/getPrize","id="+$stateParams.id,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.addInfo=data.prize;
                    if($scope.addInfo.awardType==1||$scope.addInfo.awardType==2){
                        $scope.prizeDetailList=$scope.addInfo.prizeDetailList==null?[]:$scope.addInfo.prizeDetailList;
                    }else if($scope.addInfo.awardType==3){
                        $scope.addInfo.money=$scope.addInfo.prizeDetailList[0].money;
                    }
                    $scope.clearListsta=false;
                    $scope.awardTypeChange();
                }
            });
    };
    $scope.getprizeDetail();

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
        },
        isRowSelectable: function(row){ // 选中行
            if($scope.prizeDetailList != null&&$scope.prizeDetailList.length>0){
                $scope.initialization=true;
                for(var i=0;i<$scope.prizeDetailList.length;i++){
                    if(row.entity.couponId==$scope.prizeDetailList[i].couponId){
                        row.grid.api.selection.selectRow(row.entity);
                    }
                }
                $scope.initialization=false;
            }
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
            '<input type="number" disabled ng-model="row.entity.itemCount">' +
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
            $scope.conList=[];
            $scope.prizeDetailList=[];
            $scope.conSta=false;
            $scope.moneySta=true;
            $scope.money="";
        }else if($scope.addInfo.awardType=="4"){//未中奖
            $scope.conList=[];
            $scope.prizeDetailList=[];
            $scope.conSta=false;
            $scope.moneySta=false;
        }
    };

    //券列表
    $scope.getCouponList=function(funcCode){
        $http.post("prizeConfigure/getCouponList","funcCode="+funcCode,
            {headers: {'Content-Type': 'application/x-www-form-urlencoded'}})
            .success(function(data){
                if(data.status){
                    $scope.conList=[];
                    if($scope.clearListsta){
                        $scope.prizeDetailList=[];
                    }else{
                        $scope.clearListsta=true;
                    }
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
        if( $scope.initialization){
            return;
        }
        if(list!=null&&list.length>0){
            for(var i=0;i<list.length;i++){
                $scope.selectAd(list[i])
            }
        }
    };
    $scope.selectAd=function(row){
        if( $scope.initialization){
            return;
        }
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
    $scope.submitting = false;
});