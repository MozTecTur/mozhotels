(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceRoomFacilitySearch', InstanceRoomFacilitySearch);

    InstanceRoomFacilitySearch.$inject = ['$resource'];

    function InstanceRoomFacilitySearch($resource) {
        var resourceUrl =  'api/_search/instance-room-facilities/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
