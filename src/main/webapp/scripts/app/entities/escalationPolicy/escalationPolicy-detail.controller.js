'use strict';

angular.module('dutyappApp')
    .controller('EscalationPolicyDetailController', function ($scope, $stateParams, EscalationPolicy, Service, N) {
        $scope.escalationPolicy = {};
        $scope.load = function (id) {
            EscalationPolicy.get({id: id}, function(result) {
              $scope.escalationPolicy = result;
            });
        };
        $scope.load($stateParams.id);
    });
