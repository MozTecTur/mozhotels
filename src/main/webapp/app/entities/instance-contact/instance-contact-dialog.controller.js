(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceContactDialogController', InstanceContactDialogController);

    InstanceContactDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceContact', 'InstanceTur'];

    function InstanceContactDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceContact, InstanceTur) {
        var vm = this;
        vm.instanceContact = entity;
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstanceContact.get({id : id}, function(result) {
                vm.instanceContact = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceContactUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceContact.id !== null) {
                InstanceContact.update(vm.instanceContact, onSaveSuccess, onSaveError);
            } else {
                InstanceContact.save(vm.instanceContact, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
