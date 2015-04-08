'use strict';

angular.module('dutyappApp')
    .factory('Alert', function ($resource) {
        return $resource('api/alerts/:id', {id: '@id'}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.alert_time = new Date(data.alert_time);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
