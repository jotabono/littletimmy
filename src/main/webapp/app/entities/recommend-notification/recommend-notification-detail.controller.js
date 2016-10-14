(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Recommend_notificationDetailController', Recommend_notificationDetailController);

    Recommend_notificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Recommend_notification', 'User'];

    function Recommend_notificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Recommend_notification, User) {
        var vm = this;

        vm.recommend_notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:recommend_notificationUpdate', function(event, result) {
            vm.recommend_notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
