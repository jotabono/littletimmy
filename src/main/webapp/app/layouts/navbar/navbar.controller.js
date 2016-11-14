(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('NavbarController', NavbarController);

    NavbarController.$inject = ['$state', 'Auth', 'Principal', 'ProfileService', 'LoginService', 'Recommend_notification', '$interval', '$rootScope'];

    function NavbarController ($state, Auth, Principal, ProfileService, LoginService, Recommend_notification, $interval, $rootScope) {
        var vm = this;

        var timeRefresh = 60000 * 3;

        Principal.identity().then(function(account) {
            vm.notifications = Recommend_notification.getRNotificationsNotReaded();

            vm.account = account;

            $rootScope.$broadcast('emitUser',account);

            $interval(function() {
                vm.notifications = Recommend_notification.getRNotificationsNotReaded();
            }, timeRefresh);
        });

        $rootScope.$on('authenticationSuccess',function(){
            vm.notifications = Recommend_notification.getRNotificationsNotReaded();

            Principal.identity().then(function(account) {
                vm.account = account;
                $rootScope.account = account;
            });

            $interval(function() {
                vm.notifications = Recommend_notification.getRNotificationsNotReaded();
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
