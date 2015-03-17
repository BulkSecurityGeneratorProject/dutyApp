'use strict';

angular.module('dutyappApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('incident', {
                parent: 'entity',
                url: '/incident',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.incident.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/incident/incidents.html',
                        controller: 'IncidentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('incident');
                        return $translate.refresh();
                    }]
                }
            })
            .state('incidentDetail', {
                parent: 'entity',
                url: '/incident/:id',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'dutyappApp.incident.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/incident/incident-detail.html',
                        controller: 'IncidentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('incident');
                        return $translate.refresh();
                    }]
                }
            });
    });
