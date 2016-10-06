'use strict';

describe('Controller Tests', function() {

    describe('Estudios Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockEstudios, MockCentro;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockEstudios = jasmine.createSpy('MockEstudios');
            MockCentro = jasmine.createSpy('MockCentro');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Estudios': MockEstudios,
                'Centro': MockCentro
            };
            createController = function() {
                $injector.get('$controller')("EstudiosDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'littletimmyApp:estudiosUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
