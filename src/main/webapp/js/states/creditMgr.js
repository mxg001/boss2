function initCreditMgr(stateProvider){
    	/************** 信用卡管家 start***************/
	    stateProvider.state('creditMgr', {
            abstract: true,
            url: "/creditMgr",
            templateUrl: "views/common/content.html",
	    })

	    .state('creditMgr.queryCardAndReward', {
            url: "/queryCardAndReward",
            templateUrl: "views/creditRepay/queryCardAndReward.html",
            data: {pageTitle: '办卡管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/queryCardAndRewardCtrl.js?ver='+verNo]
                    });
                }]
            }
        })

        .state('creditMgr.couponImportCard', {
            url: "/couponImportCard",
            templateUrl: "views/creditRepay/couponImportCard/couponImportCard.html",
            data: {pageTitle: '办卡导入赠送'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/couponImportCard/couponImportCardCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        
        .state('creditMgr.queryLoanAndReward', {
            url: "/queryLoanAndReward",
            templateUrl: "views/creditRepay/queryLoanAndReward.html",
            data: {pageTitle: '贷款管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen')
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/queryLoanAndRewardCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('creditMgr.couponImportLoan', {
            url: "/couponImportLoan",
            templateUrl: "views/creditRepay/couponImportLoan/couponImportLoan.html",
            data: {pageTitle: '贷款导入赠送'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/couponImportLoan/couponImportLoanCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
	    
        .state('creditMgr.queryCardAndRewardDetail',{
            url: '/creditMgr/queryCardAndRewardDetail/:userNo',
            templateUrl: "views/creditMgr/queryCardAndRewardDetail.html",
            data: {pageTitle: '赠送记录详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/queryCardAndRewardDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        
        /*用户管理*/
        .state('creditMgr.userManage', {
            url: "/userManage",
            templateUrl: "views/creditMgr/userManage.html",
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/userManageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //用户详情
        .state('creditMgr.userDetail',{
            url: '/creditMgr/userDetail/:userNo',
            templateUrl: "views/creditMgr/userDetail.html",
            data: {pageTitle: '用户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/userDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //修改用户
        .state('creditMgr.updateUserDetail',{
            url: '/creditMgr/updateUserDetail/:userNo',
            templateUrl: "views/creditMgr/updateUserDetail.html",
            data: {pageTitle: '修改用户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/updateUserDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*卡片管理*/
        .state('creditMgr.cardManage', {
            url: "/cardManage",
            templateUrl: "views/creditMgr/cardManage.html",
            data: {pageTitle: '卡片管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/cardManageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //卡片详情
        .state('creditMgr.cardDetail',{
            url: '/creditMgr/cardDetail/:id',
            templateUrl: "views/creditMgr/cardDetail.html",
            data: {pageTitle: '卡片详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/cardDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*订单管理*/
        .state('creditMgr.orderManage', {
            url: "/orderManage",
            templateUrl: "views/creditMgr/orderManage.html",
            data: {pageTitle: '订单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/orderManageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //订单详情
        .state('creditMgr.orderDetail',{
            url: '/creditMgr/orderDetail/:tradeNo',
            templateUrl: "views/creditMgr/orderDetail.html",
            data: {pageTitle: '订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/orderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*账单清单*/
        .state('creditMgr.billInventory', {
            url: "/billInventory",
            templateUrl: "views/creditMgr/billInventory.html",
            data: {pageTitle: '账单清单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/billInventoryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //账单明细
        .state('creditMgr.billDetail',{
            url: '/creditMgr/billDetail/:id',
            templateUrl: "views/creditMgr/billDetail.html",
            data: {pageTitle: '账单明细'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/billDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //评测报告
        .state('creditMgr.reviewsReport',{
        	url: '/creditMgr/reviewsReport/:billId',
        	templateUrl: "views/creditMgr/reviewsReport.html",
        	data: {pageTitle: '评测报告'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        		},
        		deps: ['$ocLazyLoad',function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/creditMgr/reviewsReportCtrl.js?ver='+verNo]
        			});
        		}]
        	}
        })
        /*提额任务*/
        .state('creditMgr.quotaTask', {
            url: "/quotaTask",
            templateUrl: "views/creditMgr/quotaTask.html",
            data: {pageTitle: '提额任务'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/quotaTaskCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //任务详情
        .state('creditMgr.taskDetail',{
        	url: '/creditMgr/taskDetail/:id',
        	templateUrl: "views/creditMgr/taskDetail.html",
        	data: {pageTitle: '任务详情'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        		},
        		deps: ['$ocLazyLoad',function($ocLazyLoad){
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/creditMgr/taskDetailCtrl.js?ver='+verNo]
        			});
        		}]
        	}
        })
        /*还款查询*/
        .state('creditMgr.repayQuery', {
            url: "/repayQuery",
            templateUrl: "views/creditMgr/repayQuery.html",
            data: {pageTitle: '还款查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/repayQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //还款详情
        .state('creditMgr.repayDetail',{
            url: '/creditMgr/repayDetail/:id',
            templateUrl: "views/creditMgr/repayDetail.html",
            data: {pageTitle: '还款详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/repayDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*服务消息*/
        .state('creditMgr.serviceMessage', {
            url: "/serviceMessage",
            templateUrl: "views/creditMgr/serviceMessage.html",
            data: {pageTitle: '服务消息'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/serviceMessageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //消息详情
        .state('creditMgr.messageDetail',{
            url: '/creditMgr/messageDetail/:id',
            templateUrl: "views/creditMgr/messageDetail.html",
            data: {pageTitle: '消息详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/messageDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*banner管理*/
        .state('creditMgr.bannerMgr', {
            url: "/bannerMgr",
            templateUrl: "views/creditMgr/bannerMgr.html",
            data: {pageTitle: 'banner管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/bannerMgrCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //新增banner
        .state('creditMgr.addBanner',{
            url: '/addBanner',
            templateUrl: "views/creditMgr/addBanner.html",
            data: {pageTitle: '新增banner'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/addBannerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Banner详情
        .state('creditMgr.bannerDetail',{
            url: '/creditMgr/bannerDetail/:id',
            templateUrl: "views/creditMgr/bannerDetail.html",
            data: {pageTitle: 'Banner详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/bannerDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //Banner修改
        .state('creditMgr.updateBanner',{
            url: '/creditMgr/updateBanner/:id',
            templateUrl: "views/creditMgr/updateBanner.html",
            data: {pageTitle: '修改Banner'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/updateBannerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*系统公告*/
        .state('creditMgr.systemNotice', {
            url: "/systemNotice",
            templateUrl: "views/creditMgr/systemNotice.html",
            data: {pageTitle: '系统公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/systemNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //新增公告
        .state('creditMgr.addNotice',{
            url: '/creditMgr/addNotice',
            templateUrl: "views/creditMgr/addNotice.html",
            data: {pageTitle: '新增公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/addNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //公告详情
        .state('creditMgr.noticeDetail',{
            url: '/creditMgr/noticeDetail/:id',
            templateUrl: "views/creditMgr/noticeDetail.html",
            data: {pageTitle: '公告详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/noticeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //修改公告
        .state('creditMgr.updateNotice',{
            url: '/creditMgr/updateNotice/:id',
            templateUrl: "views/creditMgr/updateNotice.html",
            data: {pageTitle: '修改公告'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad',function($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/updateNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡管家设置*/
        .state('creditMgr.cmSetting', {
            url: "/cmSetting",
            templateUrl: "views/creditMgr/cmSetting.html",
            data: {pageTitle: '信用卡管家设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/cmSettingCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡管家设置*/
        .state('creditMgr.cmSettingAgent', {
            url: "/cmSettingAgent",
            templateUrl: "views/creditMgr/cmSettingAgent.html",
            data: {pageTitle: '信用卡管家设置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/cmSettingAgentCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*分润查询*/
        .state('creditMgr.shareQuery', {
            url: "/shareQuery",
            templateUrl: "views/creditMgr/shareQuery.html",
            data: {pageTitle: '分润查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditMgr/shareQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        
}