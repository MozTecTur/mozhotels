'use strict';

describe('Controller Tests', function() {

    describe('InstanceFacilityType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstanceFacilityType, MockInstanceFacility;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstanceFacilityType = jasmine.createSpy('MockInstanceFacilityType');
            MockInstanceFacility = jasmine.createSpy('MockInstanceFacility');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstanceFacilityType': MockInstanceFacilityType,
                'InstanceFacility': MockInstanceFacility
            };
            createController = function() {
                $injector.get('$controller')("InstanceFacilityTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instanceFacilityTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
