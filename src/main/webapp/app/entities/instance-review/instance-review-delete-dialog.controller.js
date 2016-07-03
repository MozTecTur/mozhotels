(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceReviewDeleteController',InstanceReviewDeleteController);

    InstanceReviewDeleteController.$inject = ['$uibModalInstance', 'entity', 'InstanceReview'];

    function InstanceReviewDeleteController($uibModalInstance, entity, InstanceReview) {
        var vm = this;
        vm.instanceReview = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            InstanceReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
