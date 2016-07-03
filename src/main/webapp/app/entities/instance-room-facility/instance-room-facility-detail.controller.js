(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityDetailController', InstanceRoomFacilityDetailController);

    InstanceRoomFacilityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceRoomFacility', 'InstanceRoomFacilityType', 'InstanceRoomType', 'Booking'];

    function InstanceRoomFacilityDetailController($scope, $rootScope, $stateParams, entity, InstanceRoomFacility, InstanceRoomFacilityType, InstanceRoomType, Booking) {
        var vm = this;
        vm.instanceRoomFacility = entity;
        vm.load = function (id) {
            InstanceRoomFacility.get({id: id}, function(result) {
                vm.instanceRoomFacility = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceRoomFacilityUpdate', function(event, result) {
            vm.instanceRoomFacility = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
