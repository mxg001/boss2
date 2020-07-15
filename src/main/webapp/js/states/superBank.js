function initSuperBank(stateProvider){
		/******************* 超级银行家 ************************/
        stateProvider.state('superBank', {
            abstract: true,
            url: "/superBank",
            templateUrl: "views/common/content.html",
        })
        .state('superBank.userManage', {
            url: "/superBank/userManage",
            templateUrl: 'views/superBank/userManage.html',
            data: {pageTitle: '超级银行家用户管理'},
            //controller: "userManageCtrl",
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/userManageCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.userInfoDetail', {
            url: "/superBank/userInfoSuperBank/:userCode",
            templateUrl: "views/superBank/userInfoDetail.html",
            data: {pageTitle: '超级银行家用户详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/userInfoDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateUserInfo', {
            url: "/superBank/updateUserInfo/:userCode",
            templateUrl: "views/superBank/updateUserInfo.html",
            data: {pageTitle: '超级银行家修改用户'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateUserInfoCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.orgInfoManager', {
            url: "/superBank/orgInfoManager",
            templateUrl: "views/superBank/orgInfoManager.html",
            data: {pageTitle: '银行家组织管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/orgInfoManagerCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addOrgInfo', {
            url: "/superBank/addOrgInfo",
            templateUrl: 'views/superBank/addOrgInfo.html',
            data: {pageTitle: '新增银行家组织'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addOrgInfoCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateOrgInfo', {
            url: "/superBank/updateOrgInfo/:orgId",
            templateUrl: 'views/superBank/updateOrgInfo.html',
            data: {pageTitle: '银行家组织修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateOrgInfoCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.orgBanksConfig', {
            url: "/superBank/orgBanksConfig/:orgId",
            templateUrl: 'views/superBank/orgBanksConfig.html',
            data: {pageTitle: '信用卡银行配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
               	 $ocLazyLoad.load('ui-switch');
                 $ocLazyLoad.load('ngGrid');
                 $ocLazyLoad.load('localytics.directives');
                 $ocLazyLoad.load('oitozero.ngSweetAlert');
                 $ocLazyLoad.load('infinity-chosen');
            },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/orgBanksConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.orgLoansConfig', {
            url: "/superBank/orgLoansConfig/:orgId",
            templateUrl: 'views/superBank/orgLoansConfig.html',
            data: {pageTitle: '贷款机构配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                  	 $ocLazyLoad.load('ui-switch');
                     $ocLazyLoad.load('ngGrid');
                     $ocLazyLoad.load('localytics.directives');
                     $ocLazyLoad.load('oitozero.ngSweetAlert');
                     $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/orgLoansConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.orgWxTemplate',{
            url: '/superBank/orgWxTemplate',
            templateUrl: "views/superBank/orgWxTemplate.html",
            //controller: 'orgWxTemplateCtrl',
            data: {pageTitle: '微信模板修改'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/orgWxTemplateCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.outWarn', {
            url: "/superBank/outWarn",
            templateUrl: "views/superBank/outWarn.html",
            data: {pageTitle: '出款余额不足预警'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/outWarnCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.creditcardSource', {
            url: "/superBank/creditcardSource",
            templateUrl: "views/superBank/creditcardSource.html",
            data: {pageTitle: '信用卡银行管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditcardSourceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addCreditcardSource', {
            url: "/superBank/addCreditcardSource",
            templateUrl: "views/superBank/addCreditcardSource.html",
            data: {pageTitle: '新增信用卡银行'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addCreditcardSourceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateCreditcardSource', {
            url: "/superBank/updateCreditcardSource/:id",
            templateUrl: "views/superBank/updateCreditcardSource.html",
            data: {pageTitle: '修改信用卡银行'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateCreditcardSourceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.creditcardSourceDetail', {
            url: "/superBank/creditcardSourceDetail/:id",
            templateUrl: "views/superBank/creditcardSourceDetail.html",
            data: {pageTitle: '信用卡银行详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    // $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditcardSourceDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.insuranceCompany', {
            url: "/superBank/insuranceCompany",
            templateUrl: "views/superBank/insuranceCompany.html",
            data: {pageTitle: '保险公司管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceCompanyCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addInsuranceCompany', {
            url: "/superBank/addInsuranceCompany",
            templateUrl: "views/superBank/addInsuranceCompany.html",
            data: {pageTitle: '新增保险公司'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addInsuranceCompanyCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateInsuranceCompany', {
            url: "/superBank/updateInsuranceCompany/:id",
            templateUrl: "views/superBank/updateInsuranceCompany.html",
            data: {pageTitle: '修改保险公司'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateInsuranceCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.insuranceCompanyDetail', {
            url: "/superBank/insuranceCompanyDetail/:id",
            templateUrl: "views/superBank/insuranceCompanyDetail.html",
            data: {pageTitle: '保险公司详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceCompanyDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.insuranceProduct', {
            url: "/superBank/insuranceProduct",
            templateUrl: "views/superBank/insuranceProduct.html",
            data: {pageTitle: '保险产品管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceProductCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addInsuranceProduct', {
            url: "/superBank/addInsuranceProduct",
            templateUrl: "views/superBank/addInsuranceProduct.html",
            data: {pageTitle: '新增保险产品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addInsuranceProductCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateInsuranceProduct', {
            url: "/superBank/updateInsuranceProduct/:id",
            templateUrl: "views/superBank/updateInsuranceProduct.html",
            data: {pageTitle: '修改保险产品'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateInsuranceProduct.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.insuranceProductDetail', {
            url: "/superBank/insuranceProductDetail/:id",
            templateUrl: "views/superBank/insuranceProductDetail.html",
            data: {pageTitle: '保险产品详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceProductDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*保险订单查询*/
        .state('superBank.insuranceOrder', {
            url: "/superBank/insuranceOrder",
            templateUrl: "views/superBank/insuranceOrder.html",
            data: {pageTitle: '保险订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/insuranceOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*保险订单详情界面*/
        .state('superBank.insuranceOrderDetail', {
            url: "/superBank/insuranceOrderDetail/:orderNo",
            templateUrl: 'views/superBank/insuranceOrderDetail.html',
            data: {pageTitle: '保险订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/insuranceOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*积分兑换订单查询*/
        .state('superBank.superExcOrder', {
            url: "/superBank/superExcOrder",
            templateUrl: "views/superBank/superExchangeOrder.html",
            data: {pageTitle: '积分兑换订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/superExchangeOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*积分兑换订单详情界面*/
        .state('superBank.superExchangeOrderDetail', {
            url: "/superBank/superExchangeOrderDetail/:orderNo",
            templateUrl: 'views/superBank/superExcOrderDetail.html',
            data: {pageTitle: '积分兑换订单详情'},
            resolve: {
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/superExcOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.loanInstitutionManagement', {
            url: "/superBank/loanInstitutionManagement",
            templateUrl: "views/superBank/loanInstitutionManagement.html",
            data: {pageTitle: '贷款机构管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanInstitutionManagementCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addLoanInstitutionManagement', {
            url: "/superBank/addLoanInstitutionManagement",
            templateUrl: "views/superBank/addLoanInstitutionManagement.html",
            data: {pageTitle: '新增贷款机构'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addLoanInstitutionManagementCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.updateLoanInstitution', {
            url: "/superBank/updateLoanInstitution/:id",
            templateUrl: "views/superBank/updateLoanInstitution.html",
            data: {pageTitle: '修改贷款机构'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/updateLoanInstitutionCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.loanInstitutionDetail', {
            url: "/superBank/loanInstitutionDetail/:id",
            templateUrl: "views/superBank/loanInstitutionDetail.html",
            data: {pageTitle: '贷款机构详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    // $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanInstitutionDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.bankImport', {
            url: "/superBank/bankImport",
            templateUrl: "views/superBank/bankImport.html",
            data: {pageTitle: '信用卡银行导入数据管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/bankImportCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.agentOrder', {
            url: "/superBank/agentOrder",
            templateUrl: "views/superBank/agentOrder.html",
            data: {pageTitle: '代理授权订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/agentOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.agentOrderDetail', {
            url: "/superBank/agentOrderDetail/:orderNo",
            templateUrl: 'views/superBank/agentOrderDetail.html',
            data: {pageTitle: '代理商授权订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/agentOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.openCreditOrder', {
            url: "/superBank/openCreditOrder",
            templateUrl: "views/superBank/openCreditOrder.html",
            data: {pageTitle: '开通信用卡还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/openCreditOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.openCreditDetail', {
            url: "/superBank/openCreditDetail/:orderNo",
            templateUrl: 'views/superBank/openCreditDetail.html',
            data: {pageTitle: '开通信用卡还款订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/openCreditDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.creditOrder', {
            url: "/superBank/creditOrder",
            templateUrl: "views/superBank/creditOrder.html",
            data: {pageTitle: '办理信用卡订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/creditOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.creditOrderDetail', {
            url: "/superBank/creditOrderDetail/:orderNo",
            templateUrl: 'views/superBank/creditOrderDetail.html',
            data: {pageTitle: '办理信用卡订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/creditOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.loanOrder', {
            url: "/superBank/loanOrder",
            templateUrl: "views/superBank/loanOrder.html",
            data: {pageTitle: '贷款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/loanOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.loanOrderDetail', {
            url: "/superBank/loanOrderDetail/:orderNo",
            templateUrl: 'views/superBank/loanOrderDetail.html',
            data: {pageTitle: '贷款订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/loanOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.receiveOrder', {
            url: "/superBank/receiveOrder",
            templateUrl: "views/superBank/receiveOrder.html",
            data: {pageTitle: '收款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/receiveOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.receiveOrderDetail', {
            url: "/superBank/receiveOrderDetail/:orderNo",
            templateUrl: 'views/superBank/receiveOrderDetail.html',
            data: {pageTitle: '收款订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/receiveOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.repayOrder', {
            url: "/superBank/repayOrder",
            templateUrl: "views/superBank/repayOrder.html",
            data: {pageTitle: '还款订单查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/repayOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.repayOrderDetail', {
            url: "/superBank/repayOrderDetail/:orderNo",
            templateUrl: 'views/superBank/repayOrderDetail.html',
            data: {pageTitle: '还款订单详情'},
            resolve: {
                // loadPlugin: function ($ocLazyLoad) {
                // },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/repayOrderDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.profitDetailOrder', {
            url: "/superBank/profitDetailOrder",
            templateUrl: "views/superBank/profitDetailOrder.html",
            data: {pageTitle: '订单分润明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    // $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/profitDetailOrderCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.obtainRecord', {
            url: "/superBank/obtainRecord",
            templateUrl: "views/superBank/obtainRecord.html",
            data: {pageTitle: '用户提现记录查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    // $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/obtainRecordCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.blacklist',{
            url: "/superBank/blacklist",
            templateUrl: "views/superBank/blacklist.html",
            data: {pageTitle: '黑名单管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/blacklistCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addBlack', {
            url: "/superBank/addBlack",
            templateUrl: "views/superBank/addBlackCtrl.html",
            data: {pageTitle: '新增黑名单'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addBlackCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.payChannlelist',{
            url: "/superBank/payChannlelist",
            templateUrl: "views/superBank/payChannlelist.html",
            data: {pageTitle: '支付通道管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/payChannlelistCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addPayChannle', {
            url: "/superBank/addPayChannle",
            templateUrl: "views/superBank/addPayChannle.html",
            data: {pageTitle: '新增支付通道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addPayChannleCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.editPayChannle', {
            url: "/superBank/editPayChannle/:id",
            templateUrl: "views/superBank/editPayChannle.html",
            data: {pageTitle: '修改支付通道'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/editPayChannleCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.receiptRoutelist',{
            url: "/superBank/receiptRoutelist",
            templateUrl: "views/superBank/receiptRoutelist.html",
            data: {pageTitle: '支付路由管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/receiptRoutelistCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.addReceiptRoute', {
            url: "/superBank/addReceiptRoute",
            templateUrl: "views/superBank/addReceiptRoute.html",
            data: {pageTitle: '新增支付路由'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/addReceiptRouteCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('superBank.editReceiptRoute', {
            url: "/superBank/editReceiptRoute/:id",
            templateUrl: "views/superBank/editReceiptRoute.html",
            data: {pageTitle: '修改支付路由'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/editReceiptRouteCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
		/******************* 超级银行家-红包 ************************/
        .state('red', {
            abstract: true,
            url: "/red",
            templateUrl: "views/common/content.html"
        })
        .state('red.redControl', {
            url:"/red/redControl",
            templateUrl: "views/red/redControl.html",
            data: {pageTitle: '红包业务管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redControlCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        /*红包领取查询*/
        .state('red.redEnvelopesReceive', {
            url: "/redEnvelopesReceive",
            templateUrl: "views/superBank/redEnvelopes/queryRedEnvelopesReceive.html",
            data: {pageTitle: '红包领取查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/redEnvelopes/queryRedEnvelopesReceiveCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*领地每日收益查询*/
        .state('red.redDailyDividend', {
            url: "/redDailyDividend",
            templateUrl: "views/superBank/redEnvelopes/dailyDividend.html",
            data: {pageTitle: '领地每日分红查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/redEnvelopes/dailyDividendCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*领地每日业务收益查询*/
        .state('red.dailyBusinessDividends', {
            url: "/dailyBusinessDividends",
            templateUrl: "views/superBank/redEnvelopes/dailyBusinessDividends.html",
            data: {pageTitle: '领地每日业务分红查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/superBank/redEnvelopes/dailyBusinessDividendsCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        /*红包发放查询*/
        .state('red.redEnvelopesGrant', {
            url: "/redEnvelopesGrant",
            templateUrl: "views/superBank/redEnvelopes/queryRedEnvelopesGrant.html",
            data: {pageTitle: '红包发放查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/queryRedEnvelopesGrantCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redEnvelopesGrantDetail', {
            url: "/redEnvelopesGrantDetail/:id",
            templateUrl: "views/superBank/redEnvelopes/redEnvelopesGrantDetail.html",
            data: {pageTitle: '/红包详情'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/redEnvelopesGrantDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redEnvelopesGrantExamine', {
            url: "/redEnvelopesGrantExamine/:id",
            templateUrl: "views/superBank/redEnvelopes/redEnvelopesGrantExamine.html",
            data: {pageTitle: '/红包审核'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('fancybox');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/superBank/redEnvelopes/redEnvelopesGrantExamineCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redConfigs', {
            url: "/redConfig",
            templateUrl: 'views/red/redConfig.html',
            data: {pageTitle: '红包配置管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.addRedConfig', {
            url: "/addRedConfig",
            templateUrl: 'views/red/addRedConfig.html',
            data: {pageTitle: '新增红包配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/addRedConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.editRedConfig', {
            url: "/editRedConfig/:id",
            templateUrl: 'views/red/editRedConfig.html',
            data: {pageTitle: '修改红包配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/editRedConfigCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redConfigDetail', {
            url: "/redConfigDetail/:id",
            templateUrl: 'views/red/redConfigDetail.html',
            data: {pageTitle: '修改红包配置'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('fileUpload');
                    $ocLazyLoad.load('ui-switch');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redConfigDetailCtrl.js?ver='+verNo]
                    });
                }]
            }
         })
        .state('red.redProductControl', {
            url: "/redProductControl/:busType",
            templateUrl: 'views/red/redProductControl.html',
            data: {pageTitle: '红包配置管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('ui.select');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redProductControlCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redOrgSortControl', {
            url: "/redOrgSortControl/:id/:busCode/:busType/:sortNum/:orgId/:category/:orgStatus/:flag",
            templateUrl: 'views/red/redOrgSortControl.html',
            data: {pageTitle: '分类布局配置管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('ui-switch');
                    $ocLazyLoad.load('ngGrid');
                    $ocLazyLoad.load('infinity-chosen');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redOrgSortControl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redOrgSortCtrl', {
            url: "/redOrgSortCtrl/:id/:busCode/:busType/:orgStatus",
            templateUrl: 'views/red/redOrgSortCtrl.html',
            data: {pageTitle: '分类布局管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redOrgSortCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redPersonalControl', {
            url: "/redPersonalControl",
            templateUrl: 'views/red/redPersonalControl.html',
            data: {pageTitle: '个人发红包'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redPersonalControlCtrl.js?ver='+verNo]
                    });
                }]
            }
        })
        .state('red.redOrg', {
            url:"/red/redOrg/:busCode/:busType",
            templateUrl: "views/red/redOrg.html",
            data: {pageTitle: '业务组织管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                    $ocLazyLoad.load('fileUpload');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redOrgCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.luckConf', {
            url:"/red/luckConf",
            templateUrl: "views/red/luckConf.html",
            data: {pageTitle: '用户幸运值管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/luckConfCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.plateAccount', {
            url:"/red/plateAccount/:id",
            templateUrl: "views/red/plateAccount.html",
            data: {pageTitle: '平台红包账户查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/plateAccountCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.redAccountDetailList', {
            url:"/red/redAccountDetailList",
            templateUrl: "views/red/redAccountDetailList.html",
            data: {pageTitle: '红包账户明细查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/redAccountDetailListCtrl.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.accountQuery', {
            url:"/red/accountQuery",
            templateUrl: "views/red/accountQuery.html",
            data: {pageTitle: '红包账户查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/accountQuery.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.manorManager', {
            url:"/red/manorManager",
            templateUrl: "views/red/manorManager.html",
            data: {pageTitle: '领地业务管理'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/manorManager.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.manorQuery', {
            url:"/red/manorQuery",
            templateUrl: "views/red/manorQuery.html",
            data: {pageTitle: '领地查询'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/red/manorQuery.js?ver=' + verNo]
                    });
                }]
            }
        })
         .state('red.manorAdjustRecore', {
            url:"/red/manorAdjustRecore",
            templateUrl: "views/red/manorAdjustRecore.html",
            data: {pageTitle: '领地调整价格记录'},
            resolve: {
                loadPlugin: function ($ocLazyLoad) {
                    $ocLazyLoad.load('My97DatePicker');
                    $ocLazyLoad.load('oitozero.ngSweetAlert');
                    $ocLazyLoad.load('localytics.directives');
                },
                deps: ['$ocLazyLoad', function ($ocLazyLoad) {
                    return $ocLazyLoad.load({
                        name: 'inspinia',
                        files: ['js/controllers/red/map.js','js/controllers/red/manorAdjustRecore.js?ver=' + verNo]
                    });
                }]
            }
        })
        .state('red.manorTransactionRecore', {
        	url:"/red/manorTransactionRecore/:p/:c/:r",
        	templateUrl: "views/red/manorTransactionRecore.html",
        	data: {pageTitle: '领地买卖记录'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('My97DatePicker');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('localytics.directives');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/red/map.js','js/controllers/red/manorTransactionRecore.js?ver=' + verNo]
        			});
        		}]
        	}
        })
        .state('red.manorTransactionRecoreDetail', {
        	url:"/red/manorTransactionRecoreDetail/:orderId",
        	templateUrl: "views/red/manorTransactionRecoreDetail.html",
        	data: {pageTitle: '领地业务管理'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('My97DatePicker');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/red/manorTransactionRecoreDetail.js?ver=' + verNo]
        			});
        		}]
        	}
        })
        .state('red.manorMoney', {
        	url:"/red/manorMoney",
        	templateUrl: "views/red/manorMoney.html",
        	data: {pageTitle: '领主领地收益表'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/red/map.js','js/controllers/red/manorMoney.js?ver=' + verNo]
        			});
        		}]
        	}
        })
        .state('red.manorMoneyDetl', {
        	url:"/red/manorMoneyDetl/:id",
        	templateUrl: "views/red/manorMoneyDetl.html",
        	data: {pageTitle: '转入红包账明细'},
        	resolve: {
        		loadPlugin: function ($ocLazyLoad) {
        			$ocLazyLoad.load('localytics.directives');
        			$ocLazyLoad.load('oitozero.ngSweetAlert');
        			$ocLazyLoad.load('My97DatePicker');
        		},
        		deps: ['$ocLazyLoad', function ($ocLazyLoad) {
        			return $ocLazyLoad.load({
        				name: 'inspinia',
        				files: ['js/controllers/red/map.js','js/controllers/red/manorMoneyDetl.js?ver=' + verNo]
        			});
        		}]
        	}
        })
        /******************* 超级银行家-红包 ************************/
        
}