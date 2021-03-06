(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceInfoTypeSearch', InstanceInfoTypeSearch);

    InstanceInfoTypeSearch.$inject = ['$resource'];

    function InstanceInfoTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-info-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
