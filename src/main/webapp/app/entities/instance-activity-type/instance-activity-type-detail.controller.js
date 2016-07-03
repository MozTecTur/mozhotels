(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityTypeDetailController', InstanceActivityTypeDetailController);

    InstanceActivityTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceActivityType', 'InstanceActivity'];

    function InstanceActivityTypeDetailController($scope, $rootScope, $stateParams, entity, InstanceActivityType, InstanceActivity) {
        var vm = this;
        vm.instanceActivityType = entity;
        vm.load = function (id) {
            InstanceActivityType.get({id: id}, function(result) {
                vm.instanceActivityType = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceActivityTypeUpdate', function(event, result) {
            vm.instanceActivityType = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
