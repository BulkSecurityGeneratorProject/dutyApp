'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('policyRule', {
                parent: 'entity',
                url: '/policyRule',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.policyRule.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/policyRule/policyRules.html',
                        controller: 'PolicyRuleController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('policyRule');
                        return $translate.refresh();
                    }]
                }
            })
            .state('policyRuleDetail', {
                parent: 'entity',
                url: '/policyRule/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.policyRule.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/policyRule/policyRule-detail.html',
                        controller: 'PolicyRuleDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('policyRule');
                        return $translate.refresh();
                    }]
                }
            });
    });
