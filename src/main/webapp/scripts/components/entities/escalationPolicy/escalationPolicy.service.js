'use strict';

angular.module('dutyappApp')
    .factory('EscalationPolicy', function ($resource) {
        return $resource('api/escalationPolicys/:id', {id: '@id'}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
