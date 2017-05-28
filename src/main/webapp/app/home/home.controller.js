(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', '$http'];

    function HomeController ($scope, Principal, LoginService, $state, $http) {
        var vm = this;
        $scope.resultsSearch = [];

        var wrap = angular.element("#search-wrapper");

        wrap.focus(function(e) {
            console.log(e)
            $('#resultSearch').show();
        }).blur(function(e) {
            console.log(e)
            $('#resultSearch').hide();
        });

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

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }



    }
})();
