(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceRoomTypeDialogController', InstanceRoomTypeDialogController);

    InstanceRoomTypeDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'InstanceRoomType', 'Picture', 'InstanceRoomFacility', 'InstanceTur', 'Booking'];

    function InstanceRoomTypeDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, InstanceRoomType, Picture, InstanceRoomFacility, InstanceTur, Booking) {
        var vm = this;
        vm.instanceRoomType = entity;
        vm.pictures = Picture.query();
        vm.instanceroomfacilitys = InstanceRoomFacility.query();
        vm.instanceturs = InstanceTur.query();
        vm.bookings = Booking.query();
        vm.load = function(id) {
            InstanceRoomType.get({id : id}, function(result) {
                vm.instanceRoomType = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceRoomTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceRoomType.id !== null) {
                InstanceRoomType.update(vm.instanceRoomType, onSaveSuccess, onSaveError);
            } else {
                InstanceRoomType.save(vm.instanceRoomType, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPhotoPrincipal = function ($file, instanceRoomType) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        instanceRoomType.photoPrincipal = base64Data;
                        instanceRoomType.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
