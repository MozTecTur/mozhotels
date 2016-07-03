(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('BookingPayment', BookingPayment);

    BookingPayment.$inject = ['$resource', 'DateUtils'];

    function BookingPayment ($resource, DateUtils) {
        var resourceUrl =  'api/booking-payments/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.date = DateUtils.convertDateTimeFromServer(data.date);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
