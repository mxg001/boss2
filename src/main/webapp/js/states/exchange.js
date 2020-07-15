function initExchange(stateProvider){
	    /************** 超级兑 ***************/
		stateProvider.state('exchange', {
            abstract: true,
            url: "/exchange",
            templateUrl: "views/common/content.html",
        })
        .state('exchange.userManagement', {
            url: "/userManagement",
            templateUrl: 'views/exchange/user/userManagementQuery.html',
            data: {pageTitle: '超级兑用户管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/user/userManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.userManagementDetail', {
            url: "/userManagementDetail/:merchantNo",
            templateUrl: "views/exchange/user/userManagementDetail.html",
            data: {pageTitle: '超级兑用户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/user/userManagementDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.userManagementEdit', {
            url: "/userManagementEdit/:merchantNo",
            templateUrl: "views/exchange/user/userManagementEdit.html",
            data: {pageTitle: '超级兑用户修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/user/userManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOem', {
            url: "/exchangeOem",
            templateUrl: 'views/exchange/oem/exchangeOemQuery.html',
            data: {pageTitle: '积分兑现组织管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/exchangeOemQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOemEdit', {
            url: "/exchangeOemEdit/:id",
            templateUrl: 'views/exchange/oem/exchangeOemEdit.html',
            data: {pageTitle: '积分兑现组织修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/exchangeOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOemDetail', {
            url: "/exchangeOemDetail/:id",
            templateUrl: 'views/exchange/oem/exchangeOemDetail.html',
            data: {pageTitle: '积分兑现组织详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/exchangeOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOemAdd', {
            url: "/exchangeOemAdd",
            templateUrl: 'views/exchange/oem/exchangeOemAdd.html',
            data: {pageTitle: '积分兑现组织新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/exchangeOemAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productOem', {
            url: "/productOem/:oemNo",
            templateUrl: 'views/exchange/oem/productOemQuery.html',
            data: {pageTitle:'OEM产品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/productOemQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productOemEdit', {
            url: "/productOemEdit/:id/:oemNo",
            templateUrl: 'views/exchange/oem/productOemEdit.html',
            data: {pageTitle: 'OEM产品修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/productOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productOemDetail', {
            url: "/productOemDetail/:id/:oemNo",
            templateUrl: 'views/exchange/oem/productOemDetail.html',
            data: {pageTitle: 'OEM产品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/productOemEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productOemAdd', {
            url: "/productOemAdd/:oemNo",
            templateUrl: 'views/exchange/oem/productOemAdd.html',
            data: {pageTitle:'OEM产品新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/oem/productOemAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.orgManagement', {
            url: "/orgManagement",
            templateUrl: 'views/exchange/orgManagement/orgManagementQuery.html',
            data: {pageTitle: '积分机构管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/orgManagement/orgManagementQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.orgManagementEdit', {
            url: "/orgManagementEdit/:id",
            templateUrl: 'views/exchange/orgManagement/orgManagementEdit.html',
            data: {pageTitle: '积分机构修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/orgManagement/orgManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.orgManagementDetail', {
            url: "/orgManagementDetail/:id",
            templateUrl: 'views/exchange/orgManagement/orgManagementDetail.html',
            data: {pageTitle: '积分机构详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/orgManagement/orgManagementEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productType', {
            url: "/productType",
            templateUrl: 'views/exchange/productType/productTypeQuery.html',
            data: {pageTitle: '积分产品类别管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/productType/productTypeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productTypeAdd', {
            url: "/productTypeAdd",
            templateUrl: 'views/exchange/productType/productTypeAdd.html',
            data: {pageTitle: '积分产品类别新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/productType/productTypeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productTypeEdit', {
            url: "/productTypeEdit/:id",
            templateUrl: 'views/exchange/productType/productTypeEdit.html',
            data: {pageTitle: '积分产品类别编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/productType/productTypeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productTypeDetail', {
            url: "/productTypeDetail/:id",
            templateUrl: 'views/exchange/productType/productTypeDetail.html',
            data: {pageTitle: '积分产品类别详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/productType/productTypeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.product', {
            url: "/product",
            templateUrl: 'views/exchange/product/productQuery.html',
            data: {pageTitle: '积分产品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/product/productQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productEdit', {
            url: "/productEdit/:id",
            templateUrl: 'views/exchange/product/productEdit.html',
            data: {pageTitle: '积分产品修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/product/productEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.productDetail', {
            url: "/productDetail/:id",
            templateUrl: 'views/exchange/product/productDetail.html',
            data: {pageTitle: '积分产品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/product/productEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.agentOrder', {
            url: "/agentOrder",
            templateUrl: 'views/exchange/agentOrder/agentOrderQuery.html',
            data: {pageTitle: '代理授权订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/agentOrder/agentOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.agentOrderDetail', {
            url: "/agentOrderDetail/:id",
            templateUrl: 'views/exchange/agentOrder/agentOrderDetail.html',
            data: {pageTitle: '代理授权订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/agentOrder/agentOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOrder', {
            url: "/exchangeOrder",
            templateUrl: 'views/exchange/exchangeOrder/exchangeOrderQuery.html',
            data: {pageTitle: '积分兑换订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeOrderDetail', {
            url: "/exchangeOrderDetail/:id",
            templateUrl: 'views/exchange/exchangeOrder/exchangeOrderDetail.html',
            data: {pageTitle: '积分兑换订单详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeAudit', {
            url: "/exchangeAudit",
            templateUrl: 'views/exchange/exchangeOrder/exchangeAuditQuery.html',
            data: {pageTitle: '积分兑换核销管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeAuditQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeAuditDetail', {
            url: "/exchangeAuditDetail/:id",
            templateUrl: 'views/exchange/exchangeOrder/exchangeAuditDetail.html',
            data: {pageTitle: '积分兑换核销详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeAuditEdit', {
            url: "/exchangeAuditEdit/:id",
            templateUrl: 'views/exchange/exchangeOrder/exchangeAuditEdit.html',
            data: {pageTitle: '积分兑换核销'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeAuditTwoEdit', {
            url: "/exchangeAuditTwoEdit/:id",
            templateUrl: 'views/exchange/exchangeOrder/exchangeAuditTwoEdit.html',
            data: {pageTitle: '积分兑换二次核销'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeAuditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeAuditAdd', {
            url: "/exchangeAuditAdd",
            templateUrl: 'views/exchange/exchangeOrder/exchangeAuditAdd.html',
            data: {pageTitle: '积分兑换订单新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeAuditAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.exchangeEdit', {
            url: "/exchangeEdit/:id",
            templateUrl: 'views/exchange/exchangeOrder/exchangeEdit.html',
            data: {pageTitle: '积分兑换订单修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/exchangeOrder/exchangeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.shareOrder', {
            url: "/shareOrder",
            templateUrl: 'views/exchange/shareOrder/shareOrderQuery.html',
            data: {pageTitle: '订单分润明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/shareOrder/shareOrderQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.withdrawals', {
            url: "/withdrawals",
            templateUrl: 'views/exchange/withdrawals/withdrawalsQuery.html',
            data: {pageTitle: '用户提现记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/withdrawals/withdrawalsQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.banner', {
            url: "/banner",
            templateUrl: 'views/exchange/banner/bannerQuery.html',
            data: {pageTitle: 'Banner管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/banner/bannerQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.bannerAdd', {
            url: "/bannerAdd",
            templateUrl: 'views/exchange/banner/bannerAdd.html',
            data: {pageTitle: 'Banner新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/banner/bannerAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.bannerEdit', {
            url: "/bannerEdit/:id",
            templateUrl: 'views/exchange/banner/bannerEdit.html',
            data: {pageTitle: 'Banner编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/banner/bannerEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.bannerDetail', {
            url: "/bannerDetail/:id",
            templateUrl: 'views/exchange/banner/bannerDetail.html',
            data: {pageTitle: 'Banner详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/banner/bannerEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.notice', {
            url: "/notice",
            templateUrl: 'views/exchange/notice/noticeQuery.html',
            data: {pageTitle: '公告管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/notice/noticeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.noticeAdd', {
            url: "/noticeAdd",
            templateUrl: 'views/exchange/notice/noticeAdd.html',
            data: {pageTitle: '公告新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/notice/noticeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.noticeEdit', {
            url: "/noticeEdit/:id",
            templateUrl: 'views/exchange/notice/noticeEdit.html',
            data: {pageTitle: '公告编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/notice/noticeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.noticeDetail', {
            url: "/noticeDetail/:id",
            templateUrl: 'views/exchange/notice/noticeDetail.html',
            data: {pageTitle: '公告详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('summernote');
                    $ocLazyLoad.load('angular-summernote');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/notice/noticeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.helpCenter', {
            url: "/helpCenter",
            templateUrl: 'views/exchange/helpCenter/helpCenterQuery.html',
            data: {pageTitle: '帮助中心管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/helpCenter/helpCenterQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.helpCenterAdd', {
            url: "/helpCenterAdd",
            templateUrl: 'views/exchange/helpCenter/helpCenterAdd.html',
            data: {pageTitle: '帮助中心新增'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/helpCenter/helpCenterAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.helpCenterEdit', {
            url: "/helpCenterEdit/:id",
            templateUrl: 'views/exchange/helpCenter/helpCenterEdit.html',
            data: {pageTitle: '帮助中心编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/helpCenter/helpCenterEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.helpCenterDetail', {
            url: "/helpCenterDetail/:id",
            templateUrl: 'views/exchange/helpCenter/helpCenterDetail.html',
            data: {pageTitle: '帮助中心编辑'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/helpCenter/helpCenterEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.route', {
            url: "/route",
            templateUrl: 'views/exchange/route/routeQuery.html',
            data: {pageTitle: '路由管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/route/routeQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeAdd', {
            url: "/routeAdd",
            templateUrl: 'views/exchange/route/routeAdd.html',
            data: {pageTitle: '新增核销渠道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/route/routeAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeEdit', {
            url: "/routeEdit/:id",
            templateUrl: 'views/exchange/route/routeEdit.html',
            data: {pageTitle: '修改核销渠道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/route/routeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeDetail', {
            url: "/routeDetail/:id",
            templateUrl: 'views/exchange/route/routeDetail.html',
            data: {pageTitle: '核销渠道详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/route/routeEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeGoodAdd', {
            url: "/routeGoodAdd/:channelNo",
            templateUrl: 'views/exchange/route/routeGoodAdd.html',
            data: {pageTitle: '新增渠道商品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/route/routeGoodAddCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeGood', {
            url: "/routeGood",
            templateUrl: 'views/exchange/routeGood/routeGoodQuery.html',
            data: {pageTitle: '核销渠道商品明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/routeGood/routeGoodQueryCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeGoodDetail', {
            url: "/routeGoodDetail/:id",
            templateUrl: 'views/exchange/routeGood/routeGoodDetail.html',
            data: {pageTitle: '渠道商品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/routeGood/routeGoodEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('exchange.routeGoodEdit', {
            url: "/routeGoodEdit/:id",
            templateUrl: 'views/exchange/routeGood/routeGoodEdit.html',
            data: {pageTitle: '修改渠道商品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/exchange/routeGood/routeGoodEditCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        
}