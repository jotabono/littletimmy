(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('MessagesDetailController', MessagesDetailController);

    MessagesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Messages', 'User', 'Chat'];

    function MessagesDetailController($scope, $rootScope, $stateParams, previousState, entity, Messages, User, Chat) {
        var vm = this;

        vm.messages = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:messagesUpdate', function(event, result) {
            vm.messages = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
