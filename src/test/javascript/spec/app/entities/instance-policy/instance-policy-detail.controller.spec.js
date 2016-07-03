'use strict';

describe('Controller Tests', function() {

    describe('InstancePolicy Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstancePolicy, MockInstancePolicyType, MockInstanceTur;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstancePolicy = jasmine.createSpy('MockInstancePolicy');
            MockInstancePolicyType = jasmine.createSpy('MockInstancePolicyType');
            MockInstanceTur = jasmine.createSpy('MockInstanceTur');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstancePolicy': MockInstancePolicy,
                'InstancePolicyType': MockInstancePolicyType,
                'InstanceTur': MockInstanceTur
            };
            createController = function() {
                $injector.get('$controller')("InstancePolicyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instancePolicyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
