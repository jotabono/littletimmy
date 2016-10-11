(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friend_userDetailController', Friend_userDetailController);

    Friend_userDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Friend_user', 'User'];

    function Friend_userDetailController($scope, $rootScope, $stateParams, previousState, entity, Friend_user, User) {
        var vm = this;

        vm.friend_user = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:friend_userUpdate', function(event, result) {
            vm.friend_user = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
