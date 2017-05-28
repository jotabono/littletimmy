'use strict';

describe('Controller Tests', function() {

    describe('Friendship_notification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockFriendship_notification, MockUser, MockFriend_user;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockFriendship_notification = jasmine.createSpy('MockFriendship_notification');
            MockUser = jasmine.createSpy('MockUser');
            MockFriend_user = jasmine.createSpy('MockFriend_user');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Friendship_notification': MockFriendship_notification,
                'User': MockUser,
                'Friend_user': MockFriend_user
            };
            createController = function() {
                $injector.get('$controller')("Friendship_notificationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'littletimmyApp:friendship_notificationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
