(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('GuestTouristController', GuestTouristController);

    GuestTouristController.$inject = ['$scope', '$state', 'GuestTourist', 'GuestTouristSearch'];

    function GuestTouristController ($scope, $state, GuestTourist, GuestTouristSearch) {
        var vm = this;
        vm.guestTourists = [];
        vm.loadAll = function() {
            GuestTourist.query(function(result) {
                vm.guestTourists = result;
            });
        };

        vm.search = function () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            GuestTouristSearch.query({query: vm.searchQuery}, function(result) {
                vm.guestTourists = result;
            });
        };
        vm.loadAll();
        
    }
})();
