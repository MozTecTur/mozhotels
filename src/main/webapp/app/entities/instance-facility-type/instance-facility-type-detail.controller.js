(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityTypeDetailController', InstanceFacilityTypeDetailController);

    InstanceFacilityTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceFacilityType', 'InstanceFacility'];

    function InstanceFacilityTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceFacilityType, InstanceFacility) {
        var vm = this;
        vm.instanceFacilityType = entity;
        vm.load = function (id) {
            InstanceFacilityType.get({id: id}, function(result) {
                vm.instanceFacilityType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceFacilityTypeUpdate', function(event, result) {
            vm.instanceFacilityType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
