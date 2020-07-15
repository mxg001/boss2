function initCjt(stateProvider) {
    /******************* 超级推 ************************/
    stateProvider.state('cjt', {
        abstract: true,
        url: "/cjt",
        templateUrl: "views/common/content.html",
    })
    //**************超级推活动设置***********
    .state('cjt.cjtProfitRule', {
        url: "/cjtProfitRule",
        templateUrl: "views/cjt/cjtProfitRule.html",
        data: {pageTitle: '超级推活动设置'},
        resolve: {
            loadPlugin: function ($ocLazyLoad) {
                $ocLazyLoad.load('localytics.directives')
                $ocLazyLoad.load('oitozero.ngSweetAlert');
                $ocLazyLoad.load('ui-switch');
            },
            deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                return $ocLazyLoad.load({
                    name: 'inspinia',
                    files: ['js/controllers/cjt/cjtProfitRuleCtrl.js?ver='+verNo]
                });
            }]
        }
    })
        .state('cjt.queryCjtGoods', {
            url: "/queryCjtGoods",
            data: {pageTitle: '商品管理'},
            templateUrl: 'views/cjt/cjtGoods/queryCjtGoods.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtGoods/queryCjtGoods.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.addCjtGoods', {
            url: "/addCjtGoods",
            data: {pageTitle: '新增超级推商品'},
            templateUrl: 'views/cjt/cjtGoods/addCjtGoods.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtGoods/addCjtGoods.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.updateCjtGoods', {
            url: "/updateCjtGoods/:id",
            data: {pageTitle: '修改超级推商品'},
            templateUrl: 'views/cjt/cjtGoods/updateCjtGoods.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtGoods/updateCjtGoods.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.detailCjtGoods', {
            url: "/detailCjtGoods/:id",
            data: {pageTitle: '超级推商品详情'},
            templateUrl: 'views/cjt/cjtGoods/detailCjtGoods.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtGoods/detailCjtGoods.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryCjtWhiteMer', {
            url: "/queryCjtWhiteMer",
            data: {pageTitle: '白名单管理'},
            templateUrl: 'views/cjt/queryCjtWhiteMer.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/queryCjtWhiteMer.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryCjtOrder', {
            url: "/queryCjtOrder",
            data: {pageTitle: '机具申领订单'},
            templateUrl: 'views/cjt/cjtOrder/queryCjtOrder.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtOrder/queryCjtOrder.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.detailCjtOrder', {
            url: "/detailCjtOrder/:orderNo",
            data: {pageTitle: '机具申领订单详情'},
            templateUrl: 'views/cjt/cjtOrder/detailCjtOrder.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtOrder/detailCjtOrder.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.shipDetail', {
            url: "/shipDetail/:orderNo",
            data: {pageTitle: '机具申领订单发货详情'},
            templateUrl: 'views/cjt/cjtOrder/shipDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtOrder/shipDetail.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.ship', {
            url: "/ship/:orderNo",
            data: {pageTitle: '机具发货'},
            templateUrl: 'views/cjt/cjtOrder/ship.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtOrder/ship.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryCjtAfterSale', {
            url: "/queryCjtAfterSale",
            data: {pageTitle: '申领售后订单'},
            templateUrl: 'views/cjt/queryCjtAfterSale.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/queryCjtAfterSale.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryCjtMerchantInfo', {
            url: "/queryCjtMerchantInfo",
            data: {pageTitle: '超级推商户查询'},
            templateUrl: 'views/cjt/cjtMerchantInfo/queryCjtMerchantInfo.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtMerchantInfo/queryCjtMerchantInfo.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.detailCjtMerchantInfo', {
            url: "/detailCjtMerchantInfo/:merchantNo",
            data: {pageTitle: '超级推商户详情'},
            templateUrl: 'views/cjt/cjtMerchantInfo/detailCjtMerchantInfo.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtMerchantInfo/detailCjtMerchantInfo.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.accountDetail', {
            url: "/accountDetail/:merchantNo",
            data: {pageTitle: '超级推商户提现详情'},
            templateUrl: 'views/cjt/cjtMerchantInfo/accountDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/cjtMerchantInfo/accountDetail.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryCjtProfitDetail', {
            url: "/queryCjtProfitDetail",
            data: {pageTitle: '商户-超级推收益明细'},
            templateUrl: 'views/cjt/queryCjtProfitDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/queryCjtProfitDetail.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('cjt.queryAgentCjtProfitDetail', {
            url: "/queryAgentCjtProfitDetail",
            data: {pageTitle: '代理商-超级推收益明细'},
            templateUrl: 'views/cjt/queryAgentCjtProfitDetail.html',
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load ({
                        name: 'inspinia',
                        files: ['js/controllers/cjt/queryAgentCjtProfitDetail.js?ver='+verNo]
                    });
                }]
            }
        })
}