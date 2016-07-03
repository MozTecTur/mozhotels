'use strict';

describe('Controller Tests', function() {

    describe('Tourist Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockTourist, MockBooking, MockInstanceReview, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockTourist = jasmine.createSpy('MockTourist');
            MockBooking = jasmine.createSpy('MockBooking');
            MockInstanceReview = jasmine.createSpy('MockInstanceReview');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Tourist': MockTourist,
                'Booking': MockBooking,
                'InstanceReview': MockInstanceReview,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("TouristDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:touristUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
