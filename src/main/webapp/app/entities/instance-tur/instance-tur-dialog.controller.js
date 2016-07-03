(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceTurDialogController', InstanceTurDialogController);

    InstanceTurDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'InstanceTur', 'Picture', 'InstanceContact', 'InstanceFacility', 'InstanceActivity', 'InstanceRoomType', 'InstancePolicy', 'InstanceInfo', 'InstanceReview', 'Booking', 'LocalTur', 'InstanceTurType'];

    function InstanceTurDialogController ($scope, $stateParams, $uibModalInstance, DataUtils, entity, InstanceTur, Picture, InstanceContact, InstanceFacility, InstanceActivity, InstanceRoomType, InstancePolicy, InstanceInfo, InstanceReview, Booking, LocalTur, InstanceTurType) {
        var vm = this;
        vm.instanceTur = entity;
        vm.pictures = Picture.query();
        vm.instancecontacts = InstanceContact.query();
        vm.instancefacilitys = InstanceFacility.query();
        vm.instanceactivitys = InstanceActivity.query();
        vm.instanceroomtypes = InstanceRoomType.query();
        vm.instancepolicys = InstancePolicy.query();
        vm.instanceinfos = InstanceInfo.query();
        vm.instancereviews = InstanceReview.query();
        vm.bookings = Booking.query();
        vm.localturs = LocalTur.query();
        vm.instanceturtypes = InstanceTurType.query();
        vm.load = function(id) {
            InstanceTur.get({id : id}, function(result) {
                vm.instanceTur = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceTurUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceTur.id !== null) {
                InstanceTur.update(vm.instanceTur, onSaveSuccess, onSaveError);
            } else {
                InstanceTur.save(vm.instanceTur, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };

        vm.setPhotoPrincipal = function ($file, instanceTur) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        instanceTur.photoPrincipal = base64Data;
                        instanceTur.photoPrincipalContentType = $file.type;
                    });
                });
            }
        };

        vm.openFile = DataUtils.openFile;
        vm.byteSize = DataUtils.byteSize;
    }
})();
