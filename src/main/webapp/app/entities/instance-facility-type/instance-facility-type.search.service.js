(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceFacilityTypeSearch', InstanceFacilityTypeSearch);

    InstanceFacilityTypeSearch.$inject = ['$resource'];

    function InstanceFacilityTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-facility-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
