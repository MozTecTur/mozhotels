(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('ProvinceDialogController', ProvinceDialogController);

    ProvinceDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Province', 'LocalTur', 'Picture', 'Region'];

    function ProvinceDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, Province, LocalTur, Picture, Region) {
        var vm = this;
        vm.province = entity;
        vm.localturs = LocalTur.query();
        vm.pictures = Picture.query();
        vm.regions = Region.query();
        vm.load = function(id) {
            Province.get({id : id}, function(result) {
                vm.province = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:provinceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.province.id !== null) {
                Province.update(vm.province, onSaveSuccess, onSaveError);
            } else {
                Province.save(vm.province, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPhotoPrincipal = function ($file, province) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        province.photoPrincipal = base64Data;
                        province.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
