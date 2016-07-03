'use strict';

describe('Controller Tests', function() {

    describe('InstanceRoomFacilityType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceRoomFacilityType, MockInstanceRoomFacility;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceRoomFacilityType = jasmine.createSpy('MockInstanceRoomFacilityType');
            MockInstanceRoomFacility = jasmine.createSpy('MockInstanceRoomFacility');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceRoomFacilityType': MockInstanceRoomFacilityType,
                'InstanceRoomFacility': MockInstanceRoomFacility
            };
            createController = function() {
                $injector.get('$controller')("InstanceRoomFacilityTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instanceRoomFacilityTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
