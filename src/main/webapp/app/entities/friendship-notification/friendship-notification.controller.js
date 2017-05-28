(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('Friendship_notificationController', Friendship_notificationController);

    Friendship_notificationController.$inject = ['$scope', '$state', 'Friendship_notification', 'Friendship_notificationSearch'];

    function Friendship_notificationController ($scope, $state, Friendship_notification, Friendship_notificationSearch) {
        var vm = this;
        
        vm.friendship_notifications = [];
        vm.search = search;
        vm.loadAll = loadAll;

        loadAll();

        function loadAll() {
            Friendship_notification.query(function(result) {
                vm.friendship_notifications = result;
            });
        }

        function search () {
            if (!vm.searchQuery) {
                return vm.loadAll();
            }
            Friendship_notificationSearch.query({query: vm.searchQuery}, function(result) {
                vm.friendship_notifications = result;
            });
        }    }
})();
