(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'Recommend_notification', 'Friendship_notification', '$interval', '$rootScope',  '$scope', '$http'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, Recommend_notification, Friendship_notification, $interval, $rootScope, $scope, $http) {
        var vm = this;
        $scope.resultsSearch = [];

        $scope.search = function(query){
            $http({
                method: 'GET',
                url: 'api/_search/'+query
            }).then(function successCallback(response) {
                $scope.resultsSearch = response.data;
            }, function errorCallback(response) {
            });
        }

        $scope.go = function () {
            $state.go('search',{q: $scope.searchQuery});
        }

        $scope.clearFinder = function () {
            $('#resultSearch').hide();
        };

        $scope.showResults = function () {
            $('#resultSearch').show();
        }

        var timeRefresh = 60000 * 3;

        $rootScope.$on("user-updated", function(e,res){
            vm.account = res;
            $('#image-user-navbar').attr("src",vm.account.imagen);
        });

        Principal.identity().then(function(account) {
            vm.notifications = Recommend_notification.getRNotificationsNotReaded();
            vm.friendship = Friendship_notification.getFNotificationsNotReaded();

            vm.account = account;

            $rootScope.$broadcast('emitUser',account);

            $interval(function() {
                vm.notifications = Recommend_notification.getRNotificationsNotReaded();
                vm.friendship = Friendship_notification.getFNotificationsNotReaded();
            }, timeRefresh);
        });

        $rootScope.$on('updateNotifications',function(){
            vm.notifications = Recommend_notification.getRNotificationsNotReaded();
        });

        $rootScope.$on('updateFriendships', function(){
            vm.friendship = Friendship_notification.getFNotificationsNotReaded();
        });

        $rootScope.$on('authenticationSuccess',function(){
            vm.notifications = Recommend_notification.getRNotificationsNotReaded();
            vm.friendship = Friendship_notification.getFNotificationsNotReaded();

            vm.friendship.$promise.then(function (res) {
                console.log(res);
            });

            Principal.identity().then(function(account) {
                vm.account = account;
                $rootScope.account = account;
            });

            $interval(function() {
                vm.notifications = Recommend_notification.getRNotificationsNotReaded();
                vm.friendship = Friendship_notification.getFNotificationsNotReaded();
            }, timeRefresh);
        });

        /*vm.readNotifications = function(){
            if(vm.notifications.length > 0){
                for(var i = 0; i < vm.notifications.length; i++){
                    vm.notifications[i].leida = true;
                    Recommend_notification.update(vm.notifications[i]);
                }
            }

        }*/

        vm.readNotifications = function (index) {
            vm.notifications[index].leida = true;
            Recommend_notification.update(vm.notifications[i]);
        }

        vm.readFriendships = function (index) {
            vm.friendship[index].friendship = true;
            Friendship_notification.update(vm.friendship[i]);
        }

        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });

        vm.login = login;
        vm.logout = logout;
        vm.toggleNavbar = toggleNavbar;
        vm.collapseNavbar = collapseNavbar;
        vm.$state = $state;

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            vm.notifications = null;
            vm.friendship = null;
            collapseNavbar();
            Auth.logout();
            $state.go('home');
            $rootScope.account = [];
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
    }
})();
