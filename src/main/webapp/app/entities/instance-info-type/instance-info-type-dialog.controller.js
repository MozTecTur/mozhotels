(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoTypeDialogController', InstanceInfoTypeDialogController);

    InstanceInfoTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceInfoType', 'InstanceInfo'];

    function InstanceInfoTypeDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceInfoType, InstanceInfo) {
        var vm = this;
        vm.instanceInfoType = entity;
        vm.instanceinfos = InstanceInfo.query();
        vm.load = function(id) {
            InstanceInfoType.get({id : id}, function(result) {
                vm.instanceInfoType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceInfoTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceInfoType.id !== null) {
                InstanceInfoType.update(vm.instanceInfoType, onSaveSuccess, onSaveError);
            } else {
                InstanceInfoType.save(vm.instanceInfoType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
