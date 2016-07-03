(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceRoomFacilityType', InstanceRoomFacilityType);

    InstanceRoomFacilityType.$inject = ['$resource'];

    function InstanceRoomFacilityType ($resource) {
        var resourceUrl =  'api/instance-room-facility-types/:id';

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
