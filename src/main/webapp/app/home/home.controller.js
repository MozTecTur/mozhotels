(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', 'Province'];

    function HomeController ($scope, Principal, LoginService, Province) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.provinces = Province.query();

        $scope.$on('authenticationSuccess', function() {
            console.log("scope on home...");

            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                console.log(account);
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        vm.checkIn = new Date();
        vm.checkOut = new Date();
    }
})();
