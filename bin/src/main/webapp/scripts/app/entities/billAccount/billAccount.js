'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('billAccount', {
                parent: 'entity',
                url: '/billAccount',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.billAccount.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/billAccount/billAccounts.html',
                        controller: 'BillAccountController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('billAccount');
                        return $translate.refresh();
                    }]
                }
            })
            .state('billAccountDetail', {
                parent: 'entity',
                url: '/billAccount/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.billAccount.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/billAccount/billAccount-detail.html',
                        controller: 'BillAccountDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('billAccount');
                        return $translate.refresh();
                    }]
                }
            });
    });
