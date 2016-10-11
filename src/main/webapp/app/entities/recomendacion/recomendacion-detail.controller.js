(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionDetailController', RecomendacionDetailController);

    RecomendacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Recomendacion', 'User', 'Trabajo', 'Empresa'];

    function RecomendacionDetailController($scope, $rootScope, $stateParams, previousState, entity, Recomendacion, User, Trabajo, Empresa) {
        var vm = this;

        vm.recomendacion = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:recomendacionUpdate', function(event, result) {
            vm.recomendacion = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
