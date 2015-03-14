'use strict';

angular.module('dutyappApp')
    .controller('ServiceDetailController', function ($scope, $stateParams, Service, EscalationPolicy) {
        $scope.service = {};
        $scope.load = function (id) {
            Service.get({id: id}, function(result) {
              $scope.service = result;
            });
        };
        $scope.load($stateParams.id);
    });
