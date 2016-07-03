'use strict';

describe('Controller Tests', function() {

    describe('Booking Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockBooking, MockInstanceRoomType, MockInstanceRoomFacility, MockTourist, MockGuestTourist, MockInstanceTur, MockBookingPayment;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockBooking = jasmine.createSpy('MockBooking');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockInstanceRoomFacility = jasmine.createSpy('MockInstanceRoomFacility');
            MockTourist = jasmine.createSpy('MockTourist');
            MockGuestTourist = jasmine.createSpy('MockGuestTourist');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockBookingPayment = jasmine.createSpy('MockBookingPayment');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Booking': MockBooking,
                'InstanceRoomType': MockInstanceRoomType,
                'InstanceRoomFacility': MockInstanceRoomFacility,
                'Tourist': MockTourist,
                'GuestTourist': MockGuestTourist,
                'InstanceTur': MockInstanceTur,
                'BookingPayment': MockBookingPayment
            };
            createController = function() {
                $injector.get('$controller')("BookingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:bookingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
