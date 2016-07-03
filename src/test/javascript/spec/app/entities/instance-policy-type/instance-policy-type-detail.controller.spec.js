'use strict';

describe('Controller Tests', function() {

    describe('InstancePolicyType Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstancePolicyType, MockInstancePolicy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstancePolicyType = jasmine.createSpy('MockInstancePolicyType');
            MockInstancePolicy = jasmine.createSpy('MockInstancePolicy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'InstancePolicyType': MockInstancePolicyType,
                'InstancePolicy': MockInstancePolicy
            };
            createController = function() {
                $injector.get('$controller')("InstancePolicyTypeDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'mozhotelsApp:instancePolicyTypeUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
