(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceFacilityType', InstanceFacilityType);

    InstanceFacilityType.$inject = ['$resource'];

    function InstanceFacilityType ($resource) {
        var resourceUrl =  'api/instance-facility-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
