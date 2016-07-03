(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityTypeDialogController', InstanceFacilityTypeDialogController);

    InstanceFacilityTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceFacilityType', 'InstanceFacility'];

    function InstanceFacilityTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceFacilityType, InstanceFacility) {
        var vm = this;
        vm.instanceFacilityType = entity;
        vm.instancefacilitys = InstanceFacility.query();
        vm.load = function(id) {
            InstanceFacilityType.get({id : id}, function(result) {
                vm.instanceFacilityType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceFacilityTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceFacilityType.id !== null) {
                InstanceFacilityType.update(vm.instanceFacilityType, onSaveSuccess, onSaveError);
            } else {
                InstanceFacilityType.save(vm.instanceFacilityType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
