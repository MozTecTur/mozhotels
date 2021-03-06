(function() {
    'use strict';

    angular
        .module('mozhotelsApp')
        .factory('LocalTurSearch', LocalTurSearch);

    LocalTurSearch.$inject = ['$resource'];

    function LocalTurSearch($resource) {
        var resourceUrl =  'api/_search/local-turs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
