'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('escalationPolicy', {
                parent: 'entity',
                url: '/escalationPolicy',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.escalationPolicy.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/escalationPolicy/escalationPolicys.html',
                        controller: 'EscalationPolicyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('escalationPolicy');
                        return $translate.refresh();
                    }]
                }
            })
            .state('escalationPolicyDetail', {
                parent: 'entity',
                url: '/escalationPolicy/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.escalationPolicy.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/escalationPolicy/escalationPolicy-detail.html',
                        controller: 'EscalationPolicyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('escalationPolicy');
                        return $translate.refresh();
                    }]
                }
            });
    });
