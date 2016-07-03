(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyTypeDialogController', InstancePolicyTypeDialogController);

    InstancePolicyTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstancePolicyType', 'InstancePolicy'];

    function InstancePolicyTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstancePolicyType, InstancePolicy) {
        var vm = this;
        vm.instancePolicyType = entity;
        vm.instancepolicys = InstancePolicy.query();
        vm.load = function(id) {
            InstancePolicyType.get({id : id}, function(result) {
                vm.instancePolicyType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instancePolicyTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instancePolicyType.id !== null) {
                InstancePolicyType.update(vm.instancePolicyType, onSaveSuccess, onSaveError);
            } else {
                InstancePolicyType.save(vm.instancePolicyType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
