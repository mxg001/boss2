function initCreditRepay(stateProvider){
    /*信用卡还款管理start*/
    stateProvider.state('creditRepay', {
        abstract: true,
        url: "/creditRepay",
        templateUrl: "views/common/content.html",
    })
         /*交易查询*/
        .state('creditRepay.transactionQuery', {
            url: "/transactionQuery",
            templateUrl: "views/creditRepay/transactionQuery.html",
            data: {pageTitle: '交易查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/transactionQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*交易详情*/
        .state('creditRepay.transactionDetail', {
            url: "/transactionDetail/:orderNo",
            templateUrl: "views/creditRepay/transactionDetail.html",
            data: {pageTitle: '交易详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/transactionDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*用户管理*/
        .state('creditRepay.manage', {
            url: "/manage",
            templateUrl: "views/creditRepay/manage.html",
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/manageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //订单类型管理
        .state('creditRepay.repayTypeManager', {
            url: "/repayTypeManager",
            templateUrl: "views/creditRepay/repayTypeManager.html",
            data: {pageTitle: '订单类型管理'},
            //controller: "repayTypeManagerCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/repayTypeManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*订单类型详情*/
        .state('creditRepay.typeDetail', {
            url: "/typeDetail/:id",
            templateUrl: "views/creditRepay/typeDetail.html",
            data: {pageTitle: '订单类型详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/typeDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*订单类型修改*/
        .state('creditRepay.typeEdit', {
            url: "/typeEdit/:id",
            templateUrl: "views/creditRepay/typeEdit.html",
            data: {pageTitle: '订单类型修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/typeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //通道管理
        .state('creditRepay.channel', {
            url: "/channel",
            templateUrl: "views/creditRepay/channel.html",
            data: {pageTitle: '订单类型管理'},
            //controller: "channelCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/channelCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*新增通道*/
        .state('creditRepay.addChannel', {
            url: "/addChannel",
            templateUrl: "views/creditRepay/addChannel.html",
            data: {pageTitle: '商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/addChannelCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*通道详情*/
        .state('creditRepay.channelDetail', {
            url: "/channelDetail/:id",
            templateUrl: "views/creditRepay/channelDetail.html",
            data: {pageTitle: '订单类型详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/channelDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*通道修改*/
        .state('creditRepay.channelEdit', {
            url: "/channelEdit/:id",
            templateUrl: "views/creditRepay/channelEdit.html",
            data: {pageTitle: '订单类型修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/channelEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        //通道失败预警
        .state('creditRepay.failWarn', {
            url: "/failWarn",
            templateUrl: "views/creditRepay/failWarn.html",
            data: {pageTitle: '通道失败预警配置'},
            //controller: "failWarnCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/failWarnCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*用户详情*/
        .state('creditRepay.userDetail', {
            url: "/userDetail/:merchantNo",
            templateUrl: "views/creditRepay/userDetail.html",
            data: {pageTitle: '商户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/userDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*还款订单查询*/
        .state('creditRepay.repayOrder', {
            url: "/repayOrder",
            templateUrl: "views/creditRepay/creditRepayOrder/queryCreditRepayOrder.html",
            data: {pageTitle: '还款订单查询'},
            //controller: "queryCreditRepayOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/queryCreditRepayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*还款订单详情界面*/
        .state('creditRepay.repayOrderDetail', {
            url: "/repayOrderDetail/:id/:tallyOrderNo",
            data: {pageTitle: '出款订单详细信息'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrder.html'
                },
                'detailCreditRepayOrderCommon@creditRepay.repayOrderDetail':{
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrderCommon.html'
                }
            },
            //controller: "detailCreditRepayOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/detailCreditRepayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*还款订单处理流水*/
        .state('creditRepay.repayBill', {
            url: "/repayBill",
            templateUrl: "views/creditRepay/creditRepayOrder/processingWaterCreditRepayOrder.html",
            data: {pageTitle: '还款订单处理流水'},
            //controller: "processingWaterCreditRepayOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/processingWaterCreditRepayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*还款异常订单*/
        .state('creditRepay.repayException', {
            url: "/repayException",
            templateUrl: "views/creditRepay/creditRepayOrder/abnormalCreditRepayOrder.html",
            data: {pageTitle: '还款异常订单'},
            //controller: "abnormalCreditRepayOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/abnormalCreditRepayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*异常还款订单详情界面*/
        .state('creditRepay.abnormalRepayOrderDetail', {
            url: "/abnormalRepayOrderDetail/:id/:tallyOrderNo",
            data: {pageTitle: '出款订单详细信息'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayOrder/abnormalDetailCreditRepayOrder.html'
                },
                'detailCreditRepayOrderCommon@creditRepay.abnormalRepayOrderDetail':{
                    templateUrl: 'views/creditRepay/creditRepayOrder/detailCreditRepayOrderCommon.html'
                }
            },
            //controller: "detailCreditRepayOrderCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayOrder/detailCreditRepayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*服务商查询*/
        .state('creditRepay.provider', {
            url: "/provider",
            templateUrl: "views/creditRepay/provider.html",
            data: {pageTitle: '服务商查询'},
            // //controller: "autoCheckRuleCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/providerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*服务商分润查询*/
        .state('creditRepay.providerShare', {
            url: "/providerShare",
            templateUrl: "views/creditRepay/providerShare.html",
            data: {pageTitle: '服务商分润'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/providerShareCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*激活码管理*/
        .state('creditRepay.activationCode', {
            url: "/activationCode",
            templateUrl: "views/creditRepay/activationCode.html",
            data: {pageTitle: '激活码管理'},
            // //controller: "activationCodeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/activationCodeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*结算订单管理*/
        .state('creditRepay.settleOrder', {
            url: "/settleOrder",
            templateUrl: "views/creditRepay/settleOrder.html",
            data: {pageTitle: '结算订单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/settleOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡业务管理*/
        .state('creditRepay.business', {
            url: "/business",
            templateUrl: "views/creditRepay/repayBusiness.html",
            data: {pageTitle: '信用卡业务管理'},
            // //controller: "repayBusinessCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/repayBusinessCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡还款公告管理*/
        .state('creditRepay.notice', {
            url: "/notice",
            templateUrl: "views/creditRepay/creditRepayNotice/queryCreditRepayNotice.html",
            data: {pageTitle: '公告管理'},
            //controller: "queryCreditRepayNoticeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayNotice/queryCreditRepayNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡还款公告新增*/
        .state('creditRepay.addNotice', {
            url: "/addNotice",
            data: {pageTitle: '公告新增'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayNotice/addCreditRepayNotice.html'
                },
                'creditRepayNoticeEditCommon@creditRepay.addNotice':{
                    templateUrl: 'views/creditRepay/creditRepayNotice/creditRepayNoticeEditCommon.html'
                }
            },
            //controller: "addCreditRepayNoticeCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayNotice/addCreditRepayNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡还款公告修改*/
        .state('creditRepay.modifyNotice', {
            url: "/modifyNotice/:id",
            data: {pageTitle: '公告修改'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayNotice/modifyCreditRepayNotice.html'
                },
                'creditRepayNoticeEditCommon@creditRepay.modifyNotice':{
                    templateUrl: 'views/creditRepay/creditRepayNotice/creditRepayNoticeEditCommon.html'
                }
            },
            //controller: "modifyCreditRepayNoticeCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayNotice/modifyCreditRepayNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡还款公告详情*/
        .state('creditRepay.detailNotice', {
            url: "/detailNotice/:id",
            data: {pageTitle: '公告详情'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayNotice/detailCreditRepayNotice.html'
                },
                'creditRepayNoticeDetailCommon@creditRepay.detailNotice':{
                    templateUrl: 'views/creditRepay/creditRepayNotice/creditRepayNoticeDetailCommon.html'
                }
            },
            //controller: "detailCreditRepayNoticeCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayNotice/detailCreditRepayNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*信用卡还款公告修改*/
        .state('creditRepay.issueNotice', {
            url: "/issueNotice/:id",
            data: {pageTitle: '公告修改'},
            views: {
                '': {
                    templateUrl: 'views/creditRepay/creditRepayNotice/issueCreditRepayNotice.html'
                },
                'creditRepayNoticeDetailCommon@creditRepay.issueNotice':{
                    templateUrl: 'views/creditRepay/creditRepayNotice/creditRepayNoticeDetailCommon.html'
                }
            },
            //controller: "issueCreditRepayNoticeCtrl",
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad){
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/creditRepay/creditRepayNotice/issueCreditRepayNoticeCtrl.js?ver='+verNo]
                    });
                }]
            }
        });
    /*信用卡还款管理end*/
    /*超级NFC start*/
    stateProvider.state('superNfc', {
        abstract: true,
        url: "/superNfc",
        templateUrl: "views/common/content.html",
    })
         /*用户管理*/
        .state('superNfc.manage', {
            url: "/manage",
            templateUrl: "views/superNfc/nfcManage.html",
            data: {pageTitle: '用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superNfc/nfcManageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*通用码管理*/
          .state('superNfc.commonCode', {
              url: "/commonCode",
              templateUrl: "views/commonCode/commonCode.html",
              data: {pageTitle: '通用码管理'},
              resolve: {
                  loadPlugin: function ($ocLazyLoad) {
                      $ocLazyLoad.load('oitozero.ngSweetAlert');
                      $ocLazyLoad.load('ui-switch');
                      $ocLazyLoad.load('infinity-chosen');
                      $ocLazyLoad.load('My97DatePicker');
                      $ocLazyLoad.load('fileUpload');
                      $ocLazyLoad.load('fancybox');
                  },
                  deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                      return $ocLazyLoad.load({
                          name: 'inspinia',
                          files: ['js/controllers/commonCode/commonCodeCtrl.js?ver='+verNo]
                      });
                  }]
              }
          })
      .state('superNfc.addCommonCode', {
          url: "/addCommonCode",
          templateUrl: "views/commonCode/commonCodeAdd.html",
          data: {pageTitle: '通用码新增'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/commonCode/commonCodeAddCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
      .state('superNfc.updateCommonCode', {
          url: "/updateCommonCode/:id",
          templateUrl: "views/commonCode/commonCodeEdit.html",
          data: {pageTitle: '通用码修改'},
          resolve: {
              loadPlugin: function ($ocLazyLoad) {
                  $ocLazyLoad.load('oitozero.ngSweetAlert');
                  $ocLazyLoad.load('ui-switch');
                  $ocLazyLoad.load('infinity-chosen');
                  $ocLazyLoad.load('My97DatePicker');
                  $ocLazyLoad.load('fileUpload');
                  $ocLazyLoad.load('fancybox');
              },
              deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                  return $ocLazyLoad.load({
                      name: 'inspinia',
                      files: ['js/controllers/commonCode/commonCodeEditCtrl.js?ver='+verNo]
                  });
              }]
          }
      })
        /*激活码管理*/
        .state('superNfc.activationCode', {
            url: "/activationCode",
            templateUrl: "views/superNfc/nfcActivationCode.html",
            data: {pageTitle: '激活码管理'},
            // //controller: "activationCodeCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('datePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superNfc/nfcActivationCodeCtrl.js?ver='+verNo]
                    });
                }]
            }
        });
    /*超级NFC end*/


}