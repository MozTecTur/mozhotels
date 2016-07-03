'use strict';

describe('Controller Tests', function() {

    describe('InstanceFacility Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceFacility, MockInstanceFacilityType, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            MockInstanceFacilityType = jasmine.createSpy('MockInstanceFacilityType');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceFacility': MockInstanceFacility,
                'InstanceFacilityType': MockInstanceFacilityType,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstanceFacilityDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instanceFacilityUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
