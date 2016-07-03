(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityTypeDetailController', InstanceRoomFacilityTypeDetailController);

    InstanceRoomFacilityTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceRoomFacilityType', 'InstanceRoomFacility'];

    function InstanceRoomFacilityTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceRoomFacilityType, InstanceRoomFacility) {
        var vm = this;
        vm.instanceRoomFacilityType = entity;
        vm.load = function (id) {
            InstanceRoomFacilityType.get({id: id}, function(result) {
                vm.instanceRoomFacilityType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceRoomFacilityTypeUpdate', function(event, result) {
            vm.instanceRoomFacilityType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
