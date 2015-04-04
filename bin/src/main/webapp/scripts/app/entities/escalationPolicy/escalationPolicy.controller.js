'use strict';

angular.module('dutyappApp')
    .controller('EscalationPolicyController', function ($scope, EscalationPolicy, Service, ParseLinks) {
        $scope.escalationPolicys = [];
        $scope.services = Service.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            EscalationPolicy.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.escalationPolicys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            EscalationPolicy.update($scope.escalationPolicy,
                function () {
                    $scope.loadAll();
                    $('#saveEscalationPolicyModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            EscalationPolicy.get({id: id}, function(result) {
                $scope.escalationPolicy = result;
                $('#saveEscalationPolicyModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            EscalationPolicy.get({id: id}, function(result) {
                $scope.escalationPolicy = result;
                $('#deleteEscalationPolicyConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            EscalationPolicy.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEscalationPolicyConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.escalationPolicy = {policy_name: null, has_cycle: null, cycle_time: null, id: null};
        };
    });
