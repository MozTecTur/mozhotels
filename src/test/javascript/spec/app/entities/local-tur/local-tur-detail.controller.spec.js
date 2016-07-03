'use strict';

describe('Controller Tests', function() {

    describe('LocalTur Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockLocalTur, MockInstanceTur, MockProvince;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockLocalTur = jasmine.createSpy('MockLocalTur');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            MockProvince = jasmine.createSpy('MockProvince');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'LocalTur': MockLocalTur,
                'InstanceTur': MockInstanceTur,
                'Province': MockProvince
            };
            createController = function() {
                $injector.get('$controller')("LocalTurDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:localTurUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
