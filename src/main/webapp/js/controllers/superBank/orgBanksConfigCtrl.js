/**
 * 信用卡银行配置
 */
angular.module('inspinia',['uiSwitch']).controller('orgBanksConfigCtrl',function($scope,$http,i18nService,$stateParams,$document,SweetAlert){

    i18nService.setCurrentLang('zh-cn');
    $scope.paginationOptions=angular.copy($scope.paginationOptions);
    $scope.gongzhonghaoSumInfo = [];
    $scope.appSumInfo = [];
    $scope.gridApi={};
    $scope.gridApi2={};
    $scope.banks = [];
    $scope.statuses = [{text:"全部",value:""},{text:"是",value:"on"},{text:"否",value:"off"}];
    $scope.isSubmit = false;
    $scope.resetForm1 = function () {
        $scope.baseInfo1 = {type:'1',status:'',"orgId":$stateParams.orgId,"sourceId":""};//查询条件的form
    };
    $scope.resetForm1();
    $scope.resetForm2 = function () {
        $scope.baseInfo2 = {type:'1',status:'',"orgId":$stateParams.orgId,"sourceId":""};//查询条件的form
    };
    $scope.resetForm2();
    $scope.gongzhonghaoColumnDefs = [
                         {field: 'code',width: 120, displayName: '银行编码',pinnable: false,sortable: false},
                         {field: 'sourceName',width: 150, displayName: '银行全称',pinnable: false,sortable: false},
                         {field: 'sourceNickName',width: 150, displayName: '银行别称',pinnable: false,sortable: false},
                         {field: 'showOrder',width: 100, displayName: '排序',pinnable: false,sortable: false,cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope"><input style="text-align:center;width:80px;height:28px;border-color:#3baaff" ng-model="row.entity.showOrder"/></div>'},
                         {field: 'isRecommend',width: 100, displayName: '是否推荐',pinnable: false,sortable: false,cellTemplate:
                         	'<input ng-click="grid.appScope.changeBoxVal(row)" type="checkbox" ng-bind="row.entity.isRecommend" ng-checked="row.entity.isRecommend==1" />'
                         },
                         /*{field: 'specialImage',displayName: '特别推荐图片',width: 150,pinnable: false,sortable: false,
                             cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.specialImageUrl" ng-src="{{row.entity.specialImageUrl}}" />'},*/
                         {field: 'statusInt',displayName: '是否上架',width:150,pinnable: false,sortable: false,cellTemplate:
                             '<span ng-show="grid.appScope.hasPermit(\'superBank.orgBanksConfig\')">' +
                              '<switch class="switch switch-s" ng-model="row.entity.statusInt" ng-change="grid.appScope.switchStatus(row)" />' +
                             '</span>'
                             +'<span ng-show="!grid.appScope.hasPermit(\'superBank.orgBanksConfig\')">' +
                                 ' <span ng-show="row.entity.statusInt==1">是</span>' +
                              '<span ng-show="row.entity.statusInt==0">否</span>' +
                             '</span>'
                         },
                         {field: 'createDate',width: 225, displayName: '产品上线时间',pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
                         {field: 'openUrl',width: 800, displayName: '外放链接',pinnable: false,sortable: false}
                     ];
    
    $scope.appColumnDefs = [
	                      {field: 'code',width: 120, displayName: '银行编码',pinnable: false,sortable: false},
	                      {field: 'sourceName',width: 150, displayName: '银行全称',pinnable: false,sortable: false},
	                      {field: 'sourceNickName',width: 150, displayName: '银行别称',pinnable: false,sortable: false},
	                      {field: 'showOrder',width: 100, displayName: '排序',pinnable: false,sortable: false,cellTemplate:'<div class="ui-grid-cell-contents ng-binding ng-scope"><input style="text-align:center;width:80px;height:28px;border-color:#3baaff" ng-model="row.entity.showOrder"/></div>'},
	                      {field: 'isRecommend',width: 100, displayName: '是否推荐',pinnable: false,sortable: false,cellTemplate:
	                      	'<input ng-click="grid.appScope.changeBoxVal(row)" type="checkbox" ng-bind="row.entity.isRecommend" ng-checked="row.entity.isRecommend==1" />'
	                      },
	                      /*{field: 'specialImage',displayName: '特别推荐图片',width: 150,pinnable: false,sortable: false,
	                             cellTemplate:'<img style="width: 140px; height: 36px;" ng-show="row.entity.specialImageUrl" ng-src="{{row.entity.specialImageUrl}}" />'},*/
	                      {field: 'statusInt',displayName: '是否上架',width:150,pinnable: false,sortable: false,cellTemplate:
	                          '<span ng-show="grid.appScope.hasPermit(\'superBank.orgBanksConfig\')">' +
	                           '<switch class="switch switch-s" ng-model="row.entity.statusInt" ng-change="grid.appScope.switchStatus(row)" />' +
	                          '</span>'
	                          +'<span ng-show="!grid.appScope.hasPermit(\'superBank.orgBanksConfig\')">' +
	                              ' <span ng-show="row.entity.statusInt==1">是</span>' +
	                           '<span ng-show="row.entity.statusInt==0">否</span>' +
	                          '</span>'
	                      },
	                      {field: 'createDate',width: 225, displayName: '产品上线时间',pinnable: false,sortable: false,cellFilter: 'date:"yyyy-MM-dd HH:mm:ss"'},
	                      {field: 'openUrl',width: 800, displayName: '外放链接',pinnable: false,sortable: false}
                              ];
    
       $scope.gongzhonghaoGrid = {
            paginationPageSize:10,                  //分页数量
            paginationPageSizes: [10,20,50,100],	//切换每页记录数
            useExternalPagination: true,		  	//开启拓展名
            enableHorizontalScrollbar: true,        //横向滚动条
            enableVerticalScrollbar : true,  		//纵向滚动条
            columnDefs: $scope.gongzhonghaoColumnDefs,
            onRegisterApi: function(gridApi) {
                $scope.gridApi = gridApi;
                $scope.gridApi.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
                    $scope.paginationOptions.pageNo = newPage;
                    $scope.paginationOptions.pageSize = pageSize;
                    $scope.gongzhonghaoQueryOrgSourcConfList();
                });
              //行选中事件
    			$scope.gridApi.selection.on.rowSelectionChanged($scope,function(row,event){
    				//console.log(gridApi.selection.getSelectedRows());
    				if(row.isSelected){
    					$scope.baseInfo1.pageAll=false;
    					$scope.baseInfo1.countAll=false;
    				}
    			});
    			//全选事件
    			$scope.gridApi.selection.on.rowSelectionChangedBatch($scope,function(row,event){
    				if(row){
    					if(row[0].isSelected){
    					
    						$scope.baseInfo1.pageAll=false;
    						$scope.baseInfo1.countAll=false;
    					}
    				}
    			});
            }
        };

	    $scope.appGrid = {
	        paginationPageSize:10,                  //分页数量
	        paginationPageSizes: [10,20,50,100],	//切换每页记录数
	        useExternalPagination: true,		  	//开启拓展名
	        enableHorizontalScrollbar: true,        //横向滚动条
	        enableVerticalScrollbar : true,  		//纵向滚动条
	        columnDefs: $scope.appColumnDefs,
	        onRegisterApi: function(gridApi) {
	            $scope.gridApi2 = gridApi;
	            $scope.gridApi2.pagination.on.paginationChanged($scope, function (newPage, pageSize) {
	                $scope.paginationOptions.pageNo = newPage;
	                $scope.paginationOptions.pageSize = pageSize;
	                $scope.appQueryOrgSourcConfList();
	            });
	          //行选中事件
				$scope.gridApi2.selection.on.rowSelectionChanged($scope,function(row,event){
					if(row.isSelected){
						$scope.baseInfo2.pageAll=false;
						$scope.baseInfo2.countAll=false;
					}
				});
				//全选事件
				$scope.gridApi2.selection.on.rowSelectionChangedBatch($scope,function(row,event){
					if(row){
						if(row[0].isSelected){
							$scope.baseInfo2.pageAll=false;
							$scope.baseInfo2.countAll=false;
						}
					}
				});
	        }
	    };
        
      //批量上下架
        $scope.gongzhonghaoBatchUpd = function(status){
        	$scope.rows =$scope.gridApi.selection.getSelectedRows();
        	var ids = "";
        	angular.forEach($scope.rows, function(data,index,array){
        		console.log(data);
        		ids = ids + data.id + ",";
        	});
        	if(ids.length>1){
        		$http({
                    url: "orgSourceConfig/batchOpenOrClose?ids="+ids+"&status="+status,
                    method: "GET",
                }).success(function(result){
                	$scope.notice(result.msg);
                	$scope.gongzhonghaoQueryOrgSourcConfList();
                });
        	}else{
        		$scope.notice('请选择要批量处理的数据！');
        	}
        };
        
       //批量上下架
        $scope.appBatchUpd = function(status){
        	$scope.rows =$scope.gridApi2.selection.getSelectedRows();
        	var ids = "";
        	angular.forEach($scope.rows, function(data,index,array){
        		ids = ids + data.id + ",";
        	});
        	if(ids.length>1){
        		$http({
                    url: "orgSourceConfig/batchOpenOrClose?ids="+ids+"&status="+status,
                    method: "GET",
                }).success(function(result){
                	$scope.notice(result.msg);
                	$scope.appQueryOrgSourcConfList();
                });
        	}else{
        		$scope.notice('请选择要批量处理的数据！');
        	}
        };
        
        $scope.gongzhonghaoQueryOrgSourcConfList = function(){
        	$scope.loadImg = true;
        	$scope.baseInfo1.application = "1";
            $http({
                url: "orgSourceConfig/queryOrgSourcConfList?&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                method: "POST",
                data: $scope.baseInfo1
            }).success(function(msg){
                $scope.submitting = false;
                $scope.loadImg = false;
                if (!msg.status){
                    $scope.notice(msg.msg);
                    return;
                }
                $scope.gongzhonghaoGrid.data = msg.data.page.result;
                $scope.gongzhonghaoGrid.totalItems = msg.data.page.totalCount;
                $scope.gongzhonghaoSumInfo = msg.data.sumInfo;
            }).error(function (msg) {
                $scope.submitting = false;
                $scope.loadImg = false;
                $scope.notice('服务器异常,请稍后再试.');
            });
        };

        $scope.appQueryOrgSourcConfList = function(){
        	$scope.loadImg = true;
        	$scope.baseInfo2.application = "2";
            $http({
                url: "orgSourceConfig/queryOrgSourcConfList?&pageNo="+$scope.paginationOptions.pageNo+"&pageSize="+$scope.paginationOptions.pageSize,
                method: "POST",
                data: $scope.baseInfo2
            }).success(function(msg){
                $scope.submitting = false;
                $scope.loadImg = false;
                if (!msg.status){
                    $scope.notice(msg.msg);
                    return;
                }
                $scope.appGrid.data = msg.data.page.result;
                $scope.appGrid.totalItems = msg.data.page.totalCount;
                $scope.appSumInfo = msg.data.sumInfo;
            }).error(function (msg) {
                $scope.submitting = false;
                $scope.loadImg = false;
                $scope.notice('服务器异常,请稍后再试.');
            });
        };
        
        //上下架
        $scope.switchStatus = function(row){
        	 if(row.entity.statusInt){
                 $scope.serviceText = "确定开启";
             } else {
                 $scope.serviceText = "确定关闭";
             }
             SweetAlert.swal({
                     title: "",
                     text: $scope.serviceText,
                     type: "warning",
                     showCancelButton: true,
                     confirmButtonColor: "#DD6B55",
                     confirmButtonText: "确定",
                     cancelButtonText: "取消",
                     closeOnConfirm: true,
                     closeOnCancel: true },
                 function (isConfirm) {
                     if (isConfirm) {
                    	 var tmpStatus = '';
                         if(row.entity.statusInt==true){
                        	 row.entity.statusInt = 1;
                        	 tmpStatus = "on";
                         } else if(row.entity.statusInt==false){
                        	 row.entity.statusInt = 0;
                        	 tmpStatus = 'off';
                         }
                         var data={"status":tmpStatus,"id":row.entity.id};
                         $http.post("orgSourceConfig/openOrClose",angular.toJson(data))
                             .success(function(data){
                            	 $scope.notice(data.msg);
                                 $scope.gongzhonghaoQueryOrgSourcConfList();
                                 $scope.appQueryOrgSourcConfList();
                             })
                             .error(function(data){
                                 row.entity.statusInt = !row.entity.statusInt;
                                 $scope.notice("服务器异常");
                             });
                     } else {
                         row.entity.statusInt = !row.entity.statusInt;
                     }
              });
    	};
        //银行
    	$scope.getAllBanks = function(){
        	$http({
                url: "orgSourceConfig/banksList",
                method: "POST"
            }).success(function(result){
            	 $scope.banks = result.data;
            	 $scope.banks.unshift({id:"",bankNickName:"全部"});
            });
        };
        
        //保存更改
        $scope.gongzhonghaoSaveUpd = function(){
        	$scope.rows = $scope.gridApi.grid.rows;
        	//$scope.rows = $scope.gridApi.selection.getSelectedGridRows();//$scope.gridApi.grid.rows;
        	var reg = /^\d{1,}$/;
        	$scope.isTrue = true;
        	$scope.data = "[";
        	angular.forEach($scope.rows, function(data,index,array){
        		$scope.rowIndex = index + 1;
        		$scope.showOrder = data.entity.showOrder;  // 排序值
        		$scope.isRecommend = data.entity.isRecommend;//复选框是否选中标示
        		$scope.rowId = data.entity.id;
        		//console.log(data.entity.specialImageUrl);
        		if(!reg.test($scope.showOrder)){
        			$scope.isTrue = false;
        		}
        		
        		$scope.data = $scope.data + "{" + "showOrder" +":"+$scope.showOrder + ","+"isRecommend" + ":" + $scope.isRecommend + "," + "id" + ":" + $scope.rowId + "}" + ",";
        	});
        	$scope.data = $scope.data + "]";
        	
        	if(!$scope.isTrue){
        		$scope.notice("请输入正整数！");
        		return ;
        	}
        	
        	if($scope.data.length >2){
        		$scope.isSubmit = true ;
        		var urlEncodeData = encodeURI($scope.data);
        		
        		$http({
                    url: "orgSourceConfig/saveOrgSourceConfig?data="+urlEncodeData,
                    method: "GET",
                }).success(function(result){
                	$scope.notice(result.msg);
                	$scope.isSubmit = false ;
                	$scope.gongzhonghaoQueryOrgSourcConfList();
                });
        	}else{
        		$scope.notice("没有数据！");
        		return ;
        	}
        }
        
      //保存更改
        $scope.appSaveUpd = function(){
        	$scope.rows = $scope.gridApi2.grid.rows;
        	//$scope.rows = $scope.gridApi2.selection.getSelectedGridRows();//$scope.gridApi.grid.rows;
        	var reg = /^\d{1,}$/;
        	$scope.isTrue = true;
        	$scope.data = "[";
        	angular.forEach($scope.rows, function(data,index,array){
        		$scope.rowIndex = index + 1;
        		$scope.showOrder = data.entity.showOrder;  // 排序值
        		$scope.isRecommend = data.entity.isRecommend;//复选框是否选中标示
        		$scope.rowId = data.entity.id;
        		if(!reg.test($scope.showOrder)){
        			$scope.isTrue = false;
        		}
        		$scope.data = $scope.data + "{" + "showOrder" +":"+$scope.showOrder + ","+"isRecommend" + ":" + $scope.isRecommend + "," + "id" + ":" + $scope.rowId + "}" + ",";
        		console.log($scope.data);
        	});
        	$scope.data = $scope.data + "]";
        	
        	if(!$scope.isTrue){
        		$scope.notice("请输入正整数！");
        		return ;
        	}
        	
        	if($scope.data.length >2){
        		$scope.isSubmit = true ;
        		var urlEncodeData = encodeURI($scope.data);
        		
        		$http({
                    url: "orgSourceConfig/saveOrgSourceConfig?data="+urlEncodeData,
                    method: "GET",
                }).success(function(result){
                	$scope.notice(result.msg);
                	$scope.isSubmit = false ;
                	$scope.appQueryOrgSourcConfList();
                });
        	}else{
        		$scope.notice("没有数据！");
        		return ;
        	}
        }
        
        $scope.changeBoxVal = function(row){
        	$scope.val = row.entity.isRecommend;
        	if($scope.val=="1"){
        		row.entity.isRecommend = "0";
        	}else if($scope.val=="0"){
        		row.entity.isRecommend = "1";
        	}
        }
        $scope.getAllBanks();
        //$scope.resetForm();
        $scope.appQueryOrgSourcConfList();
        $scope.gongzhonghaoQueryOrgSourcConfList();
 });
