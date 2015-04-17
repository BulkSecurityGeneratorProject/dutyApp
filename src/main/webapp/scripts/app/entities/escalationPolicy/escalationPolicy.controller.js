'use strict';

angular.module('dutyappApp')
    .controller('EscalationPolicyController', function ($scope, EscalationPolicy, Service, User, ParseLinks) {
        $scope.escalationPolicys = [];
        $scope.services = Service.query();
        $scope.page = 1;
        $scope.users = User.query();

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

        $scope.create = function (id) {
            if (id == null) {
                if ($scope.escalationPolicy.has_cycle == null)
                    $scope.escalationPolicy.has_cycle = false;

                EscalationPolicy.save($scope.escalationPolicy,
                function () {
                    $scope.loadAll();
                    $('#saveEscalationPolicyModal').modal('hide');
                    $scope.clear();
                });
            } else {
                EscalationPolicy.update($scope.escalationPolicy,
                function () {
                    $scope.loadAll();
                    $('#saveEscalationPolicyModal').modal('hide');
                    $scope.clear();
                });
            }
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

        $scope.removeRule = function (index) {
            $scope.escalationPolicy.policyRules.splice(index, 1);
        }

        $scope.removeUser = function(ruleIndex, userIndex) {
            $scope.escalationPolicy.policyRules[ruleIndex].users.splice(userIndex, 1);
        }

        $scope.addRule = function () {
            $scope.escalationPolicy.policyRules.push(null);
        }
    });
