'use strict';

describe('Controller Tests', function() {

    describe('InstanceTur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceTur, MockPicture, MockInstanceContact, MockInstanceFacility, MockInstanceActivity, MockInstanceRoomType, MockInstancePolicy, MockInstanceInfo, MockInstanceReview, MockBooking, MockLocalTur, MockInstanceTurType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockPicture = jasmine.createSpy('MockPicture');
            MockInstanceContact = jasmine.createSpy('MockInstanceContact');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            MockInstanceActivity = jasmine.createSpy('MockInstanceActivity');
            MockInstanceRoomType = jasmine.createSpy('MockInstanceRoomType');
            MockInstancePolicy = jasmine.createSpy('MockInstancePolicy');
            MockInstanceInfo = jasmine.createSpy('MockInstanceInfo');
            MockInstanceReview = jasmine.createSpy('MockInstanceReview');
            MockBooking = jasmine.createSpy('MockBooking');
            MockLocalTur = jasmine.createSpy('MockLocalTur');
            MockInstanceTurType = jasmine.createSpy('MockInstanceTurType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceTur': MockInstanceTur,
                'Picture': MockPicture,
                'InstanceContact': MockInstanceContact,
                'InstanceFacility': MockInstanceFacility,
                'InstanceActivity': MockInstanceActivity,
                'InstanceRoomType': MockInstanceRoomType,
                'InstancePolicy': MockInstancePolicy,
                'InstanceInfo': MockInstanceInfo,
                'InstanceReview': MockInstanceReview,
                'Booking': MockBooking,
                'LocalTur': MockLocalTur,
                'InstanceTurType': MockInstanceTurType
            };
            createController = function() {
                $injector.get('$controller')("InstanceTurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instanceTurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
