(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('TrabajoDialogController', TrabajoDialogController);

    TrabajoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'Trabajo', 'Empresa'];

    function TrabajoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, Trabajo, Empresa) {
        var vm = this;

        vm.trabajo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;
        vm.empresas = Empresa.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.trabajo.id !== null) {
                Trabajo.update(vm.trabajo, onSaveSuccess, onSaveError);
            } else {
                Trabajo.save(vm.trabajo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:trabajoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.fechaInicio = false;
        vm.datePickerOpenStatus.fechaFin = false;

        vm.setMultimedia = function ($file, trabajo) {
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        trabajo.multimedia = base64Data;
                        trabajo.multimediaContentType = $file.type;
                    });
                });
            }
        };

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
