(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('LocalTurDialogController', LocalTurDialogController);

    LocalTurDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'LocalTur', 'InstanceTur', 'Province'];

    function LocalTurDialogController ($scope, $stateParams, $uibModalInstance, entity, LocalTur, InstanceTur, Province) {
        var vm = this;
        vm.localTur = entity;
        vm.instanceturs = InstanceTur.query();
        vm.provinces = Province.query();
        vm.load = function(id) {
            LocalTur.get({id : id}, function(result) {
                vm.localTur = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:localTurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.localTur.id !== null) {
                LocalTur.update(vm.localTur, onSaveSuccess, onSaveError);
            } else {
                LocalTur.save(vm.localTur, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
