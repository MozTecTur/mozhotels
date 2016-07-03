(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('InstanceRoomFacility', InstanceRoomFacility);

    InstanceRoomFacility.$inject = ['$resource'];

    function InstanceRoomFacility ($resource) {
        var resourceUrl =  'api/instance-room-facilities/:id';

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
