(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecoTrabajoController', RecoTrabajoController);

    RecoTrabajoController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'recomendaciones', '$state', '$rootScope', 'Trabajo'];

    function RecoTrabajoController ($timeout, $scope, $stateParams, $uibModalInstance, recomendaciones, $state, $rootScope, Trabajo) {
        var vm = this;


        vm.account = $rootScope.account;

       	vm.recomendaciones = recomendaciones;
       	vm.trabajo;
       	vm.idtrabajo = $stateParams.id_trabajo;

       	console.log($stateParams.id_trabajo);

       	vm.recomendaciones.$promise.then(function(res){
       	    if(res.length != 0){
                vm.trabajo = vm.recomendaciones[0].trabajo;
            } else {
                vm.trabajo = Trabajo.get({id: vm.idtrabajo});
            }
            $rootScope.trabajo = vm.trabajo;
       	});
        vm.toProfile = toProfile;

        $rootScope.$on('littletimmyApp:nuevaRecomendacion', function (e,res){
            vm.recomendaciones.push(res);
        });

        function clear () {
          $uibModalInstance.dismiss('cancel');
        }
        function toProfile(){
          $uibModalInstance.dismiss('cancel');
        }
    }
})();
