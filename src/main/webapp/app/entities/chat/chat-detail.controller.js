(function() {
    'use strict';

    angular
        .module('littletimmyApp')
        .controller('ChatDetailController', ChatDetailController);

    ChatDetailController.$inject = ['$scope', '$rootScope', '$timeout', '$interval', '$window', 'previousState', 'entity', 'Chat', 'User', 'Messages', 'ChatTrackerService'];

    function ChatDetailController($scope, $rootScope, $timeout, $interval, $window, previousState, entity, Chat, User, Messages, ChatTrackerService) {
        var vm = this;

        vm.chat = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('littletimmyApp:chatUpdate', function(event, result) {
            vm.chat = result;
        });

        $scope.$on('$destroy', unsubscribe);

        $rootScope.$on('emitUser',function(e,user){
            $rootScope.account = user;
        });

        entity.$promise.then(function(res){

            ChatTrackerService.connect('chat', res.id);

            ChatTrackerService.receive().then(null, null, function(message) {
                vm.chat.messages.push(message);
                //scrollChat();
                scroll();
            });

            $window.document.title = "Chat: " + vm.chat.name;
            $timeout(function () {
                $("#messagesId").scrollTop($('#messagesId').prop("scrollHeight"));
            }, 10);

        });

        vm.post = post;

        $rootScope.$on('$stateChangeStart', function(event, toState, toParams, fromState, fromParams){
            ChatTrackerService.unsubscribe();
        });

        function post(msg) {
            //var text = ApplyLineBreaks('messageInputId');
            var post = {
                text: msg,
                chat: vm.chat
            }
            if(msg.length >= 1){
                Messages.save(post, addMsg);
            }
        }

        function scrollChat(){
            $("#messagesId").animate({ scrollTop: $('#messagesId').prop("scrollHeight")}, 500);
        }

        function scroll(){
            $timeout(function () {
                $("#messagesId").scrollTop($('#messagesId').prop("scrollHeight"));
            }, 10);
        }

        function addMsg(res){
            ChatTrackerService.sendActivity(vm.chat.id, res);
            //vm.chat.messages.push(res);
            $scope.msg = null;
            $('#messageInputId').val("");
            //scrollChat();
        }

        /*function ApplyLineBreaks(strTextAreaId) {
            var oTextarea = document.getElementById(strTextAreaId);
            if (oTextarea.wrap) {
                oTextarea.setAttribute("wrap", "off");
            }
            else {
                oTextarea.setAttribute("wrap", "off");
                var newArea = oTextarea.cloneNode(true);
                newArea.value = oTextarea.value;
                oTextarea.parentNode.replaceChild(newArea, oTextarea);
                oTextarea = newArea;
            }

            var strRawValue = oTextarea.value;
            oTextarea.value = "";
            var nEmptyWidth = oTextarea.scrollWidth;
            var nLastWrappingIndex = -1;
            for (var i = 0; i < strRawValue.length; i++) {
                var curChar = strRawValue.charAt(i);
                if (curChar == ' ' || curChar == '-' || curChar == '+')
                    nLastWrappingIndex = i;
                oTextarea.value += curChar;
                if (oTextarea.scrollWidth / 3> nEmptyWidth / 3) {
                    var buffer = "";
                    console.log(nLastWrappingIndex);
                    if (nLastWrappingIndex >= 0) {
                        for (var j = nLastWrappingIndex + 1; j < i; j++)
                            buffer += strRawValue.charAt(j);
                        nLastWrappingIndex = -1;
                    }
                    buffer += curChar;
                    oTextarea.value = oTextarea.value.substr(0, oTextarea.value.length - buffer.length);
                    oTextarea.value += "\n" + buffer;
                }
            }
            oTextarea.setAttribute("wrap", "");//.replace(new RegExp("\\n", "g"), "<br />");
            var text = oTextarea.value;
            return text;
        }*/

    }
})();
