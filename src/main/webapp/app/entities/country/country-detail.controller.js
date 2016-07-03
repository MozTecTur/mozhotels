(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('CountryDetailController', CountryDetailController);

    CountryDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'Country', 'Region'];

    function CountryDetailController($scope, $rootScope, $stateParams, entity, Country, Region) {
        var vm = this;
        vm.country = entity;
        vm.load = function (id) {
            Country.get({id: id}, function(result) {
                vm.country = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:countryUpdate', function(event, result) {
            vm.country = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
