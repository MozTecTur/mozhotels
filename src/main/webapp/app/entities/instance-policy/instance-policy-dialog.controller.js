(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyDialogController', InstancePolicyDialogController);

    InstancePolicyDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstancePolicy', 'InstancePolicyType', 'InstanceTur'];

    function InstancePolicyDialogController ($scope, $stateParams, $uibModalInstance, entity, InstancePolicy, InstancePolicyType, InstanceTur) {
        var vm = this;
        vm.instancePolicy = entity;
        vm.instancepolicytypes = InstancePolicyType.query();
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstancePolicy.get({id : id}, function(result) {
                vm.instancePolicy = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instancePolicyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instancePolicy.id !== null) {
                InstancePolicy.update(vm.instancePolicy, onSaveSuccess, onSaveError);
            } else {
                InstancePolicy.save(vm.instancePolicy, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
