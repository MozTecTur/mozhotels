(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('Tourist', Tourist);

    Tourist.$inject = ['$resource'];

    function Tourist ($resource) {
        var resourceUrl =  'api/tourists/:id';

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
