(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomFacilityTypeDialogController', InstanceRoomFacilityTypeDialogController);

    InstanceRoomFacilityTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceRoomFacilityType', 'InstanceRoomFacility'];

    function InstanceRoomFacilityTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceRoomFacilityType, InstanceRoomFacility) {
        var vm = this;
        vm.instanceRoomFacilityType = entity;
        vm.instanceroomfacilitys = InstanceRoomFacility.query();
        vm.load = function(id) {
            InstanceRoomFacilityType.get({id : id}, function(result) {
                vm.instanceRoomFacilityType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceRoomFacilityTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceRoomFacilityType.id !== null) {
                InstanceRoomFacilityType.update(vm.instanceRoomFacilityType, onSaveSuccess, onSaveError);
            } else {
                InstanceRoomFacilityType.save(vm.instanceRoomFacilityType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
