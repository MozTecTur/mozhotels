(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceTurTypeSearch', InstanceTurTypeSearch);

    InstanceTurTypeSearch.$inject = ['$resource'];

    function InstanceTurTypeSearch($resource) {
        var resourceUrl =  'api/_search/instance-tur-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
