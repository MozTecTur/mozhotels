(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityDialogController', InstanceRoomFacilityDialogController);

    InstanceRoomFacilityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceRoomFacility', 'InstanceRoomFacilityType', 'InstanceRoomType', 'Booking'];

    function InstanceRoomFacilityDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceRoomFacility, InstanceRoomFacilityType, InstanceRoomType, Booking) {
        var vm = this;
        vm.instanceRoomFacility = entity;
        vm.instanceroomfacilitytypes = InstanceRoomFacilityType.query();
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.bookings = Booking.query();
        vm.load = function(id) {
            InstanceRoomFacility.get({id : id}, function(result) {
                vm.instanceRoomFacility = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceRoomFacilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceRoomFacility.id !== null) {
                InstanceRoomFacility.update(vm.instanceRoomFacility, onSaveSuccess, onSaveError);
            } else {
                InstanceRoomFacility.save(vm.instanceRoomFacility, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
