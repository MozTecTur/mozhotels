(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .controller('InstanceActivityDetailController', InstanceActivityDetailController);

    InstanceActivityDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'DataUtils', 'entity', 'InstanceActivity', 'InstanceActivityType', 'InstanceTur'];

    function InstanceActivityDetailController($scope, $rootScope, $stateParams, DataUtils, entity, InstanceActivity, InstanceActivityType, InstanceTur) {
        var vm = this;
        vm.instanceActivity = entity;
        vm.load = function (id) {
            InstanceActivity.get({id: id}, function(result) {
                vm.instanceActivity = result;
            });
        };
        var unsubscribe = $rootScope.$on('mozhotelsApp:instanceActivityUpdate', function(event, result) {
            vm.instanceActivity = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
    }
})();
