(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InfoDialogController', InfoDialogController);

    InfoDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Info'];

    function InfoDialogController ($scope, $stateParams, $uibModalInstance, entity, Info) {
        var vm = this;
        vm.info = entity;
        vm.load = function(id) {
            Info.get({id : id}, function(result) {
                vm.info = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:infoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.info.id !== null) {
                Info.update(vm.info, onSaveSuccess, onSaveError);
            } else {
                Info.save(vm.info, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
