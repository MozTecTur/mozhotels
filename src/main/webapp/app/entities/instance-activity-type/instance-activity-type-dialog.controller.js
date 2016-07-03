(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityTypeDialogController', InstanceActivityTypeDialogController);

    InstanceActivityTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceActivityType', 'InstanceActivity'];

    function InstanceActivityTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceActivityType, InstanceActivity) {
        var vm = this;
        vm.instanceActivityType = entity;
        vm.instanceactivitys = InstanceActivity.query();
        vm.load = function(id) {
            InstanceActivityType.get({id : id}, function(result) {
                vm.instanceActivityType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceActivityTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceActivityType.id !== null) {
                InstanceActivityType.update(vm.instanceActivityType, onSaveSuccess, onSaveError);
            } else {
                InstanceActivityType.save(vm.instanceActivityType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
