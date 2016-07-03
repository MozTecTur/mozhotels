(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoDialogController', InstanceInfoDialogController);

    InstanceInfoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceInfo', 'InstanceInfoType', 'InstanceTur'];

    function InstanceInfoDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceInfo, InstanceInfoType, InstanceTur) {
        var vm = this;
        vm.instanceInfo = entity;
        vm.instanceinfotypes = InstanceInfoType.query();
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstanceInfo.get({id : id}, function(result) {
                vm.instanceInfo = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceInfoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceInfo.id !== null) {
                InstanceInfo.update(vm.instanceInfo, onSaveSuccess, onSaveError);
            } else {
                InstanceInfo.save(vm.instanceInfo, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
