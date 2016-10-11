(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('RecomendacionDialogController', RecomendacionDialogController);

    RecomendacionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Recomendacion', 'User', 'Trabajo', 'Empresa'];

    function RecomendacionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Recomendacion, User, Trabajo, Empresa) {
        var vm = this;

        vm.recomendacion = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        //vm.trabajos = Trabajo.query();
        vm.empresas = Empresa.query();
        vm.trabajos = {};
        var selectedUsers = 0;
        var recomendador = null;
        var recomendado = null;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.recomendacion.empresa = vm.recomendacion.trabajo.empresa;
            vm.isSaving = true;
            if (vm.recomendacion.id !== null) {
                Recomendacion.update(vm.recomendacion, onSaveSuccess, onSaveError);
            } else {
                Recomendacion.save(vm.recomendacion, onSaveSuccess, onSaveError);
            }
        }

        $scope.$watchCollection('vm.recomendacion.recomendador', function(e) {
            //vm.trabajos = Trabajo.getTrabajosUsers({recomendador: recomendador, recomendado: recomendado});
            console.log(e.login);
            if(e != undefined){
                selectedUsers += 1;
                recomendador = e.login;

                if(recomendador != e.login){
                    selectedUsers -= 1;
                    recomendado = e.login;
                }
            }
            if(selectedUsers == 2){
                recomendador = e.login;
                vm.trabajos = Trabajo.getTrabajosUsers({recomendador: recomendador, recomendado: recomendado});
            }
        });

        $scope.$watchCollection('vm.recomendacion.recomendado', function(e) {
            //vm.trabajos = Trabajo.getTrabajosUsers({recomendador: recomendador, recomendado: recomendado});
            console.log(e.login);
            if(e != undefined){
                selectedUsers += 1;
                recomendado = e.login;

                if(recomendado != e.login){
                    selectedUsers -= 1;
                    recomendado = e.login;
                }
            }
            if(selectedUsers == 2){
                recomendado = e.login;
                vm.trabajos = Trabajo.getTrabajosUsers({recomendador: recomendador, recomendado: recomendado});
            }
        });

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:recomendacionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaEnvio = false;
        vm.datePickerOpenStatus.fechaResolucion = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
