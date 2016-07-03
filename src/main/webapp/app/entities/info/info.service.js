(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('Info', Info);

    Info.$inject = ['$resource'];

    function Info ($resource) {
        var resourceUrl =  'api/infos/:id';

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
