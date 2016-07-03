(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceInfoDetailController', InstanceInfoDetailController);

    InstanceInfoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'InstanceInfo', 'InstanceInfoType', 'InstanceTur'];

    function InstanceInfoDetailController($scope, $rootScope, $stateParams, entity, InstanceInfo, InstanceInfoType, InstanceTur) {
        var vm = this;
        vm.instanceInfo = entity;
        vm.load = function (id) {
            InstanceInfo.get({id: id}, function(result) {
                vm.instanceInfo = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceInfoUpdate', function(event, result) {
            vm.instanceInfo = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
