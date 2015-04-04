'use strict';

angular.module('dutyappApp')
    .factory('BillAccount', function ($resource) {
        return $resource('api/billAccounts/:id', {}, {
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
