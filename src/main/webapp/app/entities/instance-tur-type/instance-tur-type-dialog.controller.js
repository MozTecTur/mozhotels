(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurTypeDialogController', InstanceTurTypeDialogController);

    InstanceTurTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceTurType', 'InstanceTur'];

    function InstanceTurTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceTurType, InstanceTur) {
        var vm = this;
        vm.instanceTurType = entity;
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstanceTurType.get({id : id}, function(result) {
                vm.instanceTurType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceTurTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceTurType.id !== null) {
                InstanceTurType.update(vm.instanceTurType, onSaveSuccess, onSaveError);
            } else {
                InstanceTurType.save(vm.instanceTurType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
