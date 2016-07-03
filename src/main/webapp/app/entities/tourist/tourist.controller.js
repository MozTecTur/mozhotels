(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('TouristController', TouristController);

    TouristController.$inject = ['$scope', '$state', 'Tourist', 'TouristSearch'];

    function TouristController ($scope, $state, Tourist, TouristSearch) {
        var vm = this;
        vm.tourists = [];
        vm.loadAll = function() {
            Tourist.query(function(result) {
                vm.tourists = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            TouristSearch.query({query: vm.searchQuery}, function(result) {
                vm.tourists = result;
            });
        };
        vm.loadAll();
        
    }
})();
