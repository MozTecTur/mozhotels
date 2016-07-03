(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('InstanceTurSearch', InstanceTurSearch);

    InstanceTurSearch.$inject = ['$resource'];

    function InstanceTurSearch($resource) {
        var resourceUrl =  'api/_search/instance-turs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
