(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('MessagesDialogController', MessagesDialogController);

    MessagesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Messages', 'User', 'Chat'];

    function MessagesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Messages, User, Chat) {
        var vm = this;

        vm.messages = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.chats = Chat.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.messages.id !== null) {
                Messages.update(vm.messages, onSaveSuccess, onSaveError);
            } else {
                Messages.save(vm.messages, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('littletimmyApp:messagesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.sendDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
