(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('RegionDetailController', RegionDetailController);

    RegionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Region', 'Province', 'Country'];

    function RegionDetailController($scope, $rootScope, $stateParams, entity, Region, Province, Country) {
        var vm = this;
        vm.region = entity;
        vm.load = function (id) {
            Region.get({id: id}, function(result) {
                vm.region = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:regionUpdate', function(event, result) {
            vm.region = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
