(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceFacilityDialogController', InstanceFacilityDialogController);

    InstanceFacilityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceFacility', 'InstanceFacilityType', 'InstanceTur'];

    function InstanceFacilityDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceFacility, InstanceFacilityType, InstanceTur) {
        var vm = this;
        vm.instanceFacility = entity;
        vm.instancefacilitytypes = InstanceFacilityType.query();
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstanceFacility.get({id : id}, function(result) {
                vm.instanceFacility = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceFacilityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceFacility.id !== null) {
                InstanceFacility.update(vm.instanceFacility, onSaveSuccess, onSaveError);
            } else {
                InstanceFacility.save(vm.instanceFacility, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
