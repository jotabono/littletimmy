(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoDialogController', RecoDialogController);

    RecoDialogController.$inject = ['$timeout', '$scope', '$rootScope', '$stateParams', '$uibModalInstance', '$state', "Recomendacion", "User"];

    function RecoDialogController ($timeout, $scope, $rootScope, $stateParams, $uibModalInstance, $state, Recomendacion, User) {
        var vm = this;
        vm.postRecomendation = postRecomendation;

        console.log($stateParams.id_trabajo);

        vm.clear = function(){
            $uibModalInstance.close();
        }

        function postRecomendation(){

            console.log($rootScope.trabajo);

            var recomendacion = {
                texto: $scope.text,
                recomendado: null,
                recomendador: null,
                trabajo: $rootScope.trabajo,
                empresa: $rootScope.trabajo.empresa
            }

            User.get({ login: $stateParams.user }, function(res){
               console.log(res);
               recomendacion.recomendado = res;
               recomendacion.recomendador = res;

                Recomendacion.save(recomendacion, function (res) {
                    $scope.$emit('littletimmyApp:nuevaRecomendacion', res);
                    console.log(res)
                    $uibModalInstance.close(res);
                }, function (error) {
                    console.log(error);
                });

            });
        }
    }

})();
