(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityDetailController', InstanceFacilityDetailController);

    InstanceFacilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceFacility', 'InstanceFacilityType', 'InstanceTur'];

    function InstanceFacilityDetailController($scope, $rootScope, $stateParams, entity, InstanceFacility, InstanceFacilityType, InstanceTur) {
        var vm = this;
        vm.instanceFacility = entity;
        vm.load = function (id) {
            InstanceFacility.get({id: id}, function(result) {
                vm.instanceFacility = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceFacilityUpdate', function(event, result) {
            vm.instanceFacility = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
