(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('ProvinceController', ProvinceController);

    ProvinceController.$inject = ['$scope', '$state', 'DataUtils', 'Province', 'ProvinceSearch'];

    function ProvinceController ($scope, $state, DataUtils, Province, ProvinceSearch) {
        var vm = this;
        vm.provinces = [];
        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
        vm.loadAll = function() {
            Province.query(function(result) {
                vm.provinces = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            ProvinceSearch.query({query: vm.searchQuery}, function(result) {
                vm.provinces = result;
            });
        };
        vm.loadAll();
        
    }
})();
