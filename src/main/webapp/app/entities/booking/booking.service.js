(function() {
    'use strict';
    angular
        .module('mozhotelsApp')
        .factory('Booking', Booking);

    Booking.$inject = ['$resource', 'DateUtils'];

    function Booking ($resource, DateUtils) {
        var resourceUrl =  'api/bookings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.checkIn = DateUtils.convertDateTimeFromServer(data.checkIn);
                    data.checkOut = DateUtils.convertDateTimeFromServer(data.checkOut);
                    data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                    data.editDate = DateUtils.convertDateTimeFromServer(data.editDate);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
