(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceReviewDialogController', InstanceReviewDialogController);

    InstanceReviewDialogController.$inject = ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'InstanceReview', 'InstanceTur', 'Tourist'];

    function InstanceReviewDialogController ($scope, $stateParams, $uibModalInstance, entity, InstanceReview, InstanceTur, Tourist) {
        var vm = this;
        vm.instanceReview = entity;
        vm.instanceturs = InstanceTur.query();
        vm.tourists = Tourist.query();
        vm.load = function(id) {
            InstanceReview.get({id : id}, function(result) {
                vm.instanceReview = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('mozhotelsApp:instanceReviewUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        };

        var onSaveError = function () {
            vm.isSaving = false;
        };

        vm.save = function () {
            vm.isSaving = true;
            if (vm.instanceReview.id !== null) {
                InstanceReview.update(vm.instanceReview, onSaveSuccess, onSaveError);
            } else {
                InstanceReview.save(vm.instanceReview, onSaveSuccess, onSaveError);
            }
        };

        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
