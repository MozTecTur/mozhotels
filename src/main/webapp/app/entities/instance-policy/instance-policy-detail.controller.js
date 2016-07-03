(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstancePolicyDetailController', InstancePolicyDetailController);

    InstancePolicyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstancePolicy', 'InstancePolicyType', 'InstanceTur'];

    function InstancePolicyDetailController($scope, $rootScope, $stateParams, entity, InstancePolicy, InstancePolicyType, InstanceTur) {
        var vm = this;
        vm.instancePolicy = entity;
        vm.load = function (id) {
            InstancePolicy.get({id: id}, function(result) {
                vm.instancePolicy = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instancePolicyUpdate', function(event, result) {
            vm.instancePolicy = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
