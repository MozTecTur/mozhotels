(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityDialogController', InstanceActivityDialogController);

    InstanceActivityDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'InstanceActivity', 'InstanceActivityType', 'InstanceTur'];

    function InstanceActivityDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, InstanceActivity, InstanceActivityType, InstanceTur) {
        var vm = this;
        vm.instanceActivity = entity;
        vm.instanceactivitytypes = InstanceActivityType.query();
        vm.instanceturs = InstanceTur.query();
        vm.load = function(id) {
            InstanceActivity.get({id : id}, function(result) {
                vm.instanceActivity = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceActivityUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceActivity.id !== null) {
                InstanceActivity.update(vm.instanceActivity, onSaveSuccess, onSaveError);
            } else {
                InstanceActivity.save(vm.instanceActivity, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPhotoPrincipal = function ($file, instanceActivity) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        instanceActivity.photoPrincipal = base64Data;
                        instanceActivity.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
