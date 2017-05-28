(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friendship_notificationDetailController', Friendship_notificationDetailController);

    Friendship_notificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Friendship_notification', 'User', 'Friend_user'];

    function Friendship_notificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Friendship_notification, User, Friend_user) {
        var vm = this;

        vm.friendship_notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:friendship_notificationUpdate', function(event, result) {
            vm.friendship_notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
