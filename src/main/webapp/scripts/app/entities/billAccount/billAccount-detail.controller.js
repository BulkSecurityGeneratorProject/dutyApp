'use strict';

angular.module('dutyappApp')
    .controller('BillAccountDetailController', function ($scope, $stateParams, BillAccount) {
        $scope.billAccount = {};
        $scope.load = function (id) {
            BillAccount.get({id: id}, function(result) {
              $scope.billAccount = result;
            });
        };
        $scope.load($stateParams.id);
    });
