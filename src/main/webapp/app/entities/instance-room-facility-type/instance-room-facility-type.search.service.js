(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceRoomFacilityTypeSearch', InstanceRoomFacilityTypeSearch);

    InstanceRoomFacilityTypeSearch.$inject = ['$resource'];

    function InstanceRoomFacilityTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-room-facility-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
