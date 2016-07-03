'use strict';

describe('Controller Tests', function() {

    describe('InstanceRoomFacility Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceRoomFacility, MockInstanceRoomFacilityType, MockInstanceRoomType, MockBooking;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceRoomFacility = jasmine.createSpy('MockInstanceRoomFacility');
            MockInstanceRoomFacilityType = jasmine.createSpy('MockInstanceRoomFacilityType');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockBooking = jasmine.createSpy('MockBooking');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceRoomFacility': MockInstanceRoomFacility,
                'InstanceRoomFacilityType': MockInstanceRoomFacilityType,
                'InstanceRoomType': MockInstanceRoomType,
                'Booking': MockBooking
            };
            createController = function() {
                $injector.get('$controller')("InstanceRoomFacilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instanceRoomFacilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
