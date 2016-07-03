(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceReviewDetailController', InstanceReviewDetailController);

    InstanceReviewDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceReview', 'InstanceTur', 'Tourist'];

    function InstanceReviewDetailController($scope, $rootScope, $stateParams, entity, InstanceReview, InstanceTur, Tourist) {
        var vm = this;
        vm.instanceReview = entity;
        vm.load = function (id) {
            InstanceReview.get({id: id}, function(result) {
                vm.instanceReview = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceReviewUpdate', function(event, result) {
            vm.instanceReview = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
