(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('RegionController', RegionController);

    RegionController.$inject = ['$scope', '$state', 'Region', 'RegionSearch'];

    function RegionController ($scope, $state, Region, RegionSearch) {
        var vm = this;
        vm.regions = [];
        vm.loadAll = function() {
            Region.query(function(result) {
                vm.regions = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            RegionSearch.query({query: vm.searchQuery}, function(result) {
                vm.regions = result;
            });
        };
        vm.loadAll();
        
    }
})();
