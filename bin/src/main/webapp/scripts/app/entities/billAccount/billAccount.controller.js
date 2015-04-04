'use strict';

angular.module('dutyappApp')
    .controller('BillAccountController', function ($scope, BillAccount, ParseLinks) {
        $scope.billAccounts = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            BillAccount.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.billAccounts = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.create = function () {
            BillAccount.update($scope.billAccount,
                function () {
                    $scope.loadAll();
                    $('#saveBillAccountModal').modal('hide');
                    $scope.clear();
                });
        };

        $scope.update = function (id) {
            BillAccount.get({id: id}, function(result) {
                $scope.billAccount = result;
                $('#saveBillAccountModal').modal('show');
            });
        };

        $scope.delete = function (id) {
            BillAccount.get({id: id}, function(result) {
                $scope.billAccount = result;
                $('#deleteBillAccountConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            BillAccount.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteBillAccountConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.clear = function () {
            $scope.billAccount = {company_name: null, id: null};
        };
    });
