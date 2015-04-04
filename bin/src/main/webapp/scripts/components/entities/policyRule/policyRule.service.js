'use strict';

angular.module('dutyappApp')
    .factory('PolicyRule', function ($resource) {
        return $resource('api/policyRules/:id', {}, {
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
