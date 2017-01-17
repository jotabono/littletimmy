
(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionDetailController', RecomendacionDetailController);

    RecomendacionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Recomendacion', 'User', 'Trabajo', 'Empresa', '$window', 'Recommend_notification'];

    function RecomendacionDetailController($scope, $rootScope, $stateParams, previousState, entity, Recomendacion, User, Trabajo, Empresa, $window, Recommend_notification) {
        var vm = this;

        vm.recomendacion = entity;
        vm.previousState = previousState.name;


        $window.document.title = "Recomendacion";

        vm.recomendacion.$promise.then(function (response) {
            $window.document.title = "Recomendacion de " + vm.recomendacion.recomendador.login;
        });

        var unsubscribe = $rootScope.$on('littletimmyApp:recomendacionUpdate', function(event, result) {
            vm.recomendacion = result;
        });
        $scope.$on('$destroy', unsubscribe);

        vm.update = update;

        function update(){
            if(!vm.recomendacion.aceptada){
                vm.recomendacion.aceptada = true;

                var notRecomendaciones = Recommend_notification.query({});
                notRecomendaciones.$promise.then(function(){
                    for(var i = 0; i < notRecomendaciones.length; i++){
                        if(notRecomendaciones[i].recomendacion.id == vm.recomendacion.id){
                            notRecomendaciones[i].leida = true;
                            Recommend_notification.update(notRecomendaciones[i]);
                        }
                    }
                });

                Recomendacion.update(vm.recomendacion, function(res){
                    $rootScope.$broadcast('updateNotifications');
                });
            }
        }
    }
})();
