(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('PictureDialogController', PictureDialogController);

    PictureDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Picture', 'Province', 'InstanceTur', 'InstanceRoomType'];

    function PictureDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, Picture, Province, InstanceTur, InstanceRoomType) {
        var vm = this;
        vm.picture = entity;
        vm.provinces = Province.query();
        vm.instanceturs = InstanceTur.query();
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.load = function(id) {
            Picture.get({id : id}, function(result) {
                vm.picture = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:pictureUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.picture.id !== null) {
                Picture.update(vm.picture, onSaveSuccess, onSaveError);
            } else {
                Picture.save(vm.picture, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPicture = function ($file, picture) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        picture.picture = base64Data;
                        picture.pictureContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
