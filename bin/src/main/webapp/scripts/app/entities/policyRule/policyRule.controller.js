'use strict';

angular.module('dutyappApp')
    .controller('PolicyRuleController', function ($scope, PolicyRule, EscalationPolicy, User, ParseLinks) {
        $scope.policyRules = [];
        $scope.escalationpolicys = EscalationPolicy.query();
        $scope.users = User.query();
        $scope.page = 1;
        $scope.loadAll = function() {
            PolicyRule.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.policyRules = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            PolicyRule.update($scope.policyRule,
                function () {
                    $scope.loadAll();
                    $('#savePolicyRuleModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            PolicyRule.get({id: id}, function(result) {
                $scope.policyRule = result;
                $('#savePolicyRuleModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            PolicyRule.get({id: id}, function(result) {
                $scope.policyRule = result;
                $('#deletePolicyRuleConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            PolicyRule.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePolicyRuleConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.policyRule = {sequence: null, escalate_time: null, id: null};
        };

        $scope.removeRule = function() {
            $scope.policyRule = null;
        }
    });
