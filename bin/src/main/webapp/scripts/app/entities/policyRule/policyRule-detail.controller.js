'use strict';

angular.module('dutyappApp')
    .controller('PolicyRuleDetailController', function ($scope, $stateParams, PolicyRule, EscalationPolicy, User) {
        $scope.policyRule = {};
        $scope.load = function (id) {
            PolicyRule.get({id: id}, function(result) {
              $scope.policyRule = result;
            });
        };
        $scope.load($stateParams.id);
    });
