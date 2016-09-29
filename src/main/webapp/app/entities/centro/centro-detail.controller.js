(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('CentroDetailController', CentroDetailController);

    CentroDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Centro'];

    function CentroDetailController($scope, $rootScope, $stateParams, previousState, entity, Centro) {
        var vm = this;

        vm.centro = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:centroUpdate', function(event, result) {
            vm.centro = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
